package io.github.arasdenizhan.bts.management.rest.controller;

import io.github.arasdenizhan.bts.management.model.websocket.BaggageUpdate;
import io.github.arasdenizhan.bts.management.service.baggage.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/baggage")
@RequiredArgsConstructor
public class BaggageController {
    private final BaggageService service;

    @GetMapping
    ResponseEntity<List<BaggageUpdate>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("{baggageTag}")
    ResponseEntity<Void> updateStatus(@PathVariable("baggageTag") String baggageTag){
        service.update(baggageTag);
        return ResponseEntity.ok().build();
    }
}
