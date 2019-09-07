package spring.boot.state.machine.repository;

import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.jpa.JpaRepositoryState;
import org.springframework.stereotype.Repository;

@Repository
public interface MyStateRepository<S extends RepositoryState> extends StateRepository<JpaRepositoryState> {
    JpaRepositoryState findByState(String state);
}
