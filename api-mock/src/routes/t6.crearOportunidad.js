const express = require('express');
const { prescorings, solicitudes } = require('../store');
const router = express.Router();

// =============================================================
// T6 - Crear oportunidad en el sistema de casos (CRM)
// POST /api/v1/crear-oportunidad
//
// Regla de negocio: solo se crea oportunidad si el pre-scoring
// (T5) del cliente dueno de la solicitud fue APROBADO_PRELIMINAR.
// El resultado se busca en el estado del servidor; cualquier
// "resultadoPrescoring" que venga en el body se ignora.
//
//   sin solicitudId            -> 400
//   solicitud inexistente      -> 404
//   cliente sin pre-scoring    -> 409
//   pre-scoring no aprobado    -> 409
//   aprobado                   -> 201 + idCaso
// =============================================================
router.post('/crear-oportunidad', (req, res) => {
  const { solicitudId } = req.body;

  if (!solicitudId) {
    return res.status(400).json({ error: 'solicitudId es obligatorio' });
  }

  const solicitud = solicitudes.get(solicitudId);
  if (!solicitud) {
    return res.status(404).json({ error: `Solicitud ${solicitudId} no encontrada` });
  }

  const resultadoPrescoring = prescorings.get(solicitud.clienteId);
  if (!resultadoPrescoring) {
    return res.status(409).json({
      error: 'No se crea oportunidad: el cliente no tiene pre-scoring',
      clienteId: solicitud.clienteId
    });
  }

  if (resultadoPrescoring !== 'APROBADO_PRELIMINAR') {
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
