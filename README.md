# JavaFX Adressbuch & Terminplaner

## Projektbeschreibung

Dieses JavaFX-Projekt implementiert ein grafisches **Adressbuch** mit integriertem **Terminplaner**. Beide Komponenten besitzen eine eigenständige Benutzeroberfläche und verwalten Kontakte sowie Termine persistent während der Programmlaufzeit.

### Hauptfunktionen:

- **Adressbuch**:

  - Kontakte anlegen, anzeigen, suchen und löschen
  - Fehlerbehandlung bei doppelten oder ungültigen Schlüsseln
  - Steuerung über grafische Oberfläche und Parser-Eingabezeile

- **Terminplaner**:

  - Verwaltung von Terminen inkl. Konfliktprüfung
  - Terminübersicht und Terminlöschung
  - Individuelle View-Komponenten per JavaFX

Die Benutzeroberflächen wurden mit dem **SceneBuilder** erstellt und als `.fxml`-Dateien eingebunden.

---

## Verwendete Technologien:

- Java 17+
- JavaFX
- SceneBuilder (GUI-Layout-Editor)
- Ant (build.xml)
- JAR-Erstellung inkl. JavaFX-Abhängigkeiten

---

## Projektstruktur:

```bash
JavaFX_Adressbuch_project-main/
├── build/         # Kompilierte .class-Dateien
├── dist/          # Fertiges .jar und JavaFX-Bibliotheken
├── src/           # (ursprüngliche Quellcodestruktur innerhalb build/classes/)
├── manifest.mf    # JAR-Metadaten
├── build.xml      # Ant-Build-Skript
└── .gitignore
```

---

## Kompilieren und Ausführen:

### Mit Ant (empfohlen)

1. Im Projektverzeichnis:

```bash
ant clean
ant jar
```

2. Ausführen des Programms über die **StartPlaner.java**-Klasse:

```bash
java -cp dist/Terminkalender.jar terminplaner.StartPlaner
```

Oder direkt aus der IDE über die Klasse `StartPlaner.java`.

Hinweis: Die ".jar"-Datei kann ebenfalls gestartet werden, sofern alle JavaFX-Abhängigkeiten korrekt eingebunden sind.

---

##  Oberfläche & Steuerung:

### Adressbuch

- JavaFX-Fenster mit Formular zur Kontaktverwaltung
- Parser-Zeile für manuelle Textbefehle
- Fehlermeldungen über Dialogboxen

### Terminplaner

- Terminliste mit Übersicht und Auswahl
- Eingabemaske für neue Termine
- Warnung bei Terminüberschneidung

Die grafischen Oberflächen wurden mit **SceneBuilder** gestaltet und als FXML-Dateien eingebunden. Diese befinden sich in:

- `adressbuchView.fxml`
- `planerView.fxml`
- `terminView.fxml`

---

## Dokumentation:

- Alle Klassen im Paket `adressbuch/` und `terminplaner/`
- Build-Anleitung in `build.xml`
- Start der Anwendung über `StartPlaner.java`
- .jar-Datei in `dist/` direkt lauffähig

---

## Autor

- Bertan Bahcivan

