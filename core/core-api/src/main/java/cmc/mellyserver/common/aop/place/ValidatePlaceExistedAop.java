package cmc.mellyserver.common.aop.place;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatePlaceExistedAop {

    private final PlaceRepository placeRepository;

    @Around("@annotation(cmc.mellyserver.common.aop.place.ValidatePlaceExisted)")
    public Object createPlace(final ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof Position) {
                Optional<Place> place = placeRepository.findByPosition((Position) arg);

                if (place.isEmpty()) {
                    Place savePlace = createMemoryRequestDto.toPlace();
                    placeRepository.save(savePlace);
                }

                return joinPoint.proceed();
            }
        }


        return null;
    }
}

