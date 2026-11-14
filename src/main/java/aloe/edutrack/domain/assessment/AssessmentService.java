package aloe.edutrack.domain.assessment;

import aloe.edutrack.domain.assessment.dto.AssessmentCreateRequest;
import aloe.edutrack.domain.assessment.dto.AssessmentResponse;
import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import aloe.edutrack.domain.subjectassignment.SubjectAssignmentRepository;
import aloe.edutrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final SubjectAssignmentRepository subjectAssignmentRepository;

    @Transactional
    public AssessmentResponse create(AssessmentCreateRequest request, Long currentUserId) {
        SubjectAssignment assignment = subjectAssignmentRepository
                .findById(request.subjectAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject assignment", request.subjectAssignmentId()
                ));

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assignment does not belong to current teacher"
            );
        }

        Assessment assessment = new Assessment(
                assignment,
                request.title().trim(),
                request.type(),
                request.assessmentDate(),
                request.maxScore()
        );

        Assessment saved = assessmentRepository.save(assessment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<AssessmentResponse> findAllBySubjectAssignmentId(Long subjectAssignmentId, Long currentUserId, Pageable pageable) {
        SubjectAssignment assignment = subjectAssignmentRepository
                .findById(subjectAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject assignment", subjectAssignmentId
                ));

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assignment does not belong to current teacher"
            );
        }

        Page<Assessment> assessments = assessmentRepository.findAllBySubjectAssignmentId(subjectAssignmentId, pageable);

        return assessments.map(this::toResponse);
    }

    private AssessmentResponse toResponse(Assessment assessment) {
        return new AssessmentResponse(
                assessment.getId(),
                assessment.getSubjectAssignment().getId(),
                assessment.getTitle(),
                assessment.getType(),
                assessment.getAssessmentDate(),
                assessment.getMaxScore()
        );
    }
}
