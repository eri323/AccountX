const express = require('express');
const router = express.Router();

// =============================================================
// T6 - Crear oportunidad en el sistema de casos (CRM)
// POST /api/v1/crear-oportunidad
// Solo deberia invocarse cuando el pre-scoring fue aprobado.
// =============================================================
router.post('/crear-oportunidad', (req, res) => {
  const { solicitudId, resultadoPrescoring } = req.body;

  if (!solicitudId) {
    return res.status(400).json({ error: 'solicitudId es obligatorio' });
  }

  // Regla de negocio: solo se crea oportunidad si fue aprobado.
  if (resultadoPrescoring && resultadoPrescoring !== 'APROBADO_PRELIMINAR') {
    return res.status(409).json({
      error: 'No se crea oportunidad: el pre-scoring no fue aprobado',
      resultadoPrescoring
    });
  }

  const idCaso = 'OP-' + Math.floor(10000 + Math.random() * 90000);

  return res.status(201).json({
    idCaso,
    solicitudId,
    estado: 'OPORTUNIDAD_CREADA'
  });
});

module.exports = router;
