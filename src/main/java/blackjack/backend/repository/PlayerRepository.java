package blackjack.backend.repository;

import blackjack.backend.domain.Player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long>{
}
