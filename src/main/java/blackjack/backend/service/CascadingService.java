package blackjack.backend.service;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import blackjack.backend.repository.GameSummaryRepository;
import blackjack.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CascadingService {
    @Autowired
    private PlayerRepository players;

    @Autowired
    private GameSummaryRepository summaries;

    public Player addPlayer(Player player) {return  players.save(player);}
    public GameSummary addSummary(GameSummary summary) {return  summaries.save(summary);}

    public Optional<Player> getPlayer(String uid) {return players.findById(uid);}
    public Optional<GameSummary> getSummary(String uid) {return summaries.findById(uid);}
    public Player updatePlayer(String uid, Player player) {}
    public GameSummary updateSummary(String uid, GameSummary summary) {}


    public void deletePlayer(String uid) { players.deleteById(uid);}
    public void deleteSummary(String uid) {summaries.deleteById(uid);}

}
