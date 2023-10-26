package cmc.mellyserver.domain.group.repository;

import cmc.mellyserver.common.fixture.GroupFixtures;
import cmc.mellyserver.common.fixture.UserFixtures;
import cmc.mellyserver.config.RepositoryTest;
import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserGroupQueryRepositoryTest extends RepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupAndUserRepository groupAndUserRepository;

	private User 모카;

	private User 머식;

	private List<UserGroup> groups;

	@BeforeEach
	void setUp() {

		// given
		User user1 = UserFixtures.모카();
		모카 = userRepository.save(user1);

		User user2 = UserFixtures.머식();
		머식 = userRepository.save(user2);

		UserGroup 친구_그룹 = GroupFixtures.친구그룹();

		GroupAndUser 친구_그룹_연결_1 = new GroupAndUser(모카, 친구_그룹);
		GroupAndUser 친구_그룹_연결_2 = new GroupAndUser(머식, 친구_그룹);

		groupAndUserRepository.saveAll(List.of(친구_그룹_연결_1, 친구_그룹_연결_2));
		groups = groupRepository.saveAll(List.of(친구_그룹));
	}

}
