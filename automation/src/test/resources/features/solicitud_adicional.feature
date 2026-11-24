# language: es
@T4
Característica: Grabar solicitud de tarjeta adicional (T4)

  La solicitud adicional siempre cuelga de una solicitud principal ya registrada en T3.

  Antecedentes:
    Dado el cliente "1001"

  @smoke
  Escenario: Se registra un adicional sobre una solicitud principal existente
    Dado tiene una solicitud principal registrada
    Cuando registra un adicional con cedula "1087654321"
    Entonces el servicio responde con codigo 201
    Y el campo "status" de la respuesta es "OK"

  @negativo
  Escenario: Adicional sin cedula devuelve error
    Dado tiene una solicitud principal registrada
    Cuando registra un adicional sin cedula
    Entonces el servicio responde con codigo 400

  @negativo
  Escenario: Adicional sin solicitud principal devuelve error
    Cuando registra un adicional con cedula "1087654321" sin solicitud principal
    Entonces el servicio responde con codigo 400

  @negativo
  Escenario: Adicional sobre una solicitud que no existe devuelve 404
    Cuando registra un adicional con cedula "1087654321" para la solicitud "SOL-NOEXISTE"
    Entonces el servicio responde con codigo 404
