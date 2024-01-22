/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author javie
 */
public class StatsId implements Serializable{

    private String playerId;
    private String matchId;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId =matchId;
    }

    public StatsId() {
    }

    public StatsId(String matchId, String playerId) {
        this.playerId =playerId;
        this.matchId =matchId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.playerId);
        hash = 53 * hash + Objects.hashCode(this.matchId);
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatsId other = (StatsId) obj;
        if (!Objects.equals(this.playerId, other.playerId)) {
            return false;
        }
        if (!Objects.equals(this.matchId, other.matchId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StatsId{" + "playerId=" + playerId + ", matchId=" + matchId + '}';
    }
    
    
}
