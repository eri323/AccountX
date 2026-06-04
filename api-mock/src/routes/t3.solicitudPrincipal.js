const express = require('express');
const router = express.Router();

// =============================================================
// T3 - Grabar solicitud principal
// POST /api/v1/solicitud-principal
// Es el corazon del flujo. Valida la entrada y persiste.
//
// Reglas de validacion (de tu analisis de valores limite):
//   - tipoTarjeta es obligatorio  -> si falta: 400
//   - ingreso debe ser > 0        -> si es 0 o negativo: 400
// =============================================================
router.post('/solicitud-principal', (req, res) => {
  const { clienteId, tipoTarjeta, ingreso } = req.body;

  // --- Validaciones de entrada (casos negativos) ---
  if (!tipoTarjeta) {
    return res.status(400).json({ error: 'tipoTarjeta es obligatorio' });
  }

  if (ingreso === undefined || ingreso === null || Number(ingreso) <= 0) {
    return res.status(400).json({ error: 'ingreso debe ser mayor a 0' });
  }

  // --- Caso exitoso: se graba la solicitud ---
  const solicitudId = 'SOL-' + Math.random().toString(36).substring(2, 8).toUpperCase();

  return res.status(201).json({
    solicitudId,
    clienteId,
    tipoTarjeta,
    estado: 'REGISTRADA'
  });
});

module.exports = router;
