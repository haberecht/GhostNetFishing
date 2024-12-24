package com.ghostnet.test;

import com.ghostnet.entities.Geisternetz;
import com.ghostnet.dao.GeisternetzDao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        GeisternetzDao geisternetzDao = new GeisternetzDao();
        String jdbcUrl = "jdbc:mysql://localhost:3306/ghostnetdb";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            if (connection != null) {
                System.out.println("Verbindung zur Datenbank erfolgreich!");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Verbinden zur Datenbank:");
            e.printStackTrace();
        }

        try {
            List<Geisternetz> geisternetze = geisternetzDao.alleGeisternetze();

            // Prüfen, ob die Liste korrekt abgerufen wurde
            if (geisternetze != null && !geisternetze.isEmpty()) {
                System.out.println("Erfolgreich Geisternetze aus der Datenbank abgerufen:");
                for (Geisternetz netz : geisternetze) {
                    System.out.println("Geisternetz ID: " + netz.getId() + ", Latitude: " + netz.getLatitude() + ", Longitude: " + netz.getLongitude());
                }
            } else {
                System.out.println("Keine Geisternetze in der Datenbank gefunden oder die Liste ist leer.");
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Geisternetze:");
            e.printStackTrace();
        }
    }

}
