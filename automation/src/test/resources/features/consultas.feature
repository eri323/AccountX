# language: es
Característica: Servicios de consulta del cliente (T0, T1, T2)

  @T0 @smoke
  Escenario: El cliente consulta las tarjetas que puede solicitar
    Dado el cliente "1001"
    Cuando consulta sus productos disponibles
    Entonces el servicio responde con codigo 200
    Y el campo "productos.size()" de la respuesta es "3"
    Y el campo "productos[0].tipoTarjeta" de la respuesta es "T001"

  @T1 @smoke
  Escenario: Se autocompletan los datos de un cliente existente
    Dado el cliente "1001"
    Cuando consulta su informacion personal
    Entonces el servicio responde con codigo 200
    Y el campo "nombre" de la respuesta no esta vacio
    Y el campo "cedula" de la respuesta no esta vacio

  @T1 @negativo
  Escenario: Cliente inexistente devuelve 404
    Dado el cliente "9999"
    Cuando consulta su informacion personal
    Entonces el servicio responde con codigo 404

  @T2
  Esquema del escenario: Valores limite de la cedula del adicional
    Dado el cliente "1001"
    Cuando consulta la informacion del adicional con cedula "<cedula>"
    Entonces el servicio responde con codigo <codigo>

    Ejemplos:
      | cedula     | codigo |
      | 12345      | 400    |
      | 123456     | 200    |
      | 1098765432 | 200    |
