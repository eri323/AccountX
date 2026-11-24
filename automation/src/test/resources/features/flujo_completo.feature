# language: es
@E2E @T6
Característica: Flujo completo de solicitud de tarjeta y creación de oportunidad (T5 -> T3 -> T6)

  Regla de negocio: solo se crea la oportunidad si el pre-scoring fue APROBADO_PRELIMINAR.
  La decisión la toma el servidor con el pre-scoring que él mismo registró;
  lo que el cliente declare en el body no cuenta.

  @smoke
  Escenario: Cliente aprobado completa el flujo y se crea la oportunidad
    Dado el cliente "1001"
    Cuando completa el flujo de solicitud de la tarjeta "T001" con ingreso 2500000
    Entonces el servicio responde con codigo 201
    Y el campo "idCaso" de la respuesta no esta vacio
    Y el campo "estado" de la respuesta es "OPORTUNIDAD_CREADA"

  @negativo
  Esquema del escenario: No se crea oportunidad si el pre-scoring no fue aprobado
    Dado el cliente "<cliente>"
    Cuando completa el flujo de solicitud de la tarjeta "T001" con ingreso 2500000
    Entonces el servicio responde con codigo 409
    Y el campo "resultadoPrescoring" de la respuesta es "<resultado>"
    Y la respuesta no trae el campo "idCaso"

    Ejemplos:
      | cliente | resultado                |
      | 1002    | RECHAZADO                |
      | 1003    | REQUIERE_ANALISIS_MANUAL |

  @negativo @seguridad
  Escenario: Declarar un resultado falso en el body no salta el pre-scoring
    Dado el cliente "1002"
    Y se ejecuta el prescoring
    Y tiene una solicitud principal registrada
    Cuando solicita crear la oportunidad declarando el resultado "APROBADO_PRELIMINAR"
    Entonces el servicio responde con codigo 409
    Y el campo "resultadoPrescoring" de la respuesta es "RECHAZADO"
    Y la respuesta no trae el campo "idCaso"

  @negativo
  Escenario: No se crea oportunidad si el cliente nunca pasó por el pre-scoring
    Dado el cliente "9999"
    Y tiene una solicitud principal registrada sin pasar por el prescoring
    Cuando solicita crear la oportunidad
    Entonces el servicio responde con codigo 409
    Y la respuesta no trae el campo "idCaso"

  @negativo
  Escenario: No se crea oportunidad para una solicitud que no existe
    Dado el cliente "1001"
    Cuando solicita crear la oportunidad para la solicitud "SOL-NOEXISTE"
    Entonces el servicio responde con codigo 404
