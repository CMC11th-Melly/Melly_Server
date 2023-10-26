package cmc.mellyserver.builder;

import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;

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
