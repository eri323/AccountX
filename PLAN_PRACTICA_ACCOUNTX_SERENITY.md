# Plan de Práctica — Automatización Account X con Serenity BDD
**Autor:** Erick Raúl Gutiérrez Téllez  
**Objetivo:** Construir un proyecto real, ejecutable y publicable en GitHub que replique el flujo completo de solicitud de tarjetas de Account X, con API simulada local y automatización con Serenity BDD + Screenplay.

---

## Visión general del proyecto

```
accountx-qa-practice/
├── api-mock/          ← Servidor local Node.js que simula T0–T6
├── automation/        ← Proyecto Serenity BDD + Screenplay
├── performance/       ← Scripts JMeter para T5 y T6
├── docs/              ← Evidencias, reportes, decisiones
└── README.md          ← Descripción del proyecto para GitHub
```

---

## FASE 1 — API Mock local (Semana 1)

### Objetivo
Tener un servidor corriendo en `http://localhost:8080` que simule los 7 servicios del reto (T0–T6) para que la automatización tenga contra qué ejecutarse sin depender de ambientes reales.

### Stack recomendado
- **Node.js + Express** (simple, sin necesidad de Java)
- Respuestas hardcodeadas en JSON
- Sin base de datos en la primera versión

### Estructura del proyecto API

```
api-mock/
├── src/
│   ├── routes/
│   │   ├── t0.productos.js
│   │   ├── t1.infoCliente.js
│   │   ├── t2.infoAdicional.js
│   │   ├── t3.solicitudPrincipal.js
│   │   ├── t4.solicitudAdicional.js
│   │   ├── t5.prescoring.js
│   │   └── t6.crearOportunidad.js
│   └── app.js
├── package.json
└── README.md
```

### Endpoints a implementar

| Servicio | Método | Endpoint | Respuesta simulada |
|---|---|---|---|
| T0 | GET | `/api/v1/productos-no-disponibles/:clienteId` | Lista de tarjetas disponibles |
| T1 | GET | `/api/v1/info-cliente/:clienteId` | Datos personales del cliente |
| T2 | GET | `/api/v1/info-adicional/:cedula` | Datos del cliente adicional |
| T3 | POST | `/api/v1/solicitud-principal` | `{ solicitudId: "abc123" }` |
| T4 | POST | `/api/v1/solicitud-adicional` | `{ status: "OK" }` |
| T5 | POST | `/api/v1/prescoring` | `{ resultado: "APROBADO_PRELIMINAR" }` |
| T6 | POST | `/api/v1/crear-oportunidad` | `{ idCaso: "OP12345" }` |

### Lógica mínima a incluir en T5
Para que los casos negativos funcionen, T5 debe responder diferente según el `clienteId`:

```javascript
// t5.prescoring.js — lógica de simulación
const resultados = {
  "1001": "APROBADO_PRELIMINAR",
  "1002": "RECHAZADO",
  "1003": "REQUIERE_ANALISIS_MANUAL"
};

router.post('/prescoring', (req, res) => {
  const { clienteId } = req.body;
  const resultado = resultados[clienteId] || "RECHAZADO";
  res.status(200).json({ resultado });
});
```

### Entregable Fase 1
- Servidor corriendo con `node src/app.js`
- Todos los endpoints responden correctamente verificado con Postman
- Colección de Postman exportada y guardada en `/docs/postman/`

---

## FASE 2 — Proyecto Serenity BDD + Screenplay (Semanas 2 y 3)

### Objetivo
Automatizar los casos de prueba del reto técnico contra la API mock local, usando la arquitectura Screenplay con Serenity BDD.

### Stack
- **Java 11+**
- **Gradle** (build tool)
- **Serenity BDD + Cucumber**
- **Screenplay Pattern**
- **RestAssured** (para pruebas de API)

### Estructura del proyecto de automatización

```
automation/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/accountx/qa/
│       │       ├── features/          ← Runners de Cucumber
│       │       │   ├── SolicitudRunner.java
│       │       │   └── PrescoringRunner.java
│       │       ├── stepdefinitions/   ← Traducción Gherkin → código
│       │       │   ├── SolicitudSteps.java
│       │       │   └── PrescoringSteps.java
│       │       ├── tasks/             ← Acciones reutilizables (Screenplay)
│       │       │   ├── EnviarSolicitudPrincipal.java
│       │       │   ├── ValidarPrescoring.java
│       │       │   └── CrearOportunidad.java
│       │       ├── questions/         ← Verificaciones (Screenplay)
│       │       │   ├── ElSolicitudId.java
│       │       │   └── ElResultadoPrescoring.java
│       │       └── model/             ← POJOs de request/response
│       │           ├── SolicitudRequest.java
│       │           └── PrescoringResponse.java
│       └── resources/
│           ├── features/              ← Archivos .feature (Gherkin)
│           │   ├── solicitud_principal.feature
│           │   ├── prescoring.feature
│           │   └── crear_oportunidad.feature
│           ├── data/
│           │   └── clientes.csv       ← Datos de prueba parametrizados
│           └── serenity.conf          ← Configuración del proyecto
├── build.gradle
└── README.md
```

