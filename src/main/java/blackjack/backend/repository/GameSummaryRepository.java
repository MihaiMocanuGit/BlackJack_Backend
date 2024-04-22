package blackjack.backend.repository;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameSummaryRepository extends MongoRepository<GameSummary, String> {
    public List<GameSummary> findGameSummariesByPlayer(Player player);
    public List<GameSummary> findGameSummariesByPlayer_Uid(String playerUid);

}
