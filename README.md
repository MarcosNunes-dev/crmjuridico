# CRM Jurídico

A desktop CRM for small law firms, written in Java 21 with JavaFX and SQLite. It manages clients, legal cases, tasks and a hearings/deadlines agenda, with role-based access for administrators, lawyers and interns. The UI and data model are in Portuguese (Portugal).

## What it does

- **Authentication** – username/password login; passwords are stored as SHA-256 hashes. Deactivated users cannot log in.
- **Three roles** (`NivelAcesso`), each with its own JavaFX screen:
  - **Administrator** – manages users (create lawyers, interns and admins; edit; activate/deactivate), clients, cases, tasks and agenda.
  - **Lawyer** – dashboard, clients, cases, tasks and agenda; can add and edit records.
  - **Intern** – dashboard plus clients, cases, tasks and agenda, with search and filters (mostly read-oriented).
- **Clients** – individual/company records (name, address, phone, email, NIF, type); soft-delete via active/inactive.
- **Cases (processos)** – case number, action name, status, distribution/closing dates, linked to a client and a responsible lawyer.
- **Tasks** – title, description, priority, status, due date, linked to a case and a responsible user.
- **Agenda** – hearings/appointments with location, date/time and status, linked to a case and a user.
- **Persistence** – local SQLite file (`database/crm.db`); tables are created automatically by the console entry point.
- **Two front ends** – a JavaFX GUI (`MainFX`) and a text-based console app (`viewConsola.Main`) sharing the same controller and DAO layer.

## What it does NOT do

- No web or multi-user/networked mode: it is a single-machine app on a local SQLite file, with no server, sync or concurrent-access handling.
- No document management (no file upload, templates, or generated contracts/powers of attorney).
- No billing, invoicing, fee tracking or time tracking.
- No integration with court systems (e.g. PJe), e-mail, calendars or notifications/reminders.
- No automated tests (there is no `src/test`).
- No password recovery, user self-registration, or audit log.
- Passwords use plain unsalted SHA-256, which is **not** adequate for production use; the default admin credentials must be changed.
- The GUI does not create the database schema or the initial admin by itself (see below).
- No internationalisation: the interface is Portuguese only.

## Requirements

- JDK 21
- Maven (or the included `mvnw` wrapper)

## Getting started

1. **Initialise the database** by running the console app once. It creates the tables and, if the database is empty, a default administrator:

   ```
   username: admin
   password: 1234
   ```

   Change this password after the first login.

   Run `crmjuridico.viewConsola.Main` from your IDE (not yet wired to a Maven goal).

2. **Launch the GUI:**

   ```bash
   ./mvnw javafx:run
   ```

   The database path is relative (`database/crm.db`), so always run from the project root.

## Project structure

```
src/main/java/crmjuridico/
├── auxiliares/   helpers: SHA-256 hash, console input, console menus
├── controller/   GestaoEscritorio – business logic (singleton)
├── database/     SQLite connection, schema creation, DAOs
├── model/        entities (users, clients, cases, tasks, agenda) and enums
├── viewConsola/  console front end
└── viewGUI/      JavaFX front end (controllers; FXML in src/main/resources)
```

## Tech stack

Java 21 · JavaFX 21 · SQLite (`sqlite-jdbc`) · Maven · JUnit 5 (declared, no tests yet)
