package spring.boot.state.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import spring.boot.state.machine.listener.StateMachineListener;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration
        extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    private StateMachine<String, String> stateMachine;

    @Bean
    public Action<String, String> initAction() {
        return ctx -> System.out.println("Init " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> entryAction() {
        return ctx -> System.out.println("Entry " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> executeAction() {
        return ctx -> {
            /*if ("S3".equals(ctx.getTarget().getId()))
                throw new NullPointerException("Just for test");*/
            System.out.println("Execute " + ctx.getTarget().getId());
        };
    }

    @Bean
    public Action<String, String> exitAction() {
        return ctx -> System.out.println("Exit " + ctx.getSource().getId() + " -> " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> errorAction() {
        return ctx -> System.out.println("Error " + ctx.getSource().getId() + " " + ctx.getException());
    }

    @Bean
    public StateMachineListener stateMachineEventListener() {
        StateMachineListener listener = new StateMachineListener();
        stateMachine.addStateListener(listener);
        return listener;
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {

        states.withStates()
                .initial("S2")
                .end("SF")
                .state("S1")
                .state("S2")
                .state("S3")
                .stateEntry("S4", entryAction())
                .stateDo("S4", executeAction())
                .stateExit("S4", exitAction())
                .states(new HashSet<String>(Arrays.asList("S1", "S2", "S3", "S4")));

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {

        transitions.withExternal()
                .source("SI").target("S1").event("E1").action(initAction()).and()
                .withExternal()
                .source("S1").target("S2").event("E2").and()
                .withExternal()
                .source("S2")
                .target("S3")
                .event("E3")
                .action(executeAction(), errorAction())
                .and()
                .withExternal()
                .source("S3").target("S4").event("E4").and()
                .withExternal()
                .source("S4").target("SF").event("end");
    }
}