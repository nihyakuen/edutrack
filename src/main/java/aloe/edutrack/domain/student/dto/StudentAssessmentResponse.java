package aloe.edutrack.domain.student.dto;

import aloe.edutrack.domain.assessment.AssessmentType;

import java.time.LocalDate;

public record StudentAssessmentResponse(
        Long assessmentId,
        String title,
        AssessmentType type,
        LocalDate assessmentDate,
        int maxScore,
        Integer score
) {}