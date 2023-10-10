package cmc.mellyserver.domain.group;

import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupWriter {

    private final GroupRepository groupRepository;

    public UserGroup save(UserGroup userGroup) {
        return groupRepository.save(userGroup);
    }
}
