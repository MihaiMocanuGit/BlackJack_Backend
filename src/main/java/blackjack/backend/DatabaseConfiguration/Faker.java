package blackjack.backend.DatabaseConfiguration;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Faker {

    private String usernameFilepath;
    private List<String> lines;
    private Random rand;
    final int CACHED_SIZE = 81475;
    public Faker(String usernameFilepath)
    {
        this.usernameFilepath = usernameFilepath;
    }

    public static class FakerTuple{
        public Player player;
        public List<GameSummary> summaries;
    }
    public boolean init()
    {
        try {
            rand = new Random();
            lines = Files.readAllLines(Paths.get(usernameFilepath));
            return true;
        }
        catch (Exception e)
        {

            return false;
        }
    }
    public Player getFakePlayer()
    {
        String username = lines.get(rand.nextInt(CACHED_SIZE));
        float level = rand.nextInt(200) * 1.213f + 1;
        float bank = rand.nextInt( (int)(level * 2.53f)) + 110;

        return new Player(username, bank, level);

    }

    public GameSummary getFakeGameSummary(Player player)
    {
        float lowerBound = -200.0f + player.getLevel() * 3.5f;
        float upperBound = 50.0f + player.getLevel() * 3.5f;

        float profit = rand.nextFloat(lowerBound, upperBound);
        int handsPlayed = rand.nextInt(100);

        float experienceGained = (profit > 0 ? profit * 0.01f : 0) +  handsPlayed * 0.01f;

        return new GameSummary(player, profit, experienceGained, handsPlayed);
    }

    public List<GameSummary> getFakeGameSummaries(Player player, int noSummaries)
    {
        ArrayList<GameSummary> summaries = new ArrayList<>();
        for (int i = 0; i < noSummaries; i++) {
            summaries.add(getFakeGameSummary(player));
        }

        return summaries;
    }

    public FakerTuple getFakePlayerWithSummaries(int noSummaries)
    {
        Player player = getFakePlayer();
        FakerTuple result = new FakerTuple();

        result.player = player;
        result.summaries = getFakeGameSummaries(player, noSummaries);

        return result;
    }

    public FakerTuple getFakePlayerWithSummaries()
    {
        Player player = getFakePlayer();
        FakerTuple result = new FakerTuple();

        result.player = player;
        result.summaries = getFakeGameSummaries(player, rand.nextInt(25));

        return result;
    }
}
