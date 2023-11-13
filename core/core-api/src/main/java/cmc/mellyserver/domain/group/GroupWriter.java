package cmc.mellyserver.domain.group;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupWriter {

	private final GroupRepository groupRepository;

	public UserGroup save(UserGroup userGroup) {
		return groupRepository.save(userGroup);
	}

}
