package cmc.mellyserver.mellyapi.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {

	@GetMapping("/")
	public String test() {
		return "check";
	}
}
