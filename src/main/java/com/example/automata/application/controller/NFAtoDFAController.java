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
public class NFAtoDFAController {

    @PostMapping("nfa-to-dfa")
    public ResponseEntity<?> convertNFAtoDFA(@RequestBody AutomataRequestBody requestBody) {

        Automaton automaton = convertToAutomaton(requestBody, false);
        automaton.determinize();
        return getResponseEntity(automaton, false);
    }
}
