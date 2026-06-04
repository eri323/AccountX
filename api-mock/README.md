# API Mock — Account X (T0–T6)

Servidor local en Node.js + Express que simula los 7 servicios del flujo de solicitud de tarjetas. Permite que la automatización corra de forma **determinista**, sin depender de ambientes reales.

## Correr

```bash
npm install
npm start        # http://localhost:8080
```

## Endpoints

| Servicio | Método | Endpoint |
|---|---|---|
| Health | GET | `/health` |
| T0 | GET | `/api/v1/productos-no-disponibles/:clienteId` |
| T1 | GET | `/api/v1/info-cliente/:clienteId` |
| T2 | GET | `/api/v1/info-adicional/:cedula` |
| T3 | POST | `/api/v1/solicitud-principal` |
| T4 | POST | `/api/v1/solicitud-adicional` |
| T5 | POST | `/api/v1/prescoring` |
| T6 | POST | `/api/v1/crear-oportunidad` |

## Lógica determinista clave

**T5 (pre-scoring)** responde según el `clienteId`, lo que permite probar las tres ramas de la tabla de decisión:

| clienteId | Resultado |
|---|---|
| `1001` | `APROBADO_PRELIMINAR` |
| `1002` | `RECHAZADO` |
| `1003` | `REQUIERE_ANALISIS_MANUAL` |

**T3** valida la entrada: `tipoTarjeta` obligatorio e `ingreso > 0` (responde `400` si no se cumple).

**T6** solo crea oportunidad si el pre-scoring fue `APROBADO_PRELIMINAR` (si no, responde `409`).
