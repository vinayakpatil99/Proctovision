package com.prctovision.repository;

import com.prctovision.model.ProctorLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProctorLogRepository extends JpaRepository<ProctorLog, Long> {

    // ✅ Custom query to fetch logs with student and exam eagerly joined
    @Query("SELECT pl FROM ProctorLog pl " +
           "LEFT JOIN FETCH pl.student " +
           "LEFT JOIN FETCH pl.exam " +
           "ORDER BY pl.eventTime DESC")
    List<ProctorLog> findAllWithStudentAndExam();
}
