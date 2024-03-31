package blackjack.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;



@Entity
public class Player {

    private @Id @GeneratedValue Long uid;
    private String username;
    private float bank;
    private float level;


    public Player(String username, float bank, float level) {

        this.username = username;
        this.bank = bank;
        this.level = level;
    }

    public Player() {

    }


    public Long getUid() {
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


    public void setUid(Long id) {
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
        Player employee = (Player) o;
        return Objects.equals(this.uid, employee.uid) && Objects.equals(this.username, employee.username)
                && Objects.equals(this.bank, employee.bank) && Objects.equals(this.level, employee.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uid, this.username, this.bank, this.level);
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + this.uid + ", name='" + this.username + '\'' + ", bank='" + this.bank + '\'' +
                            ", level='" + this.level +'}';
    }
}