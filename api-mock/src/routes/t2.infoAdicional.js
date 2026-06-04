const express = require('express');
const router = express.Router();

// =============================================================
// T2 - Informacion del cliente adicional
// GET /api/v1/info-adicional/:cedula
// Datos del tarjetahabiente adicional (si aplica).
// =============================================================
router.get('/info-adicional/:cedula', (req, res) => {
  const { cedula } = req.params;

  if (!cedula || cedula.length < 6) {
    return res.status(400).json({ error: 'cedula invalida' });
  }

  return res.status(200).json({
    cedula,
    nombre: 'Adicional Demo',
    parentesco: 'CONYUGE',
    elegible: true
  });
});

module.exports = router;
