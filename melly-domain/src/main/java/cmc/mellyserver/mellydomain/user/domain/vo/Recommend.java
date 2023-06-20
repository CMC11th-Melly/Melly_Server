package cmc.mellyserver.mellydomain.user.domain.vo;

import cmc.mellyserver.mellydomain.common.enums.RecommendActivity;
import cmc.mellyserver.mellydomain.common.enums.RecommendGroup;
import cmc.mellyserver.mellydomain.common.enums.RecommendPlace;
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

    @Enumerated(EnumType.STRING)
    private RecommendPlace recommendPlace;

    @Enumerated(EnumType.STRING)
    private RecommendActivity recommendActivity;

}
