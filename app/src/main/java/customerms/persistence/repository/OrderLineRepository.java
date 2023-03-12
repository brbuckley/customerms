package customerms.persistence.repository;

import customerms.persistence.domain.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** OrderLine repository. */
@Repository
public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Integer> {}
