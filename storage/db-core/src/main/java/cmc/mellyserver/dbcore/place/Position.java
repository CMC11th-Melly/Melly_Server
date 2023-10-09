package cmc.mellyserver.dbcore.place;

import jakarta.persistence.Embeddable;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class Position {
    private Double lat;
    private Double lng;
}
