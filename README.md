# Account X — QA Automation Practice

Proyecto de automatización de pruebas que replica el flujo de **solicitud de tarjetas de crédito** descrito en un reto técnico de QA (servicios T0–T6: catálogo, datos del cliente, solicitud principal/adicional, pre-scoring y creación de oportunidad).

El objetivo es demostrar, con código ejecutable, cómo se diseña y automatiza un framework de pruebas de API moderno usando **Serenity BDD + el patrón Screenplay**, corriendo contra una **API mock local** que permite probar de forma determinista todas las ramas de negocio.

---

## Stack

| Capa | Tecnología |
|---|---|
| API Mock | Node.js + Express |
| Automatización | Java 17 + Serenity BDD + Screenplay + Cucumber (Gherkin) |
| Cliente HTTP | RestAssured (vía serenity-screenplay-rest) |
| Build | Gradle (wrapper incluido) |
| Reportes | Serenity HTML report |

---

## Arquitectura del framework

El proyecto separa responsabilidades en capas, cada una en su propio nivel de abstracción:

```
.feature (Gherkin)      →  QUÉ se prueba (lenguaje de negocio, legible por todos)
        ↓
Step Definitions        →  Pegamento DELGADO: solo orquesta al actor, sin lógica
        ↓
Tasks (Screenplay)      →  Acciones de negocio reutilizables (EnviarSolicitudPrincipal...)
        ↓
Interactions (Post/Get) →  Mecánica técnica HTTP
        ↓
Questions (Screenplay)  →  Leen el estado del sistema para validarlo
        ↓
Model (POJOs)           →  Contratos tipados de request/response
```

**Patrón Screenplay:** un *Actor* ("Cliente bancario") tiene *Abilities* (`CallAnApi`), ejecuta *Tasks* (`actor.attemptsTo(...)`) y hace *Questions* (`actor.should(seeThat(...))`). Cada Task y Question es una clase pequeña, de responsabilidad única, reutilizable y componible. Esto aplica principios SOLID al código de pruebas y evita los "god objects" típicos del Page Object Model.

### Estructura

```
.
├── api-mock/                       # Servidor Node que simula T0-T6
│   └── src/
│       ├── routes/                 # Un archivo por servicio (t0..t6)
│       └── app.js
└── automation/                     # Proyecto Serenity BDD
    └── src/test/
        ├── java/com/accountx/qa/
        │   ├── runners/            # Runner JUnit 5 + Cucumber
        │   ├── stepdefinitions/    # Pegamento Gherkin → código
        │   ├── tasks/              # Acciones Screenplay reutilizables
        │   ├── questions/          # Verificaciones Screenplay
        │   └── model/              # POJOs de request
        └── resources/
            ├── features/           # Escenarios .feature (Gherkin)
            └── serenity.conf       # Configuración por ambiente
```

---

## Cómo correr el proyecto

### 1. Iniciar la API mock

```bash
cd api-mock
npm install
npm start          # levanta http://localhost:8080
```

### 2. Ejecutar los tests de automatización

En otra terminal (con la API mock corriendo):

```bash
cd automation
./gradlew test     # en Windows: .\gradlew.bat test
```

> Requiere **Java 17**. El wrapper de Gradle se encarga del resto.
> Para apuntar a otro ambiente: `./gradlew test -Drestapi.baseUrl=https://qa.accountx.example.com`

### 3. Ver el reporte

Abrir: `automation/target/site/serenity/index.html`

---

## Cobertura de pruebas

| Servicio | Escenarios |
|---|---|
| **T3** — Grabar solicitud principal | Happy path (201) + sin tipoTarjeta (400) + ingreso cero (400) |
| **T5** — Pre-scoring | Aprobado / Rechazado / Requiere análisis manual (3 particiones de equivalencia) |
| **T6** — Crear oportunidad | Validada dentro del flujo E2E |
| **E2E** — Flujo completo | T5 → T3 → T6 encadenados reutilizando las mismas tasks |

Técnicas de diseño aplicadas: **partición de equivalencia**, **valores límite** y **casos positivos/negativos**, priorizando por **riesgo de negocio**.

---

## Autor

**Erick Raúl Gutiérrez Téllez** — QA Analyst
