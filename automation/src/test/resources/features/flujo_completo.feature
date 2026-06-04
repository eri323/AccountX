# language: es
Característica: Flujo completo de solicitud de tarjeta (E2E)

  Escenario: Cliente aprobado solicita tarjeta y se crea la oportunidad
    Dado un cliente aprobado con ID "1001"
    Cuando completa el flujo de solicitud de la tarjeta "T001" con ingreso 2500000
    Entonces se crea la oportunidad y el idCaso no esta vacio
