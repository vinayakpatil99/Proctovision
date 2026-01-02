package com.prctovision.repository;

import com.prctovision.model.ExamInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExamInviteRepository extends JpaRepository<ExamInvite, Long> {
    Optional<ExamInvite> findByAccessCode(String accessCode);
}
