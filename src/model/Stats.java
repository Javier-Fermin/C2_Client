/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The entity class for the Stats data.
 * 
 * @author Javier
 */
@XmlRootElement
public class Stats implements Serializable{

    /**
     * Id field for the Stats entity
     */
    private SimpleObjectProperty<StatsId> id;

    /**
     * Kills field for the Stats entity
     */
    private SimpleStringProperty kills;

    /**
     * Deaths field for the Stats entity
     */
    private SimpleStringProperty deaths; 

    /**
     * Assists field for the Stats entity
     */
    private SimpleStringProperty assists;

    /**
     * Team field of the Stats entity
     */
    private SimpleObjectProperty<Team> team;

    /**
     * Player of the play entity
     */
    private SimpleObjectProperty<Player> player;

    /**
     * Match of the play entity
     */
    private SimpleObjectProperty<Match> match;
    
    public Stats(){
        this.id = new SimpleObjectProperty<StatsId>();
        this.kills = new SimpleStringProperty();
        this.deaths = new SimpleStringProperty();
        this.assists = new SimpleStringProperty();
        this.team = new SimpleObjectProperty<Team>();
        this.player = new SimpleObjectProperty<Player>();
        this.match = new SimpleObjectProperty<Match>();
    }
    
    public Stats(StatsId id,String kills, String deaths, String assists, Team team, Player player, Match match) {
        this.id = new SimpleObjectProperty<StatsId>(id);
        this.kills = new SimpleStringProperty(kills);
        this.deaths = new SimpleStringProperty(deaths);
        this.assists = new SimpleStringProperty(assists);
        this.team = new SimpleObjectProperty<Team>(team);
        this.player = new SimpleObjectProperty<Player>(player);
        this.match = new SimpleObjectProperty<Match>(match);
    }

    /**
     * Getter for the id field
     * 
     * @return the id field
     */
    public StatsId getId() {
        return id.get();
    }

    /**
     * Setter for the id field
     * 
     * @param id the value to be set
     */
    public void setId(StatsId id) {
        this.id.set(id);
    }

    /**
     * Getter for the kills field
     * 
     * @return the kills field
     */
    public String getKills() {
        return kills.get();
    }

    /**
     * Setter for the kills field
     * 
     * @param kills the value to be set
     */
    public void setKills(String kills) {
        this.kills.set(kills);
    }

    /**
     * Getter for the deaths field
     * 
     * @return the deaths field
     */
    public String getDeaths() {
        return deaths.get();
    }

    /**
     * Setter for the deaths field
     * 
     * @param deaths the value to be set
     */
    public void setDeaths(String deaths) {
        this.deaths.set(deaths);
    }

    /**
     * Getter for the assists field
     * 
     * @return the assists field
     */
    public String getAssists() {
        return assists.get();
    }

    /**
     * Setter for the assists field
     * 
     * @param assists the value to be set
     */
    public void setAssists(String assists) {
        this.assists.set(assists);
    }

    /**
     * Getter for the team field
     * 
     * @return the team field
     */
    public Team getTeam() {
        return team.get();
    }

    /**
     * Setter for the team field
     * 
     * @param team the value to be set
     */
    public void setTeam(Team team) {
        this.team.set(team);
    }

    /**
     * Getter for the player field
     * 
     * @return the player field
     */
    public Player getPlayer() {
        return player.get();
    }

    /**
     * Setter for the player field
     * 
     * @param player the value to be set
     */
    public void setPlayer(Player player) {
        this.player.set(player);
    }

    /**
     * Getter for the match field
     * 
     * @return the match field
     */
    public Match getMatch() {
        return match.get();
    }

    /**
     * Setter for the match
     * 
     * @param match the value to be set
     */
    public void setMatch(Match match) {
        this.match.set(match);
    }

    /**
     * HashCode method implementation for the entity
     * 
     * @return an Integer as hashcode for the object
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * This method compares two Stats entities for equality.
     * 
     * @param obj the object to compare to
     * @return True if objects are equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Stats)) {
            return false;
        }
        Stats other = (Stats) obj;
        if (this.id != null && this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * This method returns a String representation for the entity
     * 
     * @return the String representation for the entity
     */
    @Override
    public String toString() {
        return "Stats{" + "id=" + id + ", kills=" + kills + ", deaths=" + deaths + ", assists=" + assists + ", team=" + team + ", player=" + player + ", match=" + match + '}';
    }
}
