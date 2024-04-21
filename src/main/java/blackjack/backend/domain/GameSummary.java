package blackjack.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

public class GameSummary {
    private @Id String uid;

    //https://spring.io/blog/2021/11/29/spring-data-mongodb-relation-modelling
    @DocumentReference(lazy=true)
    private Player player;
    private float profit;
    private float experienceGained;
    private int handsPlayed;

    public GameSummary(Player player, float profit, float experienceGained, int handsPlayed) {
        this.player = player;
        this.profit = profit;
        this.experienceGained = experienceGained;
        this.handsPlayed = handsPlayed;
    }

    public GameSummary()
    {

    }

    @Override
    public String toString() {
        return "GameSummary{" +
                "uid='" + uid + '\'' +
                ", player=" + player +
                ", profit=" + profit +
                ", experienceGained=" + experienceGained +
                ", handsPlayed=" + handsPlayed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSummary that = (GameSummary) o;

        if (Float.compare(profit, that.profit) != 0) return false;
        if (Float.compare(experienceGained, that.experienceGained) != 0) return false;
        if (handsPlayed != that.handsPlayed) return false;
        if (!uid.equals(that.uid)) return false;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + player.hashCode();
        result = 31 * result + (profit != 0.0f ? Float.floatToIntBits(profit) : 0);
        result = 31 * result + (experienceGained != 0.0f ? Float.floatToIntBits(experienceGained) : 0);
        result = 31 * result + handsPlayed;
        return result;
    }

    public String getUid() {
        return uid;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float getExperienceGained() {
        return experienceGained;
    }

    public void setExperienceGained(float experienceGained) {
        this.experienceGained = experienceGained;
    }

    public int getHandsPlayed() {
        return handsPlayed;
    }

    public void setHandsPlayed(int handsPlayed) {
        this.handsPlayed = handsPlayed;
    }
}
