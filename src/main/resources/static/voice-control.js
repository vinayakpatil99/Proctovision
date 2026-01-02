//---------------- Global Voice Control ----------------
window.SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

if (window.SpeechRecognition) {
    const recognition = new window.SpeechRecognition();
    recognition.continuous = true;
    recognition.interimResults = false;
    recognition.lang = 'en-US';

    const voiceBtn = document.getElementById('voiceBtn'); // Only exists on Home
    let voiceActive = localStorage.getItem('voiceActive') === 'true'; // Persistent across pages
    let isRecognizing = false;

    function updateButton() {
        if (!voiceBtn) return;
        voiceBtn.style.background = voiceActive ? '#52c41a' : '#fff';
        voiceBtn.style.color = voiceActive ? '#fff' : '#1890ff';
    }

    updateButton();

    function safeStart() {
        if (isRecognizing) return;
        try {
            recognition.start();
            isRecognizing = true;
            console.log("🎤 Voice recognition started");
        } catch (err) {
            console.warn("Start blocked:", err.message);
        }
    }

    // Toggle mic only on Home page
    if (voiceBtn) {
        voiceBtn.addEventListener('click', () => {
            voiceActive = !voiceActive;
            localStorage.setItem('voiceActive', voiceActive);
            updateButton();

            if (voiceActive) safeStart();
            else {
                recognition.stop();
                isRecognizing = false;
                console.log("🛑 Voice stopped");
            }
        });
    }

	recognition.onresult = (event) => {
	    if (!voiceActive) return;

	    const transcript = event.results[event.results.length - 1][0].transcript.trim().toLowerCase();
	    console.log("🎧 Heard:", transcript);

	    // === Mutually exclusive command checks ===
	    if (transcript.includes('platform')) {
	        document.getElementById('platform')?.scrollIntoView({ behavior: 'smooth' });
	    } else if (transcript.includes('proctoring')) {
	        document.getElementById('proctoring')?.scrollIntoView({ behavior: 'smooth' });
	    } else if (transcript.includes('testimonials')) {
	        document.getElementById('testimonials')?.scrollIntoView({ behavior: 'smooth' });
	    } else if (transcript.includes('contact')) {
	        document.getElementById('contact')?.scrollIntoView({ behavior: 'smooth' });
	    } else if (transcript.includes('home') || transcript.includes('back to home')) {
	        window.location.href = '/home.html';
	    } else if (transcript.includes('login')) {
	        window.location.href = '/login.html';
	    } else if (transcript.includes('register') || transcript.includes('registration')) {
	        window.location.href = '/register.html';
	    } else if (transcript.includes('ai assistant')) {
	        window.location.href = '/voice-ai-continuous.html';
	    }
	};
    recognition.onerror = (event) => {
        console.error("Speech recognition error:", event.error);
        isRecognizing = false;
    };

    recognition.onend = () => {
        isRecognizing = false;
        if (voiceActive) safeStart(); // Restart automatically
    };

    // Auto-start if previously active (even on Login/Register)
    if (voiceActive) safeStart();

} else {
    console.warn("Speech Recognition not supported in this browser.");
}
