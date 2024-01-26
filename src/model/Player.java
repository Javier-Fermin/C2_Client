/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Objects;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class is the entity player
 *
 * @author javie
 */
@XmlRootElement
public class Player extends User {

    /**
     * Active field for the player entity
     */
    private SimpleBooleanProperty active;
    /**
     * nickname field for the player entity
     */
    private SimpleStringProperty nickname;
    
    public Player() {
        super();
        nickname = new SimpleStringProperty();
        active = new SimpleBooleanProperty();
    }
    
    public Player(Boolean active, String nickname, String name, String passwd, String phone, String email, String address, UserType userType, ObservableList<Stats> stats) {
        super(name, passwd, phone, email, address, userType);
        this.active = new SimpleBooleanProperty(active);
        this.nickname = new SimpleStringProperty(nickname);
    }
    
    public Player(Player player) {
        super(player.getName(), player.getPasswd(), player.getPhone(), player.getEmail(), player.getAddress(), player.getUserType());
        this.active = new SimpleBooleanProperty(player.getActive());
        this.nickname = new SimpleStringProperty(player.getNickname());
    }

    public Boolean getActive() {
        return active.get();
    }

    public void setActive(Boolean active) {
        this.active.set(active);
    }

    public String getNickname() {
        return nickname.get();
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    /**
     * HashCode and equals for Player entity 
     */
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.active);
        hash = 59 * hash + Objects.hashCode(this.nickname);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        /*if (getClass() != obj.getClass()) {
            return false;
        }*/
        final Player other = (Player) obj;
        if (!Objects.equals(this.nickname, other.nickname)) {
            return false;
        }
        if (!Objects.equals(this.active, other.active)) {
            return false;
        }
        return true;
    }

    /**
     * toString to player entity
     */
    @Override
    public String toString() {
        return  nickname.get();
    }

}
