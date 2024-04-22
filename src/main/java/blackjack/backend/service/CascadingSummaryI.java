package blackjack.backend.service;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

//@Service
public interface CascadingSummaryI {
    Player addPlayer(Player player);
    GameSummary addSummary(GameSummary summary);

    Optional<Player> getPlayer(String uid);

    boolean playerExistsById(String uid);
    Optional<GameSummary> getSummary(String uid);
    boolean summaryExistsById(String uid);

    Iterable<Player> getPlayers();
    void removeAllPlayers();
    void removeAllPSummaries();

    long countPlayers();
    long countGameSummaries();

    Iterable<GameSummary> getGameSummaries();
    boolean updatePlayer(String uid, Player player);
    boolean updateSummary(String uid, GameSummary newSummary);


    void deletePlayer(String uid);
    void deleteSummary(String uid);
}
