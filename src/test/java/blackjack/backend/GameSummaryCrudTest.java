package blackjack.backend;

import blackjack.backend.domain.GameSummary;
import blackjack.backend.domain.Player;
import blackjack.backend.repository.GameSummaryRepository;
import blackjack.backend.repository.PlayerRepository;

import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@DataMongoTest
@AutoConfigureDataMongo
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameSummaryCrudTest {


    @Autowired
    GameSummaryRepository summaries;
    @Autowired
    PlayerRepository players;



    @BeforeAll
    public void Init()
    {
        summaries.deleteAll();
        players.deleteAll();
    }

    @Test
    @Order(1)
    public void basicAdd() {

        for (int i = 0; i < 50; i++) {
            Player player = new Player(Integer.toString(i), i, i);

            player = players.save(player);
            GameSummary summary;
            for (int j = 0; j < 20; j++) {
                summary = new GameSummary(player, i * 20 + j, 1000 + i * 20 + j, 2000 + i * 20 + j);

                summary = summaries.save(summary);

                assertEquals(summaries.count(), i * 20 + j + 1);
                assertTrue(summaries.existsById(summary.getUid()));
            }
            List<GameSummary> test = summaries.findAll().stream().toList();
            players.delete(player);
            test = summaries.findAll().stream().toList();
            assertEquals(0, summaries.count());
        }
    }

}
