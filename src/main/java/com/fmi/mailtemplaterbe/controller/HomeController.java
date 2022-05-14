package com.fmi.mailtemplaterbe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping(value = "/")
    public ResponseEntity<String> getGreetings() {
        return ResponseEntity.ok("Greetings from Email Templater application!");
    }
}
