package aloe.edutrack.domain.group;

import aloe.edutrack.exception.ResourceAlreadyExistsException;
import aloe.edutrack.domain.group.dto.StudyGroupCreateRequest;
import aloe.edutrack.domain.group.dto.StudyGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public StudyGroupResponse create(StudyGroupCreateRequest request) {

        if (studyGroupRepository.existsByCode(request.code())) {
            throw new ResourceAlreadyExistsException("Group already exists: " + request.code());
        }

        StudyGroup group = new StudyGroup(request.code());

        StudyGroup saved = studyGroupRepository.save(group);
        return toResponse(saved);
    }

    public Page<StudyGroupResponse> getAllStudyGroups(Pageable pageable) {
        Page<StudyGroup> studyGroupPage = studyGroupRepository.findAll(pageable);
        return studyGroupPage.map(this::toResponse);
    }


    private StudyGroupResponse toResponse(StudyGroup studyGroup) {
        return new StudyGroupResponse(
                studyGroup.getId(),
                studyGroup.getCode()
        );
    }
}
