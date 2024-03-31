package blackjack.backend.exceptions;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}