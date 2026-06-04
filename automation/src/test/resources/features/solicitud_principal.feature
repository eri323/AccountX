# language: es
Característica: Grabar solicitud de tarjeta principal (T3)

  Escenario: Solicitud principal exitosa con datos validos
    Dado un cliente con ID "1001" autenticado
    Cuando envia una solicitud con tipoTarjeta "T001" e ingreso 2500000
    Entonces el servicio responde con codigo 201
    Y la respuesta contiene un solicitudId no vacio

  Escenario: Solicitud sin tipo de tarjeta devuelve error
    Dado un cliente con ID "1001" autenticado
    Cuando envia una solicitud sin tipoTarjeta
    Entonces el servicio responde con codigo 400

  Escenario: Solicitud con ingreso cero devuelve error
    Dado un cliente con ID "1001" autenticado
    Cuando envia una solicitud con ingreso 0
    Entonces el servicio responde con codigo 400
