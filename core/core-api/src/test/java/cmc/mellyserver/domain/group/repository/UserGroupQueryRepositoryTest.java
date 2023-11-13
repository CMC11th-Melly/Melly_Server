package cmc.mellyserver.domain.group.repository;

import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.support.RepositoryTestSupport;

public class UserGroupQueryRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupAndUserRepository groupAndUserRepository;
}
