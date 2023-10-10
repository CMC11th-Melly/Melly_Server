package cmc.mellyserver.domain.group;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupAndUserReader {

    private final GroupAndUserRepository groupAndUserRepository;

    public List<User> getGroupMembers(Long groupId) {
        return groupAndUserRepository.getUsersParticipatedInGroup(groupId);
    }

    public int countGroupMembers(Long groupId) {
        return groupAndUserRepository.countUserParticipatedInGroup(groupId);
    }

    public Optional<GroupAndUser> findByUserIdAndGroupId(final Long userId, final Long groupId) {
        return groupAndUserRepository.findByUserIdAndGroupId(userId, groupId);
    }
}
