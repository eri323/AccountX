# language: es
@T3
Característica: Grabar solicitud de tarjeta principal (T3)

  Antecedentes:
    Dado el cliente "1001"

  @smoke
  Escenario: Solicitud principal exitosa con datos validos
    Cuando envia una solicitud con tipoTarjeta "T001" e ingreso 2500000
    Entonces el servicio responde con codigo 201
    Y el campo "solicitudId" de la respuesta no esta vacio
    Y el campo "clienteId" de la respuesta es "1001"
    Y el campo "estado" de la respuesta es "REGISTRADA"

  @negativo
  Esquema del escenario: Valores limite del ingreso
    Cuando envia una solicitud con tipoTarjeta "T001" e ingreso <ingreso>
    Entonces el servicio responde con codigo <codigo>

    Ejemplos:
      | ingreso | codigo |
      | -1      | 400    |
      | 0       | 400    |
      | 1       | 201    |

  @negativo
  Escenario: Solicitud sin tipo de tarjeta devuelve error
    Cuando envia una solicitud sin tipoTarjeta con ingreso 2500000
    Entonces el servicio responde con codigo 400
    Y el campo "error" de la respuesta es "tipoTarjeta es obligatorio"

  @negativo
  Escenario: Solicitud sin ingreso devuelve error
    Cuando envia una solicitud con tipoTarjeta "T001" sin ingreso
    Entonces el servicio responde con codigo 400
    Y el campo "error" de la respuesta es "ingreso debe ser mayor a 0"

  @negativo
  Escenario: Solicitud sin clienteId devuelve error
    Cuando envia una solicitud sin clienteId con tipoTarjeta "T001" e ingreso 2500000
    Entonces el servicio responde con codigo 400
    Y el campo "error" de la respuesta es "clienteId es obligatorio"
