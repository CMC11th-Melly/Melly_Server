package cmc.mellyserver.user.domain;

import cmc.mellyserver.common.enums.RecommendActivity;
import cmc.mellyserver.common.enums.RecommendGroup;
import cmc.mellyserver.common.enums.RecommendPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
