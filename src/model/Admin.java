/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.Objects;

/**
 * This class has the data of a User that has admin privileges
 * 
 * @author javie
 */
public class Admin extends User{

    public Admin(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Admin(Date joinDate, String name, String passwd, String phone, String email, String address, UserType userType) {
        super(name, passwd, phone, email, address, userType);
        this.joinDate = joinDate;
    }

    public Admin() {
    }
    
    /**
     * This is the date when the Admin joined the application
     */
    private Date joinDate;

    /**
     * 
     * @return 
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * 
     * @param joinDate 
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
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