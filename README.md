# Ghost Net Fishing

Spring-Boot-Prototyp für die IU-Fallstudie **Aufgabe 1.3: Ghost Net Fishing**.

Die Anwendung bildet einen vereinfachten Prozess zum Melden, Übernehmen und Abschließen von Geisternetz-Fällen ab. Spring Boot wird als Alternative zu JSF/CDI genutzt; die Umsetzung erfolgt mit Spring Beans, Spring MVC und Spring Data JPA.

## Ausgewählte Anforderungen

1. MUST: Geisternetze können anonym erfasst werden.
2. MUST: Bergende Personen können sich für die Bergung eines Geisternetzes eintragen.
3. MUST: Bergende Personen sehen, welche Geisternetze noch zu bergen sind.
4. MUST: Übernommene Geisternetze können als geborgen gemeldet werden.
5. COULD: Beliebige Personen können Geisternetze als verschollen melden.

## Technologie-Stack

- Java 17
- Maven
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Hibernate
- H2 Database
- Thymeleaf
- Bootstrap

## Start

```powershell
mvn spring-boot:run
```

Danach ist die Anwendung unter `http://localhost:8080` erreichbar.

## H2-Konsole

Die H2-Konsole ist unter `http://localhost:8080/h2-console` erreichbar und dient ausschließlich Entwicklungs- und Demonstrationszwecken sowie zur Kontrolle der persistenten Datenhaltung im Prototyp.

Zugangsdaten:

- JDBC URL: `jdbc:h2:mem:ghostnetdb`
- User: `sa`
- Passwort: leer lassen

## Architektur

Das Projekt folgt einer einfachen Spring-MVC-Struktur:

- `entity`: JPA-Entities und Enum
- `repository`: Spring-Data-JPA-Repositories
- `service`: fachliche Regeln, Statuswechsel und Transaktionen
- `controller`: Spring-MVC-Endpunkte für Formular- und Seitenflüsse
- `dto`: Formularobjekt für die Verschollenmeldung
- `templates`: Thymeleaf-Seiten mit Bootstrap
- `config`: Demo-Daten für den Prototyp
- `exception`: einfache fachliche Fehlerbehandlung

Die Controller enthalten nur Request-Verarbeitung. Die Businesslogik liegt zentral in `GhostNetService`. Die Persistenz erfolgt über JPA/Hibernate mit einer H2-In-Memory-Datenbank.

## Datenmodell

`GhostNet` enthält:

- `id`
- `latitude`
- `longitude`
- `estimatedSize`
- `description`
- `status`
- `assignedRecoveryPerson`
- `missingReporterName`
- `missingReporterPhone`

`RecoveryPerson` enthält:

- `id`
- `name`
- `phone`

Zwischen `GhostNet` und `RecoveryPerson` besteht eine Many-to-One-Beziehung: Ein Geisternetz darf maximal einer bergenden Person zugeordnet sein, während eine bergende Person mehrere Geisternetze übernehmen darf.

Eine Person, die ein Netz als verschollen meldet, wird nicht automatisch als `RecoveryPerson` gespeichert. Dafür werden `missingReporterName` und `missingReporterPhone` direkt am `GhostNet` gespeichert.

## Statuslogik

Die Statuslogik ist zentral in `GhostNetService` implementiert:

- `REPORTED`: anonym gemeldetes Geisternetz ohne bergende Person
- `RECOVERY_PENDING`: Bergung wurde von einer `RecoveryPerson` übernommen
- `RECOVERED`: übernommenes Netz wurde erfolgreich geborgen
- `MISSING`: Netz wurde als verschollen gemeldet

Erlaubte Übergänge:

- `REPORTED` -> `RECOVERY_PENDING`
- `REPORTED` -> `MISSING`
- `RECOVERY_PENDING` -> `RECOVERED`
- `RECOVERY_PENDING` -> `MISSING`

`RECOVERED` und `MISSING` sind fachliche Endzustände. Für abgeschlossene Fälle werden in der Oberfläche keine weiteren Aktionen angeboten.

## GitHub-Abgabe

Ins Repository gehören:

- `src/`
- `pom.xml`
- `README.md`
- `.gitignore`

Nicht ins Repository gehören:

- `target/`
- `.idea/`
- `*.iml`
- `.DS_Store`
- lokale Logs
