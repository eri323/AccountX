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

Opciones:

```bash
./gradlew test -Ptags="smoke"              # solo los happy paths (6)
./gradlew test -Ptags="T6 & negativo"      # expresiones de tags de JUnit 5, sin la @
./gradlew test -Denvironment=qa            # URL del ambiente 'qa' en serenity.conf
./gradlew test -Drestapi.baseUrl=http://localhost:8081   # URL explícita
```

Tags disponibles: `@T0`…`@T6`, `@E2E`, `@smoke`, `@negativo`, `@seguridad`.
`-Dcucumber.filter.tags` no funciona con este runner: dentro del `@Suite` de JUnit 5, Cucumber
lo ignora y corre todo. Por eso se filtra con `-Ptags`.

### 3. Ver el reporte

Abrir: `automation/target/site/serenity/index.html`

---

## Cobertura de pruebas

28 escenarios en los 7 servicios.

| Servicio | Escenarios |
|---|---|
| **T0** — Productos disponibles | Catálogo del cliente (200, 3 productos) |
| **T1** — Información del cliente | Cliente existente (200) · inexistente (404) |
| **T2** — Información del adicional | Valores límite de la cédula: 5 dígitos (400) · 6 (200) · 10 (200) |
| **T3** — Solicitud principal | Happy path (201) · ingreso −1 / 0 / 1 (límite) · sin tipoTarjeta · sin ingreso · sin clienteId (400) |
| **T4** — Solicitud adicional | Happy path (201) · sin cédula / sin solicitud (400) · solicitud inexistente (404) |
| **T5** — Pre-scoring | Aprobado / Rechazado / Análisis manual / cliente desconocido → Rechazado (valor seguro) · sin clienteId (400) |
| **T6** — Crear oportunidad | Aprobado (201) · rechazado y análisis manual (409) · **resultado falsificado en el body (409)** · cliente sin pre-scoring (409) · solicitud inexistente (404) |

Técnicas de diseño aplicadas: **partición de equivalencia**, **valores límite** y **casos positivos/negativos**, priorizando por **riesgo de negocio**.

### La regla que más importa: T6

Solo se crea oportunidad si el pre-scoring fue `APROBADO_PRELIMINAR`. El mock decide con el
pre-scoring que **él mismo registró** en T5 (estado en memoria, `api-mock/src/store.js`) y no con
un campo que manda el cliente. El escenario `@seguridad` lo prueba: un cliente rechazado declara
`"resultadoPrescoring": "APROBADO_PRELIMINAR"` en el body y el servicio igual responde 409.

Las pruebas negativas de T6 se validaron contra la versión anterior del mock, la que confiaba en
el body: las 5 fallan, que es lo esperado. No solo pasan contra el mock corregido.

### Decisiones del framework

- **Actores en `OnStage`/`Cast`** (`stepdefinitions/Hooks.java`): un escenario puede combinar pasos
  de varias clases y todos usan el mismo actor, que ya tiene la habilidad de llamar al API.
- **El flujo E2E es una Task** (`CompletarFlujoDeSolicitud`) que verifica cada paso intermedio. Si
  falla T5 o T3, el escenario se cae ahí y el mensaje dice cuál paso fue.
- **Los parámetros del Gherkin son los que se envían**: el `clienteId` sale de `Dado el cliente "..."`,
  no de un valor fijo en el código.

---

## Autor

**Erick Raúl Gutiérrez Téllez** — QA Analyst
