package blackjack.backend.repository;

import blackjack.backend.domain.Player;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlayerRepository extends MongoRepository<Player, String>{
    public List<Player> findByUsername(String username);
    public List<Player> findByLevelBetween(float leve1, float level2);

}
