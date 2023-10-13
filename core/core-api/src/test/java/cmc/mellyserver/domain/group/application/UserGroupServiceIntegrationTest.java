package cmc.mellyserver.domain.group.application;


import cmc.mellyserver.config.IntegrationTest;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.support.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
public class UserGroupServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupAndUserRepository groupAndUserRepository;


    @DisplayName("그룹의_인원은_10명을_초과할수없다")
    @Test
    void 그룹의_인원은_10명을_초과할수없다() {

        // given
        UserGroup group = UserGroup.builder().groupName("테스트 그룹").groupType(GroupType.FRIEND).build();
        UserGroup 테스트_그룹 = groupRepository.save(group);

        for (int i = 0; i < 10; i++) {
            User 모카 = 모카().회원();
            groupService.participateToGroup(모카.getId(), 테스트_그룹.getId());
        }

        // when & then
        User 마지막_회원 = 머식().회원();

        Assertions.assertThatThrownBy(() -> {
                    groupService.participateToGroup(마지막_회원.getId(), 테스트_그룹.getId());
                }
        ).isInstanceOf(BusinessException.class).hasMessage("그룹의 인원은 최대 10명 입니다.");
    }
    

    @DisplayName("분산락을 적용해 동시 접속자가 생겨도 10명 인원제한을 유지한다")
    @Test
    void 분산락을_적용해_동시접속자가_생겨도_10명제한을_유지한다() throws InterruptedException {

        // given
        UserGroup group = UserGroup.builder().groupName("테스트 그룹").groupType(GroupType.FRIEND).build();
        UserGroup 테스트_그룹 = groupRepository.save(group);

        for (int i = 0; i < 9; i++) {
            User 모카 = 모카().회원();
            groupService.participateToGroup(모카.getId(), 테스트_그룹.getId());
        }

        // when
        User 동시접속유저_1 = 모카().회원();
        User 동시접속유저_2 = 모카().회원();

        Thread thread1 = new Thread(
                () -> groupService.participateToGroup(동시접속유저_1.getId(), 테스트_그룹.getId()));
        Thread thread2 = new Thread(
                () -> groupService.participateToGroup(동시접속유저_2.getId(), 테스트_그룹.getId()));
        try {
            thread1.start();
            thread2.start();
        } catch (BusinessException ex) {
            log.info(ex.getMessage());
        }

        // then
        Integer count = groupAndUserRepository.countUserParticipatedInGroup(테스트_그룹.getId());
        Assertions.assertThat(count).isEqualTo(9);
    }
}



