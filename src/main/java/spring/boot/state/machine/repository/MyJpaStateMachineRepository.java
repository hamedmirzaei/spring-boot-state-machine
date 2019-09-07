package spring.boot.state.machine.repository;

import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyJpaStateMachineRepository extends JpaStateMachineRepository {
}
