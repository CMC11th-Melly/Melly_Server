package cmc.mellyserver.place.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode
public class Position {

    private Double lat;
    private Double lng;

}
