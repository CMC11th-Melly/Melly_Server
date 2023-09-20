package cmc.mellyserver.mellyapi.common.healthcheck;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PushController {

    @GetMapping("/push")
    public String st() {
        return "push";
    }
}
