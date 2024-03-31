package blackjack.backend.service;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import blackjack.backend.assemblers.PlayerModelAssembler;
import blackjack.backend.domain.Player;
import blackjack.backend.exceptions.PlayerNotFoundException;
import blackjack.backend.repository.PlayerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class PlayerController {
    private final PlayerRepository repository;
    private final PlayerModelAssembler assembler;

    PlayerController(PlayerRepository repository, PlayerModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping("/players")
    ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {

        EntityModel<Player> entityModel = assembler.toModel(repository.save(newPlayer));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/players")
    public CollectionModel<EntityModel<Player>> all() {
        List<EntityModel<Player>> players = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());

        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());
    }

    @GetMapping("/players/{pageNo}-{pageSize}")
    public CollectionModel<EntityModel<Player>> page(@PathVariable Long pageNo, @PathVariable Long pageSize) {
        long startPosition = pageNo * pageSize;
        long endPosition = startPosition + pageSize;

        Long[] playersIds = (Long[]) repository.findAll().stream() //
                                .map(Player::getUid)
                                .toArray();
        List<EntityModel<Player>> players = IntStream
                .range((int) startPosition, (int) endPosition)
                .mapToObj(i -> assembler.toModel(repository.findById(playersIds[i]).orElseThrow(()
                                                        -> new PlayerNotFoundException(playersIds[i]))))
                .toList();
        return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());

    }

    // Single item
    @GetMapping("/players/{id}")
    public EntityModel<Player> one(@PathVariable Long id) {

        Player player = repository.findById(id) //
                .orElseThrow(() -> new PlayerNotFoundException(id));

        return assembler.toModel(player);
    }


    @PutMapping("/players/{id}")
    ResponseEntity<?> replacePlayers(@RequestBody Player newPlayer, @PathVariable Long id) {

        Player updatedPlayer = repository.findById(id) //
                .map(player -> {
                    player.setUsername(newPlayer.getUsername());
                    player.setBank(newPlayer.getBank());
                    player.setLevel(newPlayer.getLevel());
                    return repository.save(player);
                }) //
                .orElseGet(() -> {
                    newPlayer.setUid(id);
                    return repository.save(newPlayer);
                });

        EntityModel<Player> entityModel = assembler.toModel(updatedPlayer);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/players/{id}")
    ResponseEntity<?> deletePlayer(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
