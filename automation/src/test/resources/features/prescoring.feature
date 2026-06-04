# language: es
Característica: Validar Pre-scoring (T5)

  Escenario: Cliente aprobado preliminarmente
    Dado el cliente "1001" solicita la tarjeta "T001"
    Cuando se ejecuta el prescoring
    Entonces el resultado es "APROBADO_PRELIMINAR"

  Escenario: Cliente rechazado
    Dado el cliente "1002" solicita la tarjeta "T001"
    Cuando se ejecuta el prescoring
    Entonces el resultado es "RECHAZADO"

  Escenario: Cliente requiere analisis manual
    Dado el cliente "1003" solicita la tarjeta "T001"
    Cuando se ejecuta el prescoring
    Entonces el resultado es "REQUIERE_ANALISIS_MANUAL"
