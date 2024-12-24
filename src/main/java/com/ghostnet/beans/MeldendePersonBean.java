package com.ghostnet.beans;

import com.ghostnet.dao.GeisternetzDao;
import com.ghostnet.dao.MeldendePersonDao;
import com.ghostnet.entities.Geisternetz;
import com.ghostnet.entities.MeldendePerson;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@RequestScoped
public class MeldendePersonBean {

    private static final Logger logger = LoggerFactory.getLogger(MeldendePersonBean.class);

    private MeldendePerson meldendePerson = new MeldendePerson();
    private Geisternetz geisternetz = new Geisternetz();

    @Inject
    private MeldendePersonDao meldendePersonDao;

    @Inject
    private GeisternetzDao geisternetzDao;

    // Getter und Setter
    public MeldendePerson getMeldendePerson() {
        return meldendePerson;
    }

    public void setMeldendePerson(MeldendePerson meldendePerson) {
        this.meldendePerson = meldendePerson;
    }

    public Geisternetz getGeisternetz() {
        return geisternetz;
    }

    public void setGeisternetz(Geisternetz geisternetz) {
        this.geisternetz = geisternetz;
    }

    /**
     * Speichert die meldende Person und das zugehörige Geisternetz.
     */
    public void speichern() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Validierung der Eingaben
            if (meldendePerson.getName() == null || meldendePerson.getName().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name darf nicht leer sein", null));
                return;
            }

            if (meldendePerson.getSurname() == null || meldendePerson.getSurname().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nachname darf nicht leer sein", null));
                return;
            }

            if (!meldendePerson.getIsAnonymous() && (meldendePerson.getPhoneNumber() == null || meldendePerson.getPhoneNumber().isEmpty())) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Telefonnummer ist erforderlich, wenn die Person nicht anonym ist", null));
                return;
            }

            if (meldendePerson.getIsAnonymous() && meldendePerson.getPhoneNumber() != null && !meldendePerson.getPhoneNumber().isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Anonyme Personen dürfen keine Telefonnummer angeben", null));
                return;
            }

            // Speichern der meldenden Person
            meldendePersonDao.speichern(meldendePerson);
            logger.info("Meldende Person gespeichert: {} {}", meldendePerson.getName(), meldendePerson.getSurname());

            // Verknüpfen der meldenden Person mit dem Geisternetz und speichern
            geisternetz.setMeldendePerson(meldendePerson);
            geisternetzDao.speichern(geisternetz);
            logger.info("Geisternetz gespeichert: Latitude={}, Longitude={}", geisternetz.getLatitude(), geisternetz.getLongitude());

            // Erfolgsmeldung
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Geisternetz erfolgreich gemeldet", null));

            // Zurücksetzen der Eingaben
            meldendePerson = new MeldendePerson();
            geisternetz = new Geisternetz();
        } catch (Exception e) {
            logger.error("Fehler beim Speichern der meldenden Person oder des Geisternetzes", e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler beim Speichern", null));
        }
    }
}
