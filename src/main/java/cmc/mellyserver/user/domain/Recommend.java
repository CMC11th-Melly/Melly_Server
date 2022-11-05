package cmc.mellyserver.user.domain;

import cmc.mellyserver.user.domain.enums.RecommendActivity;
import cmc.mellyserver.user.domain.enums.RecommendGroup;
import cmc.mellyserver.user.domain.enums.RecommendPlace;
import lombok.AccessLevel;
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

    private String recommendPlace;

    @Enumerated(EnumType.STRING)
    private RecommendActivity recommendActivity;

}
