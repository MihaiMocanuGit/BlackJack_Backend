package blackjack.backend.repository;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSummaryRepository extends MongoRepository<GameSummary, String> {
    public List<GameSummary> findGameSummariesByPlayer(Player player);
    public List<GameSummary> findGameSummariesByPlayer_Uid(String playerUid);

}
