# language: es
@T5
Característica: Validar pre-scoring (T5)

  Cada cliente representa una partición de equivalencia del motor de scoring.
  Un cliente desconocido cae en RECHAZADO: el valor por defecto debe ser el seguro.

  Esquema del escenario: Resultado del pre-scoring por partición
    Dado el cliente "<cliente>"
    Cuando se ejecuta el prescoring
    Entonces el servicio responde con codigo 200
    Y el resultado del prescoring es "<resultado>"

    @smoke
    Ejemplos: Aprobado
      | cliente | resultado           |
      | 1001    | APROBADO_PRELIMINAR |

    Ejemplos: No aprobados
      | cliente | resultado                |
      | 1002    | RECHAZADO                |
      | 1003    | REQUIERE_ANALISIS_MANUAL |
      | 5555    | RECHAZADO                |

  @negativo
  Escenario: Pre-scoring sin clienteId devuelve error
    Dado el cliente "1001"
    Cuando se ejecuta el prescoring sin clienteId
    Entonces el servicio responde con codigo 400
