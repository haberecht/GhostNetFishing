-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 24. Dez 2024 um 11:49
-- Server-Version: 10.4.28-MariaDB
-- PHP-Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `ghostnetdb`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `bergende_person`
--

CREATE TABLE `bergende_person` (
  `person_id` bigint(20) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `bergende_person`
--

INSERT INTO `bergende_person` (`person_id`, `password`) VALUES
(6, '$2a$10$lU60vdUJrVuO/ETNCEct6.Bfdtuo4k.RlpBjkWSd0otT1hG5tK2pm'),
(9, '$2a$10$T/XoF8JIZch5L9RM64.V7.W2UHphG5FI8zfJy8emIl.k18Lx580NC'),
(11, '$2a$10$/TEjaE.FJxdHPdtzMR5HEecrTOwNg8tq8cmYI5GvBzuTkqGmyFHR2');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `geisternetz`
--

CREATE TABLE `geisternetz` (
  `id` bigint(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `groesse` double NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `status_aenderungszeitpunkt` timestamp NOT NULL DEFAULT current_timestamp(),
  `bergende_person_id` bigint(20) DEFAULT NULL,
  `lastEditedBy` varchar(255) DEFAULT NULL,
  `lastEditedTime` datetime(6) DEFAULT NULL,
  `meldende_person_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `geisternetz`
--

INSERT INTO `geisternetz` (`id`, `latitude`, `longitude`, `groesse`, `status`, `status_aenderungszeitpunkt`, `bergende_person_id`, `lastEditedBy`, `lastEditedTime`, `meldende_person_id`) VALUES
(1, 71.98159065114336, 1.3843842956807917, 2000, 'GEBORGEN', '2024-11-17 11:01:17', 6, NULL, NULL, NULL),
(2, 50.70844416316964, -45.38971457745764, 1002, 'GEBORGEN', '2024-11-17 11:45:13', NULL, NULL, NULL, NULL),
(3, 15.984887641500206, -122.79948983456823, 904, 'GEBORGEN', '2024-11-18 14:03:23', NULL, NULL, NULL, NULL),
(5, -52.193389997591794, 8.596844336726974, 102, 'VERSCHOLLEN', '2024-11-18 14:13:35', 6, NULL, NULL, NULL),
(6, 4.0363001438452075, 61.6257473442343, 463, 'GEBORGEN', '2024-11-18 14:20:07', NULL, NULL, NULL, NULL),
(7, 49.757461602255205, -20.117898323795487, 553, 'GEBORGEN', '2024-11-19 13:22:33', NULL, NULL, NULL, NULL),
(8, 75.023530269752, 6.325227877834152, 100, 'GEBORGEN', '2024-11-19 13:26:09', 6, NULL, NULL, NULL),
(9, 72.05295031620136, 43.060440680721115, 350, 'GEBORGEN', '2024-11-19 16:51:36', NULL, NULL, NULL, NULL),
(10, 55.934791463307164, 4.019000193813156, 210, 'GEBORGEN', '2024-11-19 16:56:39', 6, NULL, NULL, NULL),
(11, 56.403048120351116, 0.6668338045598432, 1231, 'VERSCHOLLEN', '2024-11-24 17:39:53', NULL, NULL, NULL, NULL),
(14, 34.57414834642113, 20.523850665114786, 123, 'GEBORGEN', '2024-12-22 17:14:06', 6, NULL, NULL, NULL),
(15, 21.372388931083307, 37.891375000270386, 432, 'VERSCHOLLEN', '2024-12-22 17:14:54', 6, NULL, NULL, NULL),
(16, 36.26346989264761, 19.058089271310855, 188, 'GEMELDET', '2024-12-22 17:32:26', 6, NULL, NULL, NULL),
(20, 123, 120, 123, 'GEBORGEN', '2024-12-24 09:46:36', NULL, NULL, NULL, 10),
(21, 10, 10, 1, 'GEMELDET', '2024-12-24 10:10:25', 6, NULL, NULL, NULL),
(22, 54, 12, 123, 'BERGUNG_BEVORSTEHEND', '2024-12-24 10:17:36', 6, NULL, NULL, NULL),
(23, 123, 111, 11, 'GEMELDET', '2024-12-24 10:36:22', NULL, NULL, NULL, 12);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `meldende_person`
--

CREATE TABLE `meldende_person` (
  `isAnonymous` bit(1) NOT NULL,
  `person_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `meldende_person`
--

INSERT INTO `meldende_person` (`isAnonymous`, `person_id`) VALUES
(b'0', 10),
(b'1', 12);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `person`
--

CREATE TABLE `person` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `person`
--

INSERT INTO `person` (`id`, `name`, `surname`, `mail`, `phoneNumber`) VALUES
(6, 'Jan', 'Haberecht', 'haberecht.jan@gmail.com', '015253371155'),
(9, 'Test', 'Test', 'test@test.de', '0123456789'),
(10, 'Simone', 'Peters', NULL, '03671264121'),
(11, 'Simone', 'Peters', 'simone.peters@web.de', '0123456789'),
(12, 'Anonym', '', NULL, NULL);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `bergende_person`
--
ALTER TABLE `bergende_person`
  ADD PRIMARY KEY (`person_id`);

--
-- Indizes für die Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  ADD PRIMARY KEY (`id`),
  ADD KEY `bergende_person_id` (`bergende_person_id`),
  ADD KEY `FK6k24f77cnoo5kf84vmps1ju2f` (`meldende_person_id`);

--
-- Indizes für die Tabelle `meldende_person`
--
ALTER TABLE `meldende_person`
  ADD PRIMARY KEY (`person_id`);

--
-- Indizes für die Tabelle `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT für Tabelle `person`
--
ALTER TABLE `person`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `bergende_person`
--
ALTER TABLE `bergende_person`
  ADD CONSTRAINT `bergende_person_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);

--
-- Constraints der Tabelle `geisternetz`
--
ALTER TABLE `geisternetz`
  ADD CONSTRAINT `FK6k24f77cnoo5kf84vmps1ju2f` FOREIGN KEY (`meldende_person_id`) REFERENCES `meldende_person` (`person_id`),
  ADD CONSTRAINT `geisternetz_ibfk_1` FOREIGN KEY (`bergende_person_id`) REFERENCES `bergende_person` (`person_id`) ON DELETE SET NULL;

--
-- Constraints der Tabelle `meldende_person`
--
ALTER TABLE `meldende_person`
  ADD CONSTRAINT `FKcbe3amox1gb7u5xts1qh32qn0` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
