package blackjack.backend.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import blackjack.backend.domain.Player;
import blackjack.backend.controller.PrimaryController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {

    @Override
    public EntityModel<Player> toModel(Player player) {

        return EntityModel.of(player, //
                linkTo(methodOn(PrimaryController.class).one(player.getUid())).withSelfRel(),
                linkTo(methodOn(PrimaryController.class).all()).withRel("player"));
    }
}