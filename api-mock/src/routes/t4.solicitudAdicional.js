const express = require('express');
const { solicitudes } = require('../store');
const router = express.Router();

// =============================================================
// T4 - Grabar solicitud adicional
// POST /api/v1/solicitud-adicional
// Se asocia a una solicitud principal YA registrada en T3.
//
//   sin solicitudId / cedulaAdicional -> 400
//   solicitud principal inexistente   -> 404
// =============================================================
router.post('/solicitud-adicional', (req, res) => {
  const { solicitudId, cedulaAdicional } = req.body;

  if (!solicitudId) {
    return res.status(400).json({ error: 'solicitudId es obligatorio' });
  }

  if (!cedulaAdicional) {
    return res.status(400).json({ error: 'cedulaAdicional es obligatoria' });
  }

  if (!solicitudes.has(solicitudId)) {
    return res.status(404).json({ error: `Solicitud principal ${solicitudId} no encontrada` });
  }

  return res.status(201).json({
    solicitudId,
    cedulaAdicional,
    status: 'OK'
  });
});

module.exports = router;
