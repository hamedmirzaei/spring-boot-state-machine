package spring.boot.state.machine.repository;

import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.statemachine.data.jpa.JpaRepositoryTransition;
import org.springframework.stereotype.Repository;

@Repository
public interface MyTransitionRepository<T extends RepositoryTransition> extends TransitionRepository<JpaRepositoryTransition> {
    JpaRepositoryTransition findByEvent(String event);
}
