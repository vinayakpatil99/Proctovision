package com.prctovision.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI Mock Interview Service
 * - Supports session-based chat
 * - Calls OpenRouter API (real AI)
 * - Falls back to offline logic if network fails
 */
@Service
public class InterviewService {

    @Value("${openrouter.api.key}")
    private String openRouterKey;

    @Value("${openrouter.api.url}")
    private String openRouterUrl;

    @Value("${openrouter.model}")
    private String modelName;

    // Thread-safe session store (no DB)
    private final Map<String, List<Map<String, String>>> sessions = new ConcurrentHashMap<>();

    /**
     * Start a new mock interview session
     */
    public Map<String, String> startInterview(String role, String experience) {
        String sessionId = UUID.randomUUID().toString();

        List<Map<String, String>> history = new ArrayList<>();
        String systemPrompt = "You are a professional interviewer. Conduct a mock interview for a "
                + experience + " " + role
                + ". Ask one question at a time. After each candidate answer, give a short evaluation "
                + "and ask the next question. Be concise and polite. End with a summary and a score out of 10.";

        history.add(Map.of("role", "system", "content", systemPrompt));
        sessions.put(sessionId, history);

        // Ask the first question
        String firstQuestion = callModel(sessionId, "Start the interview with the first question.");

        Map<String, String> result = new HashMap<>();
        result.put("sessionId", sessionId);
        result.put("question", firstQuestion);
        return result;
    }

    /**
     * Candidate submits answer → AI replies
     */
    public String submitAnswer(String sessionId, String answerText) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Invalid sessionId");
        }
        return callModel(sessionId, answerText);
    }

    /**
     * Core AI call — includes timeout & offline fallback
     */
    private String callModel(String sessionId, String userMessage) {
        List<Map<String, String>> history = sessions.get(sessionId);
        history.add(Map.of("role", "user", "content", userMessage));

        try {
            // --- Timeout fix ---
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(10000); // 10 sec
            factory.setReadTimeout(20000);    // 20 sec
            RestTemplate restTemplate = new RestTemplate(factory);

            Map<String, Object> body = new HashMap<>();
            body.put("model", modelName);
            body.put("messages", history);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openRouterKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> resp = restTemplate.exchange(openRouterUrl, HttpMethod.POST, request, Map.class);

            if (resp.getStatusCode() != HttpStatus.OK)
                throw new RuntimeException("OpenRouter returned: " + resp.getStatusCode());

            Map respBody = resp.getBody();
            if (respBody == null || !respBody.containsKey("choices"))
                throw new RuntimeException("Unexpected response: " + respBody);

            List choices = (List) respBody.get("choices");
            if (choices.isEmpty()) return "";

            Map first = (Map) choices.get(0);
            Map message = (Map) first.get("message");
            String content = (String) message.get("content");

            // Save assistant reply
            history.add(Map.of("role", "assistant", "content", content));
            sessions.put(sessionId, history);

            return content;

        } catch (Exception e) {
            System.err.println("⚠️ [Mock Mode] API failed: " + e.getMessage());

            // --- Offline fallback logic ---
            return offlineResponse(userMessage);
        }
    }

    /**
     * Local mock fallback — runs without internet
     */
    private String offlineResponse(String userInput) {
        userInput = userInput.toLowerCase();

        if (userInput.contains("start") || userInput.contains("hello"))
            return "Hi! Let's begin your mock interview. Tell me about yourself.";
        if (userInput.contains("project"))
            return "Can you explain your final year project and what technologies you used?";
        if (userInput.contains("strength"))
            return "What are your main strengths as a developer?";
        if (userInput.contains("weakness"))
            return "What is one area you’re currently trying to improve?";
        if (userInput.contains("thank"))
            return "Great talking with you! I’d rate this interview 8/10.";
        return "Interesting! Could you elaborate more on that?";
    }
}
