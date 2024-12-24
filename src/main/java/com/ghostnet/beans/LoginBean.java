package com.ghostnet.beans;

import com.ghostnet.dao.BergendePersonDao;
import com.ghostnet.entities.BergendePerson;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped // SessionScoped, um den Benutzerstatus während der Sitzung zu halten
public class LoginBean implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(LoginBean.class);

    private String email;
    private String password;
    private boolean loggedIn;
    private BergendePerson loggedInUser; // Neue Eigenschaft für die eingeloggte Person

    @Inject
    private BergendePersonDao bergendePersonDao;

    // Getter und Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public BergendePerson getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(BergendePerson loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public String login() {
        logger.info("Login-Versuch für E-Mail: {}", email);

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte geben Sie E-Mail und Passwort ein", null));
            return null;
        }

        BergendePerson person = bergendePersonDao.findByEmail(email);
        if (person == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Benutzer nicht gefunden", null));
            return null;
        }

        // Vergleich des eingegebenen Passworts mit dem gespeicherten Hash
        if (!BCrypt.checkpw(password, person.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültiges Passwort", null));
            return null;
        }

        // Erfolgreicher Login - Speichern der angemeldeten Person
        loggedIn = true;
        loggedInUser = person; // Speichern der eingeloggten Person
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", person);

        logger.info("Erfolgreicher Login für Benutzer: {}", email);
        return "/secured/securedIndex.xhtml?faces-redirect=true";
    }

    public String logout() {
        try {
            // Session invalidieren
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

            // Benutzerstatus zurücksetzen
            loggedIn = false;
            loggedInUser = null; // Zurücksetzen der eingeloggten Person
            logger.info("Benutzer wurde ausgeloggt");

            // Manuelle Weiterleitung
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            return null; // Keine JSF-Navigation notwendig
        } catch (IOException e) {
            logger.error("Fehler bei der Weiterleitung nach dem Logout", e);
            return null;
        }
    }
}
