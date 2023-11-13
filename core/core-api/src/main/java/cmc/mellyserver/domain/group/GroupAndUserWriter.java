package cmc.mellyserver.domain.group;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupAndUserWriter {

  private final GroupAndUserRepository groupAndUserRepository;

  public GroupAndUser save(GroupAndUser groupAndUser) {
	return groupAndUserRepository.save(groupAndUser);
  }

  public void deleteByUserIdAndGroupId(Long userId, Long groupId) {
	groupAndUserRepository.deleteByUserIdAndGroupId(userId, groupId);
  }

}
