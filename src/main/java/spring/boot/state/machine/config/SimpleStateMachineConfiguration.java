package spring.boot.state.machine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.RepositoryStateMachineModelFactory;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.data.jpa.JpaRepositoryTransition;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.stereotype.Component;
import spring.boot.state.machine.listener.StateMachineListener;
import spring.boot.state.machine.repository.MyStateRepository;
import spring.boot.state.machine.repository.MyTransitionRepository;

@Component
@Configuration
@EnableStateMachineFactory
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<String, String> {

    private StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;
    private MyStateRepository myStateRepository;
    private MyTransitionRepository myTransitionRepository;

    @Autowired
    public SimpleStateMachineConfiguration(StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister,
                                           MyStateRepository myStateRepository,
                                           MyTransitionRepository myTransitionRepository) {

        this.stateMachineRuntimePersister = stateMachineRuntimePersister;
        this.myStateRepository = myStateRepository;
        this.myTransitionRepository = myTransitionRepository;

        initialize();
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
                .withConfiguration()
                .listener(stateMachineListener())
                .autoStartup(true);
        config
                .withPersistence()
                .runtimePersister(stateMachineRuntimePersister);
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial("SI")
                .end("SF")
                .state("S1")
                .state("S2")
                .state("S3")
                .state("S4");
    }

    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model
                .withModel()
                .factory(modelFactory());
    }

    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new RepositoryStateMachineModelFactory(myStateRepository, myTransitionRepository);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source("SI")
                .target("S1")
                .event("E1").and()
                .withExternal()
                .source("S1")
                .target("S2")
                .event("E2").and()
                .withExternal()
                .source("S2")
                .target("S3")
                .event("E3")
                .and()
                .withExternal()
                .source("S3")
                .target("S4")
                .event("E4").and()
                .withExternal()
                .source("S4")
                .target("SF")
                .event("END");
    }

    @Bean
    public StateMachineListener stateMachineListener() {
        return new StateMachineListener();
    }

    private void initialize() {
        myStateRepository.save(new JpaRepositoryState("datajpapersist1","SI", true));
        myStateRepository.save(new JpaRepositoryState("datajpapersist1","S1", false));
        /*myStateRepository.save(new JpaRepositoryState("S2"));
        myStateRepository.save(new JpaRepositoryState("S3"));
        myStateRepository.save(new JpaRepositoryState("S4"));
        myStateRepository.save(new JpaRepositoryState("SF"));*/

        myTransitionRepository.save(new JpaRepositoryTransition("datajpapersist1", myStateRepository.findByState("SI"), myStateRepository.findByState("S1"), "E1"));
        /*myTransitionRepository.save(new JpaRepositoryTransition(myStateRepository.findByState("S1"), myStateRepository.findByState("S2"), "E2"));
        myTransitionRepository.save(new JpaRepositoryTransition(myStateRepository.findByState("S2"), myStateRepository.findByState("S3"), "E3"));
        myTransitionRepository.save(new JpaRepositoryTransition(myStateRepository.findByState("S3"), myStateRepository.findByState("S4"), "E4"));
        myTransitionRepository.save(new JpaRepositoryTransition(myStateRepository.findByState("S4"), myStateRepository.findByState("SF"), "END"));*/
    }

}