const express = require('express');
const { solicitudes } = require('../store');
const router = express.Router();

// =============================================================
// T3 - Grabar solicitud principal
// POST /api/v1/solicitud-principal
// Es el corazon del flujo. Valida la entrada y persiste.
//
// Reglas de validacion (analisis de valores limite):
//   - clienteId es obligatorio    -> si falta: 400
//   - tipoTarjeta es obligatorio  -> si falta: 400
//   - ingreso debe ser > 0        -> 0, negativo o ausente: 400
//                                    (1 es el limite valido)
// =============================================================
router.post('/solicitud-principal', (req, res) => {
  const { clienteId, tipoTarjeta, ingreso } = req.body;

  // --- Validaciones de entrada (casos negativos) ---
  if (!clienteId) {
    return res.status(400).json({ error: 'clienteId es obligatorio' });
  }

  if (!tipoTarjeta) {
    return res.status(400).json({ error: 'tipoTarjeta es obligatorio' });
  }

  if (ingreso === undefined || ingreso === null || Number(ingreso) <= 0) {
    return res.status(400).json({ error: 'ingreso debe ser mayor a 0' });
  }

  // --- Caso exitoso: se graba la solicitud ---
  const solicitudId = 'SOL-' + Math.random().toString(36).substring(2, 8).toUpperCase();
  solicitudes.set(solicitudId, { clienteId: String(clienteId), tipoTarjeta, ingreso: Number(ingreso) });

  return res.status(201).json({
    solicitudId,
    clienteId,
    tipoTarjeta,
    estado: 'REGISTRADA'
  });
});

module.exports = router;
