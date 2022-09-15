package cmc.mellyserver.user.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;

// TODO : 특정 장소에 대한 정보!
@Embeddable
@Getter
public class VisitedPlace {

    private Position position;
    private int count;
    private String color;


    @Data
    static class Position
    {
        Double x;
        Double y;
    }

}
