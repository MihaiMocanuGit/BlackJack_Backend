package blackjack.backend.configuration;

import blackjack.backend.domain.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Faker {

    private String usernameFilepath;
    private List<String> lines;
    private Random rand;
    final int CACHED_SIZE = 81475;
    public Faker(String usernameFilepath)
    {
        this.usernameFilepath = usernameFilepath;
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
}
