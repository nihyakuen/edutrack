package aloe.edutrack.domain.subject;

import aloe.edutrack.exception.ResourceAlreadyExistsException;
import aloe.edutrack.domain.subject.dto.SubjectCreateRequest;
import aloe.edutrack.domain.subject.dto.SubjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectResponse create(SubjectCreateRequest request) {

        if (subjectRepository.existsByCode(request.code())) {
            throw new ResourceAlreadyExistsException("Subject already exists: " + request.code());
        }

        Subject subject = new Subject(
                request.code(),
                request.name()
        );

        Subject saved = subjectRepository.save(subject);

        return toResponse(saved);
    }

    public Page<SubjectResponse> getAllSubjects(Pageable pageable) {
        Page<Subject> subjectPage = subjectRepository.findAll(pageable);
        return subjectPage.map(this::toResponse);
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getCode(),
                subject.getName()
        );
    }
}
