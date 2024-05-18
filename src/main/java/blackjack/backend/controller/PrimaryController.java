package blackjack.backend.controller;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import blackjack.backend.assemblers.GameSummaryAssembler;
import blackjack.backend.assemblers.PlayerModelAssembler;
import blackjack.backend.configuration.Faker;
import blackjack.backend.domain.AdminUserDTO;
import blackjack.backend.domain.LoginDTO;
import blackjack.backend.domain.Player;
import blackjack.backend.exceptions.PlayerNotFoundException;
import blackjack.backend.repository.GameSummaryRepository;
import blackjack.backend.repository.PlayerRepository;
import blackjack.backend.response.LoginMessage;
import blackjack.backend.service.AdminUsersServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class PrimaryController {
    private final String origin = "http://localhost:3000";
    ///TODO use CascadingService
    private PlayerRepository players;
    private GameSummaryRepository summaries;
    private GameSummaryAssembler summaryAssembler;
    private final PlayerModelAssembler playerAssembler;



    @Autowired
    private AdminUsersServiceI adminUsersService;
    private boolean reversed;

    //public GameSummaryRepository TEMP_summaries() {return  sumarries;}
    //public PlayerRepository TEMP_players() {return  players;}


    private int sortRepoByLevelCondition(Player player1, Player player2)
    {
        if (player1.getLevel() == player2.getLevel())
            return 0;
        else if (player1.getLevel() < player2.getLevel() ^ reversed)
            return -1;
        else
            return 1;

    }
    private List<Player> repoSortToList()
    {
        return players.findAll().stream().sorted((p1, p2) ->
                {
                    if (p1.getLevel() == p2.getLevel())
                        return 0;
                    else if (p1.getLevel() < p2.getLevel() ^ reversed)
                        return -1;
                    else
                        return 1;
                }).toList();
    }


    PrimaryController(PlayerRepository players, PlayerModelAssembler playerAssembler, GameSummaryRepository summaries, GameSummaryAssembler summaryAssembler) {
        this.players = players;
        this.playerAssembler = playerAssembler;

        this.summaries = summaries;
        this.summaryAssembler = summaryAssembler;

        reversed = false;
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {

        EntityModel<Player> entityModel = playerAssembler.toModel(players.save(newPlayer));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @CrossOrigin(origins = origin)
    @PostMapping("/fakers")
    void fake() {
        Faker faker = new Faker("data/usernames.txt");
        faker.init();

        for (long i = 0; i < 100; i++)
        {
            players.save(faker.getFakePlayer());
        }
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/status")
    public int status() {

        return 1;
    }
    @CrossOrigin(origins = origin)
    @GetMapping("/players")
    public CollectionModel<EntityModel<Player>> all() {
        List<EntityModel<Player>> players = repoSortToList().stream() //
                .map(playerAssembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(players, linkTo(methodOn(PrimaryController.class).all()).withSelfRel());
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/players/{pageNo}/{pageSize}")
    public CollectionModel<EntityModel<Player>> page(@PathVariable("pageNo") Long pageNo, @PathVariable("pageSize") Long pageSize) {

        if (pageNo < 0)
            pageNo = 0L;
        long startPosition = pageNo * pageSize;
        long endPosition = startPosition + pageSize;


        Object[] playersIds =   repoSortToList().stream() //
                                .map(Player::getUid)
                                .toArray();

        if (endPosition > playersIds.length)
        {

            startPosition = (playersIds.length / pageSize) * pageSize;
            endPosition = Math.min(playersIds.length, startPosition + pageSize);

        }

        List<EntityModel<Player>> players = IntStream
                .range((int) startPosition, (int) endPosition)
                .mapToObj(i -> playerAssembler.toModel(this.players.findById((String) playersIds[i]).orElseThrow(()
                                                        -> new PlayerNotFoundException((String) playersIds[i]))))
                .toList();
        return CollectionModel.of(players, linkTo(methodOn(PrimaryController.class).all()).withSelfRel());

    }

    // Single item
    @CrossOrigin(origins = origin)
    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable String id) {

        Player player = players.findById(id) //
                .orElseThrow(() -> new PlayerNotFoundException(id));

        return playerAssembler.toModel(player);
    }

    @CrossOrigin(origins = origin)
    @GetMapping("/players/size")
    public long size() {
        return players.findAll().size();
    }

    ///TODO: Make it a PutMapping instead
    @CrossOrigin(origins = origin)
    @GetMapping("/players/sort/{reverse}")
    public CollectionModel<EntityModel<Player>> sortRepo(@PathVariable("reverse") String reverse)
    {
        this.reversed = reverse.equalsIgnoreCase("true") || reverse.equals("1");

        EntityModel<Boolean> entityModel = EntityModel.of(reversed);

        return this.all();
    }
    @CrossOrigin(origins = origin)
    @PutMapping("/players/{id}")
    ResponseEntity<?> replacePlayers(@RequestBody Player newPlayer, @PathVariable String id) {

        Player updatedPlayer = players.findById(id) //
                .map(player -> {
                    player.setUsername(newPlayer.getUsername());
                    player.setBank(newPlayer.getBank());
                    player.setLevel(newPlayer.getLevel());
                    return players.save(player);
                }) //
                .orElseGet(() -> {
                    newPlayer.setUid(id);
                    return players.save(newPlayer);
                });

        EntityModel<Player> entityModel = playerAssembler.toModel(updatedPlayer);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @CrossOrigin(origins = origin)
    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable String id) {

        players.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    ///https://www.tutussfunny.com/login-registration-form-using-spring-boot-react/
    ///https://www.tutussfunny.com/login-and-registration-rest-api-using-spring-boot-mysql/
    ///https://www.youtube.com/watch?v=ug3K7h0LbJ0
    ///https://www.youtube.com/watch?v=4w2ZYxiIvkc
    /// actually, try https://medium.com/@tericcabrel/implement-jwt-authentication-in-a-spring-boot-3-application-5839e4fd8fac

    @PostMapping(path = "/save")
    public boolean saveEmployee(@RequestBody AdminUserDTO adminUserDTO)
    {
        return adminUsersService.addAdminUser(adminUserDTO);
    }
    @PostMapping(path = "/login")
    //TODO: create ResponseEntity for return
    public ResponseEntity<?> loginEmployee(@RequestBody LoginDTO loginDTO)
    {
        LoginMessage loginResponse = adminUsersService.loginAdminUser(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}
