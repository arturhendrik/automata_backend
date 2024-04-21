package com.example.automata.application.controller;

import com.example.automata.application.dto.AutomataRequestBody;
import dk.brics.automaton.Automaton;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.automata.application.utils.ControllerUtils.*;

@RestController
@RequestMapping("/automata")
public class MinimizeDFAController {

    @PostMapping("minimize-dfa")
    public ResponseEntity<?> minimizeDFA(@RequestBody AutomataRequestBody requestBody) {
        Automaton automaton = convertToAutomaton(requestBody, true);
        automaton.minimize();
        return getResponseEntity(automaton, true);
    }
}