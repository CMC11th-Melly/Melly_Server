package cmc.mellyserver.memory.presentation;

import cmc.mellyserver.memory.application.MemoryService;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.place.domain.PlaceInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping("/memory")
    public ResponseEntity<String> save(@AuthenticationPrincipal User user, @RequestBody PlaceInfoRequest placeInfoRequest)
    {
        Memory memory = memoryService.createMemory(user.getUsername(),placeInfoRequest);
        return ResponseEntity.ok(memory.getContent());
    }
}
