package cmc.mellyserver.dbcore.user;

import cmc.mellyserver.dbcore.user.enums.RecommendActivity;
import cmc.mellyserver.dbcore.user.enums.RecommendGroup;
import cmc.mellyserver.dbcore.user.enums.RecommendPlace;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private RecommendPlace recommendPlace;

    @Enumerated(EnumType.STRING)
    private RecommendActivity recommendActivity;

}
