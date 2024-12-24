package com.ghostnet.beans;

import com.ghostnet.dao.BergendePersonDao;
import com.ghostnet.entities.BergendePerson;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@RequestScoped
public class BergendePersonBean {
    private static final Logger logger = LoggerFactory.getLogger(BergendePersonBean.class);
    private BergendePerson bergendePerson = new BergendePerson();

    @Inject
    private BergendePersonDao bergendePersonDao;

    // Getter und Setter
    public BergendePerson getBergendePerson() {
        return bergendePerson;
    }

    public void setBergendePerson(BergendePerson bergendePerson) {
        this.bergendePerson = bergendePerson;
    }

    public String registrieren() {
        logger.info("Start der Registrierung für Benutzer: {}", bergendePerson.getMail());

        // Überprüfung, ob die E-Mail-Adresse bereits registriert ist
        if (bergendePersonDao.istEmailBereitsRegistriert(bergendePerson.getMail())) {
            FacesContext.getCurrentInstance().addMessage("mail",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Diese E-Mail-Adresse ist bereits registriert", null));
            return null; // Verhindert die Registrierung, falls die E-Mail bereits existiert
        }

        // Überprüfung, ob das Passwort den Anforderungen entspricht
        if (!isValidPassword(bergendePerson.getPassword())) {
            logger.warn("Passwortanforderungen werden nicht erfüllt für Benutzer: {}", bergendePerson.getMail());
            FacesContext.getCurrentInstance().addMessage("password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwortanforderungen werden nicht erfüllt", null));
            return null; // Verhindert die Weiterleitung, falls das Passwort ungültig ist
        }

        // Überprüfung, ob die E-Mail-Adresse gültig ist
        if (!isValidEmail(bergendePerson.getMail())) {
            logger.warn("Ungültige E-Mail-Adresse: {}", bergendePerson.getMail());
            FacesContext.getCurrentInstance().addMessage("mail",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültige E-Mail-Adresse", null));
            return null; // Verhindert die Weiterleitung, falls die E-Mail ungültig ist
        }

        try {
            // Passwort-Hashing
            String hashedPassword = BCrypt.hashpw(bergendePerson.getPassword(), BCrypt.gensalt());
            bergendePerson.setPassword(hashedPassword);

            // Speichern der registrierten Person in der Datenbank
            bergendePersonDao.speichern(bergendePerson);
            logger.info("Benutzer erfolgreich registriert: {}", bergendePerson.getMail());
            return "login.xhtml?faces-redirect=true"; // Weiterleitung zur Login-Seite
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der Registrierung für Benutzer: {}", bergendePerson.getMail(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Speichern der Registrierung", null));
            return null;
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        // Prüfung auf mindestens eine Ziffer, einen Großbuchstaben, einen Kleinbuchstaben und ein Sonderzeichen
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        return hasDigit && hasUpperCase && hasLowerCase && hasSpecialChar;
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        // Einfacheres Regex für Java-Validierung
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        return email.matches(emailRegex);
    }

}
