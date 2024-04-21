package blackjack.backend;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import blackjack.backend.controller.PlayerController;
import blackjack.backend.domain.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class BlackJackBackendApplicationTests {

    @LocalServerPort
    int randomServerPort;
    long actualSize;
    ArrayList<String> oldUids = new ArrayList<>();
    @Autowired
    PlayerController controller;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void runTests() throws Exception {
        contextLoads();
        allBasic();
        controller.all().forEach(model -> oldUids.add(Objects.requireNonNull(model.getContent()).getUid()));
        newPlayer();
        status();
        all();
        page();
        one();
        sortRepo();
        replacePlayers();
        deletePlayer();
    }
    @Test
    void contextLoads() {

        assertThat(controller).isNotNull();

    }

    @Test
    void newPlayer() throws Exception {
        //ArrayList<Player> players = new ArrayList<>();
        long oldSize = controller.size();
        this.actualSize = oldSize;
        for(int i = 0; i < 100; i++) {
            Player player = new Player("Johnny" + Integer.toString(i), i * 10, i * 1.125f);

            final String baseUrl = "http://localhost:" + randomServerPort + "/players";
            URI uri = new URI(baseUrl);

            HttpHeaders headers = new HttpHeaders();
            // headers.set("Accept-Post", "true");

            HttpEntity<Player> request = new HttpEntity<>(player, headers);

            ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
            String id = result.getBody().substring("{\"uid\":\"".length(), result.getBody().indexOf( "\",\"username\":\"Johnny"));
            //Verify request succeed
            assertEquals(201, result.getStatusCodeValue());
            String body = "{\"uid\":\"" + id +
                    "\",\"username\":\"Johnny" + Integer.toString(i) +
                    "\",\"bank\":" + i * 10.0f +
                    ",\"level\":" + i * 1.125f +
                    ",\"_links\":{\"self\":{\"href\":\"http://localhost:8080/players/" + id +
                    "\"},\"player\":{\"href\":\"http://localhost:8080/players\"}}}";
            assertEquals( body, result.getBody());
        }
        assertEquals(100, controller.size() - oldSize);
        this.actualSize = 100 + oldSize;
    }
    @Test
    void status() {
        assertEquals(controller.status(), 1);
    }

    @Test
    void allBasic()
    {
        assertEquals(controller.all().getContent().size(), controller.size());
    }

    @Test
    void all() {
        CollectionModel<EntityModel<Player>> response = controller.all();
        Collection<EntityModel<Player>> list = response.getContent();

        for (int i = 0; i < 100; i++)
        {
            Player player = new Player("Johnny" + Integer.toString(i), i * 10, i * 1.125f);
            assertTrue(list.stream().map(EntityModel::getContent).anyMatch(x -> x.getLevel() == player.getLevel() &&
                                                    Objects.equals(x.getUsername(), player.getUsername()) &&
                                                     x.getBank() == player.getBank()));
        }
        assertEquals(actualSize, list.size());
    }

    @Test
    void page() {
        for (long i = 1; i <= actualSize; i++)
        {
            CollectionModel<EntityModel<Player>> response = controller.page(0L, i);
            Collection<EntityModel<Player>> list = response.getContent();
            assertEquals(i, list.size());

            for (long j = 0; j < actualSize / i; j++)
            {
                CollectionModel<EntityModel<Player>> responseInner = controller.page(j, i);
                Collection<EntityModel<Player>> listInner = response.getContent();
                assertEquals(i, listInner.size());
            }
        }



    }

    @Test
    void one() {
        for (int i = 0; i < oldUids.size(); i++) {
           String uid = Objects.requireNonNull(controller.one(oldUids.get(i)).getContent()).getUid();
           assertEquals(oldUids.get(i), uid);
        }
    }


    @Test
    void sortRepo() {
        CollectionModel<EntityModel<Player>> response = controller.sortRepo(Boolean.toString(false));
        Collection<EntityModel<Player>> list = response.getContent();
        Object[] players = list.stream().map(EntityModel::getContent).toArray();
        for (int i = 0; i < players.length - 1; i++)
        {
            Player player1 = (Player) players[i];
            Player player2 = (Player) players[i + 1];

            assertTrue(player1.getLevel() <= player2.getLevel());
        }

    }

    @Test
    void replacePlayers() throws URISyntaxException {
        for (int i = 0; i < oldUids.size(); i++) {
            String uid = Objects.requireNonNull(controller.one(oldUids.get(i)).getContent()).getUid();
            Player player = new Player("Johnny" + Integer.toString(-i), i * 10, i * 1.125f);

            final String baseUrl = "http://localhost:" + randomServerPort + "/players/" + uid;


            ResponseEntity<String> result = this.restTemplate.exchange(baseUrl, HttpMethod.PUT, new HttpEntity<>(player), String.class);
            String id = result.getBody().substring("{\"uid\":\"".length(), result.getBody().indexOf( "\",\"username\":"));
            assertEquals(201, result.getStatusCodeValue());
            String body = "{\"uid\":\"" + id +
                    "\",\"username\":\"Johnny" + -i +
                    "\",\"bank\":" + i * 10.0f +
                    ",\"level\":" + i * 1.125f +
                    ",\"_links\":{\"self\":{\"href\":\"http://localhost:8080/players/" + id +
                    "\"},\"player\":{\"href\":\"http://localhost:8080/players\"}}}";
            assertEquals( body, result.getBody());
        }
    }

    @Test
    void deletePlayer() {
         List<Player> players =  controller.all().getContent().stream().map(EntityModel::getContent).toList();
         players.forEach(x -> {
             final String baseUrl = "http://localhost:" + randomServerPort + "/players/" + x.getUid();


             ResponseEntity<String> result = this.restTemplate.exchange(baseUrl, HttpMethod.DELETE, new HttpEntity<>(x.getUid()), String.class);

             assertEquals(actualSize - 1, controller.size());
             actualSize--;
         });

         assertEquals(0, controller.size());
    }

}
