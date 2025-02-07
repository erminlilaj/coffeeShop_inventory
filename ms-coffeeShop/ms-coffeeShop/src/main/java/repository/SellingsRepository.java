package repository;

import entity.Sellings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellingsRepository extends JpaRepository<Sellings, Long> {
}