### Casos de prueba a automatizar (por prioridad)

#### Prioridad 1 — T3: Grabar solicitud principal
```gherkin
# features/solicitud_principal.feature

Feature: Grabar solicitud de tarjeta principal (T3)

  Scenario: Solicitud principal exitosa con datos válidos
    Given un cliente con ID "1001" autenticado
    When envía una solicitud con tipoTarjeta "T001" e ingreso 2500000
    Then el servicio responde con código 201
    And la respuesta contiene un solicitudId no vacío

  Scenario: Solicitud sin tipo de tarjeta devuelve error
    Given un cliente con ID "1001" autenticado
    When envía una solicitud sin tipoTarjeta
    Then el servicio responde con código 400

  Scenario: Solicitud con ingreso cero devuelve error
    Given un cliente con ID "1001" autenticado
    When envía una solicitud con ingreso 0
    Then el servicio responde con código 400
```

#### Prioridad 2 — T5: Validar Prescoring
```gherkin
# features/prescoring.feature

Feature: Validar Prescoring (T5)

  Scenario: Cliente aprobado preliminarmente
    Given el cliente "1001" solicita la tarjeta "T001"
    When se ejecuta el prescoring
    Then el resultado es "APROBADO_PRELIMINAR"

  Scenario: Cliente rechazado
    Given el cliente "1002" solicita la tarjeta "T001"
    When se ejecuta el prescoring
    Then el resultado es "RECHAZADO"

  Scenario: Cliente requiere análisis manual
    Given el cliente "1003" solicita la tarjeta "T001"
    When se ejecuta el prescoring
    Then el resultado es "REQUIERE_ANALISIS_MANUAL"
```

#### Prioridad 3 — T6: Crear Oportunidad
```gherkin
# features/crear_oportunidad.feature

Feature: Crear oportunidad en CRM (T6)

  Scenario: Oportunidad creada correctamente tras aprobación
    Given una solicitud aprobada con ID "abc123" para el cliente "1001"
    When se crea la oportunidad
    Then el servicio responde con código 201
    And la respuesta contiene un idCaso no vacío
```

#### Prioridad 4 — Flujo E2E completo (Happy Path)
```gherkin
Feature: Flujo completo de solicitud de tarjeta

  Scenario: Cliente solicita tarjeta principal y se crea oportunidad
    Given un cliente con ID "1001" autenticado en Account X
    When completa el formulario con datos válidos
    And el prescoring devuelve "APROBADO_PRELIMINAR"
    And se graba la solicitud principal
    Then se crea la oportunidad en CRM
    And el idCaso es válido y no está vacío
```

### Ejemplo de implementación Screenplay

#### Task: EnviarSolicitudPrincipal.java
```java
public class EnviarSolicitudPrincipal implements Task {

    private final String clienteId;
    private final String tipoTarjeta;
    private final int ingreso;

    // Constructor y factory method
    public static EnviarSolicitudPrincipal con(String clienteId, 
                                                String tipoTarjeta, 
                                                int ingreso) {
        return new EnviarSolicitudPrincipal(clienteId, tipoTarjeta, ingreso);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Post.to("/api/v1/solicitud-principal")
                .with(request -> request.body(
                    new SolicitudRequest(clienteId, tipoTarjeta, ingreso)
                ))
        );
    }
}
```

#### Question: ElSolicitudId.java
```java
public class ElSolicitudId implements Question<String> {

    public static ElSolicitudId delUltimoResponse() {
        return new ElSolicitudId();
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse()
                           .jsonPath()
                           .getString("solicitudId");
    }
}
```

#### StepDefinition: SolicitudSteps.java
```java
public class SolicitudSteps {

    Actor cliente = Actor.named("Cliente bancario");

    @Given("un cliente con ID {string} autenticado")
    public void clienteAutenticado(String clienteId) {
        cliente.whoCan(CallAnApi.at("http://localhost:8080"));
    }

    @When("envía una solicitud con tipoTarjeta {string} e ingreso {int}")
    public void enviaSolicitud(String tipoTarjeta, int ingreso) {
        cliente.attemptsTo(
            EnviarSolicitudPrincipal.con("1001", tipoTarjeta, ingreso)
        );
    }

    @Then("el servicio responde con código {int}")
    public void verificaCodigo(int codigoEsperado) {
        assertThat(SerenityRest.lastResponse().statusCode())
            .isEqualTo(codigoEsperado);
    }

    @Then("la respuesta contiene un solicitudId no vacío")
    public void verificaSolicitudId() {
        cliente.should(
            seeThat(ElSolicitudId.delUltimoResponse(), not(emptyOrNullString()))
        );
    }
}
```

