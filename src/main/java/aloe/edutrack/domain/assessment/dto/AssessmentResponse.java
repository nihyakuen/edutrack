package aloe.edutrack.domain.assessment.dto;

import aloe.edutrack.domain.assessment.AssessmentType;

import java.time.LocalDate;

public record AssessmentResponse (
        Long id,
        Long subjectAssignmentId,
        String title,
        AssessmentType type,
        LocalDate assessmentDate,
        int maxScore
) {}
