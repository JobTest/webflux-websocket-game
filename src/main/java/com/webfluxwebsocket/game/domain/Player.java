package com.webfluxwebsocket.game.domain;

import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * A Player.
 */
@Document(collection = "players")
public class Player {
    @Id
    private String id;
    private String win;
    private Integer number;
    private PlayerState state;

    public Player() {

    }

    public Player(String id, String win, Integer number, PlayerState state) {
        this.id = id;
        this.win = win;
        this.number = number;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", win='" + win + '\'' +
                ", number=" + number +
                ", state=" + state +
                '}';
    }
}
