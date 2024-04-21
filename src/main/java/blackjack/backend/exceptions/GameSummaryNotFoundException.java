package blackjack.backend.exceptions;

public class GameSummaryNotFoundException extends RuntimeException {

    public GameSummaryNotFoundException(String id) {
        super("Could not find gameSummary " + id);
    }
}