/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class has the data of a User that has admin privileges
 * 
 * @author javie
 */
@XmlRootElement
public class Admin extends User{

    public Admin(Date joinDate, String name, String passwd, String phone, String email, String address, UserType userType) {
        super(name, passwd, phone, email, address, userType);
        this.joinDate = new SimpleObjectProperty<Date>(joinDate);
    }

    public Admin() {
        super();
        joinDate = new SimpleObjectProperty<Date>();
    }
    
    /**
     * This is the date when the Admin joined the application
     */
    private SimpleObjectProperty<Date> joinDate;

    /**
     * 
     * @return 
     */
    public Date getJoinDate() {
        return joinDate.get();
    }

    /**
     * 
     * @param joinDate 
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate.set(joinDate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.joinDate);
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
        final Admin other = (Admin) obj;
        if (!Objects.equals(this.joinDate, other.joinDate)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Admin{" + ", joinDate=" + joinDate + '}';
    }
    
    
}