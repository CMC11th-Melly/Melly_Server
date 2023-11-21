package cmc.mellyserver.domain.group;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupAndUserReader {

    private final GroupAndUserRepository groupAndUserRepository;

    public List<GroupMemberResponseDto> getGroupMembers(Long userId, Long groupId) {
        List<User> users = groupAndUserRepository.getUsersParticipatedInGroup(groupId);
        return users.stream()
            .map(user -> GroupMemberResponseDto.of(user.getId(), user.getProfileImage(), user.getNickname(),
                user.getId().equals(userId))
            )
            .collect(Collectors.toList());
    }

    public int countGroupMembers(Long groupId) {
        return groupAndUserRepository.countUserParticipatedInGroup(groupId);
    }

    public Optional<GroupAndUser> findByUserIdAndGroupId(Long userId, Long groupId) {
        return groupAndUserRepository.findByUserIdAndGroupId(userId, groupId);
    }

}
