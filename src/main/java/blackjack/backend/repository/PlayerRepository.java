package blackjack.backend.repository;

import blackjack.backend.domain.Player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String>{
    public List<Player> findByUsername(String username);
    public List<Player> findByLevelBetween(float leve1, float level2);

}
