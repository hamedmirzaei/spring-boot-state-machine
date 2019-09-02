package spring.boot.state.machine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class StateMachineController {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @GetMapping("/start")
    public String startStateMachine() {
        stateMachine.start();
        return "Start State Machine";
    }

    @GetMapping("/stop")
    public String endStateMachine() {
        stateMachine.stop();
        return "Stop State Machine";
    }

    @GetMapping("/states")
    public List<String> getStates() {
        List<String> result = new ArrayList<>();
        Collection<State<String, String>> states = stateMachine.getStates();
        states.stream().forEach(s -> result.add(s.getId()));
        return result;
    }

    @GetMapping("/states/current")
    public String getCurrentState() {
        return stateMachine.getState().getId();
    }


    @GetMapping("/event/start/{event}")
    public String startEvent(@PathVariable("event") String event) {
        stateMachine.sendEvent(event);
        return "Event " + event + " Started";
    }


}
