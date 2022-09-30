package cmc.mellyserver.user.domain;

import cmc.mellyserver.user.domain.enums.RecommendPeople;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Recommend {

    @Enumerated(EnumType.STRING)
    private RecommendPeople recommandPeople;

}
