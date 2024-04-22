package blackjack.backend.service;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import blackjack.backend.repository.GameSummaryRepository;
import blackjack.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CascadingService implements CascadingSummaryI {
    //@Autowired
    private PlayerRepository players;

    //@Autowired
    private GameSummaryRepository summaries;


    public CascadingService(PlayerRepository players, GameSummaryRepository summaries) {
        this.players = players;
        this.summaries = summaries;
    }

    public Player addPlayer(Player player) {return  players.save(player);}
    public GameSummary addSummary(GameSummary summary) {return  summaries.save(summary);}

    public Optional<Player> getPlayer(String uid) {return players.findById(uid);}

    public boolean playerExistsById(String uid) {return players.existsById(uid);}
    public Optional<GameSummary> getSummary(String uid) {return summaries.findById(uid);}
    public boolean summaryExistsById(String uid) {return summaries.existsById(uid);}

    public Iterable<Player> getPlayers() {return players.findAll();}
    public void removeAllPlayers() {players.deleteAll();}
    public void removeAllPSummaries() {summaries.deleteAll();}

    public long countPlayers() {return  players.count();}
    public long countGameSummaries() {return summaries.count();}

    public Iterable<GameSummary> getGameSummaries() {return summaries.findAll();}
    public boolean updatePlayer(String uid, Player player) {

        Optional<Player> oldPlayer = players.findById(uid);
        if (oldPlayer.isPresent())
        {
            for (GameSummary summary : oldPlayer.get().getGames()) {
                //if the new Player's game history does not already exist in the old player's history, delete it
                if (player.getGames().stream().noneMatch(summary1 -> Objects.equals(summary1.getUid(), summary.getUid())))
                    summaries.deleteById(summary.getUid());
                // else

            }

            this.addPlayer(player);
            return true;
        }

        return false;
    }
    public boolean updateSummary(String uid, GameSummary newSummary) {
        Optional<GameSummary> oldSummary = summaries.findById(uid);

        if (oldSummary.isPresent()) {

            //delete the summary from the old player's game history list
           summaries.findGameSummariesByPlayer_Uid(oldSummary.get().getPlayer().getUid()).stream()
                        .filter(s -> Objects.equals(s.getUid(), uid))
                        .forEach(summary1 -> summaries.deleteById(summary1.getUid()));


            //update the data
            oldSummary.get().setPlayer(newSummary.getPlayer());
            oldSummary.get().setProfit(newSummary.getProfit());
            oldSummary.get().setExperienceGained(newSummary.getExperienceGained());
            oldSummary.get().setHandsPlayed(newSummary.getHandsPlayed());

            //add the summary to the new player's game history
            Optional<Player> player = players.findById(newSummary.getPlayer().getUid());
            if (player.isPresent()) {
                player.get().getGames().add(oldSummary.get());


                return true;
            }
            else {
                return false;
            }
        }

        return false;
    }


    public void deletePlayer(String uid) {
        for (GameSummary summary : summaries.findGameSummariesByPlayer_Uid(uid))
            summaries.deleteById(summary.getUid());
        players.deleteById(uid);
    }
    public void deleteSummary(String uid) {
        Optional<GameSummary> summary = summaries.findById(uid);

        //delete the summary from the player's game history list
        if (summary.isPresent())
            summaries.findGameSummariesByPlayer_Uid(summary.get().getPlayer().getUid()).removeIf(s -> Objects.equals(s.getUid(), uid));

        summaries.deleteById(uid);
    }

}
