// =============================================================
// Estado en memoria compartido entre servicios.
//
// Por que existe: la regla "solo se crea oportunidad si el
// pre-scoring fue aprobado" la debe decidir el SERVIDOR con lo que
// el mismo registro, no con un campo que manda el cliente. Si T6
// confiara en el body, cualquiera podria saltarse el scoring.
// =============================================================
const prescorings = new Map(); // clienteId   -> resultado de T5
const solicitudes = new Map(); // solicitudId -> { clienteId, tipoTarjeta, ingreso }

module.exports = { prescorings, solicitudes };
