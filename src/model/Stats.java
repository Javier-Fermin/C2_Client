/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 * The entity class for the Stats data.
 * 
 * @author Javier
 */
public class Stats implements Serializable{

    /**
     * Id field for the Stats entity
     */
    private StatsId id;

    /**
     * Kills field for the Stats entity
     */
    private Integer kills;

    /**
     * Deaths field for the Stats entity
     */
    private Integer deaths; 

    /**
     * Assists field for the Stats entity
     */
    private Integer assists;

    /**
     * Team field of the Stats entity
     */
    private Team team;

    /**
     * Player of the play entity
     */
    private Player player;

    /**
     * Match of the play entity
     */
    private Match match;
    
    public Stats() {
    }

    public Stats(Integer kills, Integer deaths, Integer assists, Team team, Player player, Match match) {
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.team = team;
        this.player = player;
        this.match = match;
    }

    /**
     * Getter for the id field
     * 
     * @return the id field
     */
    public StatsId getId() {
        return id;
    }

    /**
     * Setter for the id field
     * 
     * @param id the value to be set
     */
    public void setId(StatsId id) {
        this.id = id;
    }

    /**
     * Getter for the kills field
     * 
     * @return the kills field
     */
    public Integer getKills() {
        return kills;
    }

    /**
     * Setter for the kills field
     * 
     * @param kills the value to be set
     */
    public void setKills(Integer kills) {
        this.kills = kills;
    }

    /**
     * Getter for the deaths field
     * 
     * @return the deaths field
     */
    public Integer getDeaths() {
        return deaths;
    }

    /**
     * Setter for the deaths field
     * 
     * @param deaths the value to be set
     */
    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    /**
     * Getter for the assists field
     * 
     * @return the assists field
     */
    public Integer getAssists() {
        return assists;
    }

    /**
     * Setter for the assists field
     * 
     * @param assists the value to be set
     */
    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    /**
     * Getter for the team field
     * 
     * @return the team field
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Setter for the team field
     * 
     * @param team the value to be set
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Getter for the player field
     * 
     * @return the player field
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Setter for the player field
     * 
     * @param player the value to be set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Getter for the match field
     * 
     * @return the match field
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Setter for the match
     * 
     * @param match the value to be set
     */
    public void setMatch(Match match) {
        this.match = match;
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
