/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
        private SimpleIntegerProperty id;

        /**
         * playedDate field for the Match entity
         */
        private Date playedDate;

        /**
         * winner field for the Match entity
         */
        private SimpleStringProperty winner;

        /**
         * tournament field for the Match entity
         */
        private SimpleObjectProperty<Tournament> tournament;

        /**
         * league field for the Match entity
         */
        private SimpleObjectProperty<League> league;

        /**
         * plays of the Match entity
         */
        private Set<Stats> stats;

        /**
         * descrition of the match
         */
        private SimpleStringProperty description;

        public SimpleStringProperty getDescription() {
            return description;
        }

        public void setDescription(SimpleStringProperty description) {
            this.description = description;
        }

        public SimpleIntegerProperty getId() {
            return id;
        }

        public void setId(SimpleIntegerProperty id) {
            this.id = id;
        }

        public Date getPlayedDate() {
            return playedDate;
        }

        public void setPlayedDate(Date playedDate) {
            this.playedDate = playedDate;
        }

        public SimpleStringProperty getWinner() {
            return winner;
        }

        public void setWinner(SimpleStringProperty winner) {
            this.winner = winner;
        }

        public SimpleObjectProperty<Tournament> getTournament() {
            return tournament;
        }

        public void setTournament(SimpleObjectProperty<Tournament> tournament) {
            this.tournament = tournament;
        }

        public SimpleObjectProperty<League> getLeague() {
            return league;
        }

        public void setLeague(SimpleObjectProperty<League> league) {
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
            return "Match [id=" + id + ", playedDate=" + playedDate + ", winner=" + winner + ", tournament=" + tournament
                    + ", league=" + league + ", stats=" + stats + "]";
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

    }
