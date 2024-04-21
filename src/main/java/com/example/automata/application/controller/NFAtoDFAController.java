package com.example.automata.application.controller;

import com.example.automata.application.dto.AutomataRequestBody;
import com.example.automata.application.dto.Edge;
import com.example.automata.application.dto.Node;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.*;

import static com.example.automata.application.utils.ControllerUtils.convertToAutomaton;

@RestController
@RequestMapping("/automata")
public class NFAtoDFAController {

    @PostMapping("nfa-to-dfa")
    public ResponseEntity<?> convertNFAtoDFA(@RequestBody AutomataRequestBody requestBody) {

        Automaton automaton = convertToAutomaton(requestBody);
        automaton.determinize();
        List<Edge> edges = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        AutomataRequestBody dataForFrontend = new AutomataRequestBody();
        int edgeId = 0;
        int x = 0;
        int y = 0;
        State initialState = automaton.getInitialState();

        Set<State> idk = automaton.getStates();
        for (State a : idk) {
            Node node = new Node();
            String group = "Normal";
            if (a.equals(initialState) && a.isAccept()) {
                group = "Initial_Final";
            }
            else if (a.equals(initialState)) {
                group = "Initial";
            }
            else if (a.isAccept()) {
                group = "Final";
            }
            node.setGroup(group);
            String[] split = a.toString().split(" ");
            int id = Integer.parseInt(split[1]);
            Set<Transition> transitions = a.getTransitions();
            node.setId(id);
            node.setLabel("q"+id);
            node.setY(y);
            node.setX(x);
            x += 100;
            y += 100;
            nodes.add(node);
            for (Transition t : transitions) {
                String[] splitTransition = t.toString().split(" ");
                String label = splitTransition[0];
                Edge edge = new Edge();
                edge.setFrom(id);
                edge.setTo(Integer.parseInt(splitTransition[2]));
                if (label.contains("-")) {
                    char startChar = label.charAt(0);
                    char endChar = label.charAt(2);

                    for (char ch = startChar; ch <= endChar; ch++) {
                        Edge newEdge = new Edge();
                        newEdge.setFrom(edge.getFrom());
                        newEdge.setTo(edge.getTo());
                        newEdge.setId(String.valueOf(edgeId));
                        newEdge.setLabel(String.valueOf(ch));
                        edges.add(newEdge);
                        edgeId++;
                    }
                } else {
                    edge.setId(String.valueOf(edgeId));
                    edge.setLabel(splitTransition[0]);
                    edges.add(edge);
                    edgeId++;
                }
            }
        }

        Map<String, Edge> mergedMap = new HashMap<>();
        for (Edge edge : edges) {
            String key = edge.getFrom() + "-" + edge.getTo();
            Edge mergedEdge = mergedMap.get(key);
            if (mergedEdge == null) {
                mergedMap.put(key, edge);
            } else {
                mergedEdge.setLabel(mergedEdge.getLabel() + "; " + edge.getLabel());
            }
        }
        List<Edge> newEdges =  new ArrayList<>(mergedMap.values());

        dataForFrontend.setEdges(newEdges);
        dataForFrontend.setNodes(nodes);

        return new ResponseEntity<>(dataForFrontend, HttpStatus.OK);
    }
}
