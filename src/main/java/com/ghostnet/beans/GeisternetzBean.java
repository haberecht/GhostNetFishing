package com.ghostnet.beans;

import com.ghostnet.dao.GeisternetzDao;
import com.ghostnet.dao.MeldendePersonDao;
import com.ghostnet.entities.BergendePerson;
import com.ghostnet.entities.Geisternetz;
import com.ghostnet.entities.MeldendePerson;
import com.ghostnet.entities.Status;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Named
@RequestScoped
public class GeisternetzBean implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(GeisternetzBean.class);

    private List<Geisternetz> geisternetze;
    private Geisternetz neuesGeisternetz;
    private MeldendePerson meldendePerson;

    @Inject
    private GeisternetzDao geisternetzDao;

    @Inject
    private MeldendePersonDao meldendePersonDao;

    @Inject
    private LoginBean loginBean;

    @PostConstruct
    public void init() {
        neuesGeisternetz = new Geisternetz();
        meldendePerson = new MeldendePerson();

        try {
            geisternetze = geisternetzDao.alleGeisternetze();
            if (geisternetze == null || geisternetze.isEmpty()) {
                logger.warn("Keine Geisternetze gefunden!");
                geisternetze = new ArrayList<>();
            } else {
                logger.info("Geisternetze erfolgreich geladen: {}", geisternetze.size());
            }
        } catch (Exception e) {
            logger.error("Fehler beim Laden der Geisternetze", e);
            geisternetze = new ArrayList<>();
        }
    }

    public List<Geisternetz> getGeisternetze() {
        return geisternetze;
    }

    public Geisternetz getNeuesGeisternetz() {
        return neuesGeisternetz;
    }

    public void setNeuesGeisternetz(Geisternetz neuesGeisternetz) {
        this.neuesGeisternetz = neuesGeisternetz;
    }

    public MeldendePerson getMeldendePerson() {
        return meldendePerson;
    }

    public void setMeldendePerson(MeldendePerson meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public void addGeisternetz() {
        try {
            if (!validateGeisternetz()) {
                return;
            }

            if (loginBean.isLoggedIn()) {
                handleLoggedInUser();
            } else {
                if (meldendePerson.getIsAnonymous()) {
                    handleAnonymousUser();
                } else {
                    handleNamedUser();
                }
            }

            neuesGeisternetz.setStatus(Status.GEMELDET);
            geisternetzDao.speichern(neuesGeisternetz);
            geisternetze.add(neuesGeisternetz);

            resetForm();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Geisternetz wurde erfolgreich gemeldet", null));
        } catch (Exception e) {
            logger.error("Fehler beim Speichern des Geisternetzes", e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Speichern des Geisternetzes: " + e.getMessage(), null));
        }
    }

    private boolean validateGeisternetz() {
        if (neuesGeisternetz.getLatitude() == 0 || neuesGeisternetz.getLongitude() == 0 || neuesGeisternetz.getGroesse() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte alle Felder korrekt ausfüllen", null));
            return false;
        }
        return true;
    }

    public String getBergendePersonDetails(Geisternetz netz) {
        if (netz.getBergendePerson() != null) {
            String name = netz.getBergendePerson().getName() + " " + netz.getBergendePerson().getSurname();
            String email = netz.getBergendePerson().getMail();
            return name + " (" + email + ")";
        }
        return "Keine bergende Person";
    }

    private void handleLoggedInUser() {
        BergendePerson aktuelleBergendePerson = (BergendePerson) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("user");

        if (aktuelleBergendePerson != null) {
            // Kein Setzen der bergenden Person beim Melden
            logger.info("Angemeldete Person meldet ein Geisternetz: {}", aktuelleBergendePerson.getName());
        } else {
            throw new IllegalStateException("Keine gültige bergende Person in der Session gefunden.");
        }

        // Meldende Person wird nur gesetzt, wenn nicht anonym
        meldendePerson.setName(aktuelleBergendePerson.getName());
        meldendePerson.setSurname(aktuelleBergendePerson.getSurname());
        meldendePerson.setIsAnonymous(false);
    }


    private void handleAnonymousUser() {
        meldendePerson.setName("Anonym");
        meldendePerson.setSurname("");
        meldendePerson.setPhoneNumber(null);
        meldendePerson.setIsAnonymous(true);

        meldendePersonDao.speichern(meldendePerson);
        neuesGeisternetz.setMeldendePerson(meldendePerson);
    }

    private void handleNamedUser() {
        if (meldendePerson.getPhoneNumber() == null || meldendePerson.getPhoneNumber().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Telefonnummer ist erforderlich, wenn die Person nicht anonym ist", null));
            throw new IllegalStateException("Telefonnummer fehlt");
        }

        meldendePerson.setIsAnonymous(false); // Nicht anonym
        meldendePersonDao.speichern(meldendePerson); // Speichern in der DB
    }

    private void resetForm() {
        neuesGeisternetz = new Geisternetz();
        meldendePerson = new MeldendePerson();
    }

    public String getGeisternetzeAsJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(geisternetze);
        } catch (Exception e) {
            logger.error("Fehler beim Konvertieren der Geisternetze in JSON", e);
            return "[]";
        }
    }

    public String getStatusText(String status) {
        switch (status) {
            case "GEMELDET":
                return "Gemeldet";
            case "BERGUNG_BEVORSTEHEND":
                return "Bergung bevorstehend";
            case "GEBORGEN":
                return "Geborgen";
            case "VERSCHOLLEN":
                return "Verschollen";
            default:
                return "Unbekannt";
        }
    }

    public void bergeNetz(Geisternetz netz) {
        if (!loginBean.isLoggedIn()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sie müssen eingeloggt sein, um diese Aktion auszuführen.", null));
            return;
        }

        BergendePerson aktuellePerson = (BergendePerson) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("user");

        if (aktuellePerson == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine angemeldete bergende Person gefunden.", null));
            return;
        }

        try {
            // Status auf BERGUNG_BEVORSTEHEND setzen
            netz.setStatus(Status.BERGUNG_BEVORSTEHEND);

            // Die angemeldete Person als BergendePerson setzen
            netz.setBergendePerson(aktuellePerson);

            // Änderungen speichern
            geisternetzDao.aktualisieren(netz);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Geisternetz wurde zur Bergung markiert.", null));
        } catch (Exception e) {
            logger.error("Fehler beim Markieren der Bergung für Geisternetz mit ID: {}", netz.getId(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Markieren der Bergung.", null));
        }
    }


    public void markiereAlsGeborgen(Geisternetz netz) {
        if (netz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültiges Geisternetz", null));
            return;
        }

        try {
            // Ändere den Status auf "GEBORGEN"
            netz.setStatus(Status.GEBORGEN);
            geisternetzDao.aktualisieren(netz); // Änderungen speichern

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Geisternetz wurde als geborgen markiert.", null));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(GeisternetzBean.class);
            logger.error("Fehler beim Markieren als geborgen für Geisternetz mit ID: {}", netz.getId(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Markieren als geborgen.", null));
        }
    }

    public void markiereAlsVerschollen(Geisternetz netz) {
        if (netz == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ungültiges Geisternetz", null));
            return;
        }

        try {
            // Ändere den Status auf "VERSCHOLLEN"
            netz.setStatus(Status.VERSCHOLLEN);
            geisternetzDao.aktualisieren(netz); // Änderungen speichern

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Das Geisternetz wurde als verschollen markiert.", null));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(GeisternetzBean.class);
            logger.error("Fehler beim Markieren als verschollen für Geisternetz mit ID: {}", netz.getId(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Markieren als verschollen.", null));
        }
    }



}

