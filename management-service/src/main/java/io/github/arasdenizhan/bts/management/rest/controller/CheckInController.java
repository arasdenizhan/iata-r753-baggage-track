package io.github.arasdenizhan.bts.management.rest.controller;

import io.github.arasdenizhan.bts.management.dto.BaggageInfoDto;
import io.github.arasdenizhan.bts.management.rest.request.CheckInRequest;
import io.github.arasdenizhan.bts.management.service.baggage.BaggageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check-in")
@RequiredArgsConstructor
public class CheckInController {
    private final BaggageService service;

    @PostMapping
    ResponseEntity<BaggageInfoDto> checkIn(@RequestBody CheckInRequest request){
        return ResponseEntity.ok(service.checkIn(request));
    }
}