### Datos de prueba: clientes.csv
```csv
clienteId,tipoTarjeta,ingreso,direccion,celular,resultadoEsperado
1001,T001,2500000,Calle 123,3011234567,APROBADO_PRELIMINAR
1002,T001,1500000,Calle 456,3029876543,RECHAZADO
1003,T002,3000000,Calle 789,3041112233,REQUIERE_ANALISIS_MANUAL
```

### Configuración: serenity.conf
```hocon
serenity {
  project.name = "Account X QA Practice"
  tag.failures.with = "issue"
}

restapi {
  baseUrl = "http://localhost:8080"
}
```

### Entregables Fase 2
- Proyecto ejecutable con `gradle clean test`
- Reporte Serenity generado en `/automation/target/site/serenity/`
- Todos los escenarios de prioridad 1 y 2 pasando (verde)
- README con instrucciones de cómo correr el proyecto

---

## FASE 3 — Pruebas de Performance con JMeter (Semana 4)

### Objetivo
Replicar el plan de carga diseñado en el reto técnico contra la API mock local.

### Estructura

```
performance/
├── plans/
│   ├── T5_prescoring_load_test.jmx
│   └── T6_crear_oportunidad_load_test.jmx
├── data/
│   └── clientes_carga.csv
└── reports/
    └── resultados_primer_run.html
```

### Escenarios a ejecutar

| Prueba | Usuarios | Duración | Objetivo |
|---|---|---|---|
| Carga T5 | 50 → 200 | 15 min | Comportamiento normal |
| Estrés T5 | 200 → 500 | 20 min | Punto de quiebre |
| Carga T6 | 50 → 150 | 15 min | Comportamiento normal |

### KPIs a capturar
- Tiempo de respuesta p95 (objetivo: < 3 segundos)
- Throughput (req/seg)
- Tasa de error (objetivo: < 1%)
- Usuarios en el punto de quiebre

### Entregables Fase 3
- Archivos `.jmx` ejecutables
- Reporte HTML del primer run
- Documento de análisis: qué encontré, dónde falló, conclusión

---

## FASE 4 — CI/CD con GitHub Actions (Semana 5)

### Objetivo
Que los tests de Serenity corran automáticamente en cada push a GitHub.

### Archivo de pipeline

```yaml
# .github/workflows/qa-pipeline.yml

name: Account X QA Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  automation-tests:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout código
        uses: actions/checkout@v3

      - name: Configurar Java 11
        uses: actions/setup-java@v3
        with:
          java-version: '11'
          distribution: 'temurin'

      - name: Instalar Node.js para API mock
        uses: actions/setup-node@v3
        with:
          node-version: '18'

      - name: Iniciar API mock
        working-directory: api-mock
        run: |
          npm install
          node src/app.js &
          sleep 3

      - name: Ejecutar tests Serenity
        working-directory: automation
        run: gradle clean test

      - name: Publicar reporte Serenity
        uses: actions/upload-artifact@v3
        if: always()
        with:
          name: serenity-report
          path: automation/target/site/serenity/
```

### Entregables Fase 4
- Pipeline corriendo en GitHub Actions
- Badge de estado en el README (`passing` / `failing`)
- Reporte publicado como artefacto del pipeline

---

## Cronograma resumen

| Semana | Fase | Entregable clave |
|---|---|---|
| 1 | API Mock Node.js | Servidor con T0–T6 corriendo + colección Postman |
| 2 | Serenity BDD — T3 y T5 | Primeros casos pasando en verde |
| 3 | Serenity BDD — T6 y E2E | Flujo completo automatizado |
| 4 | Performance JMeter | Reportes de carga T5 y T6 |
| 5 | CI/CD GitHub Actions | Pipeline automático en cada push |

---

## README del proyecto (para GitHub)

```markdown
# Account X QA Practice Project

Proyecto de automatización de pruebas que replica el flujo de solicitud 
de tarjetas de crédito descrito en el reto técnico de Sofka (2026).

## Stack
- API Mock: Node.js + Express
- Automatización: Java 11 + Serenity BDD + Screenplay + Cucumber
- Performance: Apache JMeter
- CI/CD: GitHub Actions

## Cómo correr el proyecto

### 1. Iniciar API mock
cd api-mock
npm install
node src/app.js

### 2. Ejecutar tests
cd automation
gradle clean test

### 3. Ver reporte
Abrir: automation/target/site/serenity/index.html

## Cobertura de pruebas
- T3: Grabar solicitud principal (happy path + casos negativos)
- T5: Validar prescoring (3 resultados posibles)
- T6: Crear oportunidad
- E2E: Flujo completo
```

---

## Nota final — Por qué este proyecto importa

Cuando termines este proyecto puedes decir con evidencia:

> *"Tengo un proyecto en GitHub donde construí la API mock, automaticé el flujo completo con Serenity BDD usando Screenplay, ejecuté pruebas de carga con JMeter y configuré el pipeline de CI/CD con GitHub Actions. Acá está el link."*

Eso vale más que cualquier año de experiencia declarada sin respaldo.
