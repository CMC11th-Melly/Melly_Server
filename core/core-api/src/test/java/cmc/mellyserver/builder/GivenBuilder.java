package cmc.mellyserver.builder;

import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.AgeGroup;
import cmc.mellyserver.mellycore.user.domain.enums.Gender;

public final class GivenBuilder {

    private final BuilderSupporter bs;
    private User user;
    private Memory memory;
    private UserGroup userGroup;
    private PlaceScrap placeScrap;
    private Notification notification;
    private Place place;

    public GivenBuilder(final BuilderSupporter bs) {
        this.bs = bs;
    }

    public GivenBuilder 회원_가입을_한다(final String email, final String nickname) {

        User user = User.createEmailLoginUser(email, "1234", nickname, AgeGroup.TWO, Gender.MALE);
        this.user = bs.getUserRepository().save(user);
        return this;
    }

    public User 회원() {
        return user;
    }

}
