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
- **POST /battles**: Für das Starten eines Kampfes zwischen zwei Benutzern.
- **POST /cards**: Für das Hinzufügen einer neuen Karte.
- **PUT /decks**: Für das Hinzufügen einer Karte zum Deck eines Benutzers.
- **GET /deck**: Für das Abrufen des Decks eines Benutzers.
- **POST /packages**: Für das Erstellen eines neuen Kartendecks.
- **GET /packages**: Für das Abrufen aller Kartendecks des Benutzers.
- **POST /transactions/packages**: Für das Kaufen eines Kartendecks.
- **GET /scoreboard**: Für das Abrufen der Bestenliste.
- **POST /sessions**: Für das Anmelden eines Benutzers.
- **GET /stats**: Für das Abrufen der Statistiken eines Benutzers.
- **POST /users**: Für das Registrieren eines neuen Benutzers.
- **GET /users**: Für das Abrufen aller Benutzer.
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
siehe Initialize-Postgres-Script.sql

### 4. Testen der Anwendung zum Beispiel
#### POST /users:
Registrierung eines neuen Benutzers, indem man folgendes JSON an http://localhost:10001/users sendet:
```JSON
{
    "username": "testuser",
    "password": "testpassword"
}
```

#### GET /users:
Rufe alle registrierten Benutzer ab:
- URL:http://localhost:10001
- Methode: GET
--- 
## App Design

### Entscheidungen & Struktur
- Die Anwendung ist als RESTful-API umgesetzt, die mit Java, IntelliJ, und PostgreSQL entwickelt wurde
- Die Architektur basiert auf dem Repository-Service-Controller-Muster, um eine klare Trennung von Datenbankzugriff, Geschäftslogik und API-Endpunkten zu gewährleisten.
- Die API bietet verschiedene Endpunkte zur Verwaltung von Benutzern, Karten, Paketen, Decks, Kämpfen und Statistiken.

### Klassendiagramm
Das Klassendiagramm kann als UML betrachtet werden. Die wichtigsten Klassen:

- **User**: Repräsentiert Benutzer mit Attributen wie username, password, elo, coins.
- **Card**: Beschreibt eine Spielkarte mit name, damage, element, type, monsterType.
- **Deck**: Enthält die vier ausgewählten Karten eines Spielers.
- **Battle**: Verwaltet den Kampf zwischen zwei Spielern unter Berücksichtigung von Regeln.
- **Repositories** (z. B. CardRepository, DeckRepository): Verwalten Datenbankabfragen.
- **Services** (z. B. BattleService, UserService): Enthalten Geschäftslogik.
---
## Lessons Learned
Während der Entwicklung dieses Projekts habe ich folgende wichtige Erkenntnisse gewonnen:
### 1. Saubere Architektur ist essenziell
- Die Trennung von Datenbank, Geschäftslogik und API hat zu einer besseren Wartbarkeit geführt.
- Design Patterns wie Repository-Pattern und Service-Layer haben geholfen, Code klarer zu strukturieren.
### 2. Datenbank-Optimierung ist entscheidend
- SQL-Optimierungen waren nötig, um Performance-Probleme (z. B. zu viele offene Verbindungen) zu lösen.
- Constraints (z. B. NOT NULL für monster_type) verhinderten spätere Fehler.
### 3. Fehlersuche & Debugging sind unverzichtbar
- Dank detaillierter Logs und Tests konnten viele Fehler frühzeitig entdeckt und behoben werden.
- Ein Problem mit Persistenz von Karten nach einem Battle wurde gelöst, indem die user_id in der DB aktualisiert wurde.
### 4. REST-API Design
- Das richtige Design von Endpunkten (GET /deck, POST /battles) war wichtig für eine intuitive API.
- Token-basierte Authentifizierung hat gut funktioniert, um Benutzer zu identifizieren.
---
## Unit Testing Decisions
### 3.1 Teststrategie
- JUnit 5 wurde für Unit-Tests verwendet.
- Mockito wurde genutzt, um Datenbank-Abfragen zu mocken.
- Abdeckung:
  - ✅ BattleTest – Tests für Kampf-Logik und Spezialfälle (z. B. Goblins vs. Drachen).
  - ✅ DeckTest – Sicherstellung, dass Decks korrekt erstellt und aktualisiert werden.
  - ✅ CardTest – Überprüfung der Karteneigenschaften.
  - ✅ UserTest – Tests für Benutzerverwaltung & Token.
  - ✅ PackageTest – Sicherstellung der Paketverwaltung.
  - ✅ CardRepositoryTest – Validierung von SQL-Abfragen auf Karten.
### 3.2 Beispielhafte Entscheidung
- Die Battle-Klasse war schwer zu testen, da sie zufällige Karten wählt.
    → Lösung: Dependency Injection von Decks ermöglicht kontrollierte Tests.
- Datenbank-Tests wurden durch Mocking von SQL-Abfragen ersetzt, um Tests schnell und unabhängig ausführen zu können.
---
## Unique Feature
Zusätzliches Feature: Siegerbonus
- Der Gewinner eines Battles erhält +10 Coins als Belohnung.
- Implementierung:
  - updateCoinsForExtraPrice(int userId, int coins) in UserRepositoryImpl
  - rewardWinnerWithCoins(int userId) in Battle
- Vorteil:
  - Spieler haben einen zusätzlichen Anreiz zu gewinnen.
  - Coins können genutzt werden, um neue Kartenpakete zu kaufen.
--- 
## Tracked Time
keine Zeitangabe, da ich die Zeit nicht getrackt habe. Ich schätze, dass ich ca. 30 Stunden für das Projekt benötigt habe.

---
## Fazit
Dieses Projekt war eine sehr gute Übung zur Implementierung einer sauberen, testbaren API.
Besonders Datenbank-Optimierung, Fehlerbehandlung und Tests haben viel zur Stabilität beigetragen

