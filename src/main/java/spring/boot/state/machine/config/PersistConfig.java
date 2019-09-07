package spring.boot.state.machine.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import spring.boot.state.machine.repository.MyJpaStateMachineRepository;

@Configuration
public class PersistConfig {

    private MyJpaStateMachineRepository myJpaStateMachineRepository;

    @Autowired
    public PersistConfig(MyJpaStateMachineRepository myJpaStateMachineRepository) {
        this.myJpaStateMachineRepository = myJpaStateMachineRepository;
    }

    @Bean
    public StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister() {
        return new JpaPersistingStateMachineInterceptor<>(myJpaStateMachineRepository);
    }
}
