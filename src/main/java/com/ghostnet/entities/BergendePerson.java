package com.ghostnet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "bergende_person")
@PrimaryKeyJoinColumn(name = "person_id")
public class BergendePerson extends Person {

    private String password;

    // Getter und Setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
