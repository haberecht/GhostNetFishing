package com.ghostnet.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "meldende_person")
@PrimaryKeyJoinColumn(name = "person_id")
public class MeldendePerson extends Person {

    private boolean isAnonymous;

    // Getter und Setter
    public boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
}
