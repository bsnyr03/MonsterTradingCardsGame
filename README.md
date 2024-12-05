# MonsterTradingCardsGame

## Dokumentation des MonsterTradingCardsGame

Das **MonsterTradingCardsGame (MTCG)** ist eine RESTful-Anwendung, die für das Handeln und Kämpfen mit magischen 
Karten entwickelt wurde. Die Applikation biete eie serverseitige Implementierung für Benutzerverwaltun, 
Kartenverwaltung und weitere Spielfunktionen.

## GIT-Repository
https://github.com/bsnyr03/MonsterTradingCardsGame

---
## HTTP-Server Einrichtung

Der HTTP-Server wurde ohne Hilfsframeworks entwickelt und enthält:

- **Request- und Response-Parsing:** Eigene Implementierung zur Verarbeitung von HTTP-Methoden (z. B. GET, POST).
- **Routing-Funktionalität:** Dynamische Auflösung von Endpunkten mit einem `Router`.
- **REST-API-Endpunkte:** Vollständig implementierte Endpunkte für die Verwaltung von Benutzern und Karten.

Die Endpunkte sind wie folgt strukturiert:
- **POST /users**: Für Benutzerregistrierung und Login.
- **GET /users**: Für das Abrufen aller Benutzer.
- **POST /cards**: Für das Hinzufügen einer neuen Karte.
- **GET /cards**: Für das Abrufen aller Karten.
---

## Erstellen und Verwalten des Docker-Containers und der Datenbank

Die Anwendung verwendet eine PostgreSQL-Datenbank, die mit Docker verwaltet wird.

### 1. Einrichten des Docker-Containers

Führe folgende Befehle im Terminal aus:
```bash
docker run --name mtcg -e POSTGRES_USER=baris -e POSTGRES_DB=mtcg -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
```
### 2. Zugriff auf die Datenbank

```bash
docker exec -it mtcg bash
psql -U baris -d mtcg
```
### 3. Tabellen erstellen

```SQL
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255)
);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    damage DOUBLE PRECISION NOT NULL,
    element VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL
);
```
### 4. Testen der Anwendung
#### POST /users:
Registrierung eines neuen Benutzers, indem man folgendes JSON an http://localhost:10001/users sendet:
```JSON
{
    "username": "testuser",
    "password": "testpassword"
}
```
Anmeldung des selben Users erfolgt durch Wiederholung des selben POSTs

#### GET /users:
Rufe alle registrierten Benutzer ab:
- URL:http://localhost:10001
- Methode: GET

#### POST/cards:
Hinzufügen einer neuen Karte mit folgendem JSON:
```JSON
{
    "name": "NormalMonster",
    "damage": 30.0,
    "element": "NORMAL",
    "type": "MONSTER"
}
```
#### GET/cards:
Rufe alle Karten ab:
- URL://localhost:10001/cards
- Methode: GET

---

## Design und Architektur der Anwendung

### Design-Entscheidungen
1. Layered Architecture (Schichtenarchitektur):
Die Anwendung ist in folgende Schichten unterteilt:
   - Controller: Verarbeiten HTTP-Anfragen und -Antworten.
   - Services: Enthalten die Geschäftslogik.
   - Repositories: Verwalten die Datenbankinteraktionen.
   - Modelle: Repräsentieren die Datenstrukturen.

2. Eigener HTTP-Server:
Ein leichter, selbst entwickelter HTTP-Server verarbeitet RESTful-API-Anfragen.

3. Token-basierte Authentifizierung:
Beim Login wird ein einzigartiger Token für jeden Benutzer generiert und gespeichert.

4. Dockerisierte Datenbank:
PostgreSQL wird als Datenbank verwendet, und Docker stellt eine konsistente Entwicklungsumgebung sicher.

--- 







