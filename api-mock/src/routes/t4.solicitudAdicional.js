const express = require('express');
const router = express.Router();

// =============================================================
// T4 - Grabar solicitud adicional
// POST /api/v1/solicitud-adicional
// Se asocia a una solicitud principal ya registrada.
// =============================================================
router.post('/solicitud-adicional', (req, res) => {
  const { solicitudId, cedulaAdicional } = req.body;

  if (!solicitudId) {
    return res.status(400).json({ error: 'solicitudId es obligatorio' });
  }

  if (!cedulaAdicional) {
    return res.status(400).json({ error: 'cedulaAdicional es obligatoria' });
  }

  return res.status(201).json({
    solicitudId,
    cedulaAdicional,
    status: 'OK'
  });
});

module.exports = router;
