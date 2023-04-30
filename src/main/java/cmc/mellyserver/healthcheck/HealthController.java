package cmc.mellyserver.healthcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {


    @GetMapping("/")
    public String test()
    {
        return "check";
    }
}
