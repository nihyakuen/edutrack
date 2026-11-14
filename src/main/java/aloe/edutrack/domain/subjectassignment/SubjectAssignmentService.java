package aloe.edutrack.domain.subjectassignment;

import aloe.edutrack.domain.user.User;
import aloe.edutrack.domain.user.UserRepository;
import aloe.edutrack.domain.user.UserRole;
import aloe.edutrack.exception.BusinessRuleException;
import aloe.edutrack.exception.ResourceAlreadyExistsException;
import aloe.edutrack.exception.ResourceNotFoundException;
import aloe.edutrack.domain.group.StudyGroup;
import aloe.edutrack.domain.group.StudyGroupRepository;
import aloe.edutrack.domain.subject.Subject;
import aloe.edutrack.domain.subject.SubjectRepository;
import aloe.edutrack.domain.subjectassignment.dto.SubjectAssignmentCreateRequest;
import aloe.edutrack.domain.subjectassignment.dto.SubjectAssignmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubjectAssignmentService {
    private final SubjectAssignmentRepository subjectAssignmentRepository;
    private final SubjectRepository subjectRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubjectAssignmentResponse create(SubjectAssignmentCreateRequest request) {
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", request.subjectId()));

        StudyGroup group = studyGroupRepository.findById(request.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Study group", request.groupId()));

        User teacher = userRepository.findById(request.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("User(Teacher)", request.teacherId()));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new BusinessRuleException("User must have TEACHER role");
        }

        boolean exists = subjectAssignmentRepository.existsAssignment(
                subject.getId(),
                group.getId(),
                request.academicYear(),
                request.semester()
        );
        if (exists) throw new ResourceAlreadyExistsException("Subject assignment already exists");

        SubjectAssignment assignment = new SubjectAssignment(
                subject,
                group,
                teacher,
                request.academicYear(),
                request.semester()
        );

        SubjectAssignment saved = subjectAssignmentRepository.save(assignment);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<SubjectAssignmentResponse> findAllSubjectAssignments(
            Long groupId,
            Pageable pageable,
            Long currentUserId,
            UserRole role
    ) {
        Page<SubjectAssignment> assignments;

        if (role == UserRole.ADMIN) {
            assignments = groupId == null
                    ? subjectAssignmentRepository.findAll(pageable)
                    : subjectAssignmentRepository.findAllByGroupId(groupId, pageable);
        } else { // TEACHER
            assignments = groupId == null
                    ? subjectAssignmentRepository.findAllByTeacherId(currentUserId, pageable)
                    : subjectAssignmentRepository.findAllByTeacherIdAndGroupId(
                    currentUserId, groupId, pageable
            );
        }

        return assignments.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public SubjectAssignmentResponse getSubjectAssignmentById(Long id) {
        SubjectAssignment assignment = subjectAssignmentRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject assignment", id));

        return toResponse(assignment);
    }

    private SubjectAssignmentResponse toResponse(SubjectAssignment assignment) {
        User teacher = assignment.getTeacher();

        return new SubjectAssignmentResponse(
                assignment.getId(),

                assignment.getSubject().getId(),
                assignment.getSubject().getCode(),
                assignment.getSubject().getName(),

                assignment.getGroup().getId(),
                assignment.getGroup().getCode(),

                teacher.getId(),
                teacher.getFirstName() + " " + teacher.getLastName(),

                assignment.getAcademicYear(),
                assignment.getSemester()
        );
    }
}
