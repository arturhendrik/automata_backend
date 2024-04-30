package com.example.automata.application.controller;

import com.example.automata.application.dto.AutomataRequestBody;
import dk.brics.automaton.Automaton;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.example.automata.application.utils.ControllerUtils.*;

@RestController
@RequestMapping("/automata")
public class TestController {

    @PostMapping("test/{exerciseNumber}")
    public ResponseEntity<?> testEquivalence(@RequestBody AutomataRequestBody requestBody, @PathVariable int exerciseNumber) {
        Automaton expectedAutomaton = loadJFlapAutomaton(Path.of("src/test/test" + exerciseNumber + ".jff"));
        Automaton actualAutomaton = convertToAutomaton(requestBody, false);

        List<String> returnMessage = new ArrayList<>();

        Automaton expectedMinusActual = expectedAutomaton.minus(actualAutomaton);
        if (!expectedMinusActual.isEmpty()) {
            returnMessage.add(String.format("should_accept:'%s'", expectedMinusActual.getShortestExample(true)));
        }

        Automaton actualMinusExpected = actualAutomaton.minus(expectedAutomaton);
        if (!actualMinusExpected.isEmpty()) {
            returnMessage.add(String.format("should_not_accept:'%s'", actualMinusExpected.getShortestExample(true)));
        }
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }
}
