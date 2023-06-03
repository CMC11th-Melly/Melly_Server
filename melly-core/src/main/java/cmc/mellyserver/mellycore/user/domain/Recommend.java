package cmc.mellyserver.mellycore.user.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import cmc.mellyserver.mellycore.common.enums.RecommendActivity;
import cmc.mellyserver.mellycore.common.enums.RecommendGroup;
import cmc.mellyserver.mellycore.common.enums.RecommendPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Recommend {

	@Enumerated(EnumType.STRING)
	private RecommendGroup recommendGroup;

	private RecommendPlace recommendPlace;

	private RecommendActivity recommendActivity;

}
