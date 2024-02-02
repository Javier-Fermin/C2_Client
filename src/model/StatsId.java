package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the composite key (ID) for the entity Stats.
 * 
 * <p>
 * The StatsId class is used as the primary key for the Stats entity,
 * containing the player ID and match ID.
 * </p>
 * 
 * <p>
 * This class implements the Serializable interface to support serialization.
 * </p>
 * 
 * @author Javier
 */
public class StatsId implements Serializable {

    /**
     * Id of the <code>Player</code> for the <code>Stats</code>
     */
    private String playerId;
    
    /**
     * Id of the <code>Match</code> for the <code>Stats</code>
     */
    private String matchId;

    /**
     * Gets the player ID.
     * 
     * @return The player ID.
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player ID.
     * 
     * @param playerId The player ID to set.
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the match ID.
     * 
     * @return The match ID.
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     * Sets the match ID.
     * 
     * @param matchId The match ID to set.
     */
    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    /**
     * Default constructor for StatsId.
     */
    public StatsId() {
    }

    /**
     * Parameterized constructor for StatsId.
     * 
     * @param matchId The match ID.
     * @param playerId The player ID.
     */
    public StatsId(String matchId, String playerId) {
        this.playerId = playerId;
        this.matchId = matchId;
    }

    /**
     * Generates a hash code for this StatsId object.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.playerId);
        hash = 53 * hash + Objects.hashCode(this.matchId);
        return hash;
    }

    /**
     * Checks if this StatsId object is equal to another object.
     * 
     * @param obj The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StatsId other = (StatsId) obj;
        return Objects.equals(this.playerId, other.playerId) && Objects.equals(this.matchId, other.matchId);
    }

    /**
     * Returns a string representation of the StatsId object.
     * 
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "StatsId{" + "playerId=" + playerId + ", matchId=" + matchId + '}';
    }
}