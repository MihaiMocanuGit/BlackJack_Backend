package blackjack.backend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class Player {


    private @Id String uid;
    private String username;
    private float bank;
    private float level;



    @ReadOnlyProperty
    @DocumentReference(lookup="{'player':?#{#self._id} }", lazy = true)
    @JsonManagedReference
    List<GameSummary> games = new ArrayList<>();

    public List<GameSummary> getGames() {
        return games;
    }

    public Player(String username, float bank, float level) {

        this.username = username;
        this.bank = bank;
        this.level = level;
    }

    public Player(String username, float bank, float level, List<GameSummary> games) {

        this.username = username;
        this.bank = bank;
        this.level = level;
        this.games = games;
    }

    public Player() {

    }


    public String getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public float getBank() {
        return this.bank;
    }
    public float getLevel() {
        return level;
    }

    public void setUid(String id) {
        this.uid = id;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setBank(float bank) {
        this.bank = bank;
    }
    public void setLevel(float level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Player))
            return false;
        Player player = (Player) o;
        return Objects.equals(this.uid, player.uid) && Objects.equals(this.username, player.username)
                && Objects.equals(this.bank, player.bank) && Objects.equals(this.level, player.level)
                && Objects.equals(this.games, player.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uid, this.username, this.bank, this.level, this.games);
    }

//    @Override
//    public String toString() {
//        return "Player{" + "id=" + this.uid + ", name='" + this.username + '\'' + ", bank='" + this.bank + '\'' +
//                            ", level='" + this.level +'}';
//    }

    @Override
    public String toString() {
        return "Player{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", bank=" + bank +
                ", level=" + level +
                ", games=" + games +
                '}';
    }
}