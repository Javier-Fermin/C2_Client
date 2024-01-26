/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author imape
 */
@XmlRootElement
public class Match implements Serializable {

    /**
     * Id field for the Match entity
     */
    private Integer id;

    /**
     * playedDate field for the Match entity
     */
    private Date playedDate;

    /**
     * winner field for the Match entity
     */
    private Team winner;

    /**
     * tournament field for the Match entity
     */
    private Tournament tournament;

    /**
     * league field for the Match entity
     */
    private League league;

    /**
     * plays of the Match entity
     */
    private Set<Stats> stats;

    /**
     * descrition of the match
     */
    private String description;

    public Match() {
    }

    public Match(Match match) {
        this.id = match.getId();
        this.league = match.getLeague();
        this.playedDate = match.getPlayedDate();
        this.description = match.getDescription();
        this.stats = match.getStats();
        this.tournament = match.getTournament();
        this.winner = match.getWinner();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPlayedDate() {
        return playedDate;
    }

    public void setPlayedDate(Date playedDate) {
        this.playedDate = playedDate;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Set<Stats> getStats() {
        return stats;
    }

    public void setStats(Set<Stats> stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Match)) {
            return false;
        }
        Match other = (Match) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Match(Integer id, Date playedDate, Team winner, Tournament tournament, League league, Set<Stats> stats, String description) {
        this.id = id;
        this.playedDate = playedDate;
        this.winner = winner;
        this.tournament = tournament;
        this.league = league;
        this.stats = stats;
        this.description = description;
    }

}
