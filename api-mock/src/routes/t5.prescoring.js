const express = require('express');
const router = express.Router();

// =============================================================
// T5 - Validar pre-scoring
// POST /api/v1/prescoring
// Servicio de mayor riesgo del flujo (decide aprobacion).
//
// Logica de simulacion DETERMINISTA por clienteId: esto es lo
// que permite probar las 3 ramas de tu tabla de decision sin
// depender de un motor de scoring real.
//   1001 -> APROBADO_PRELIMINAR
//   1002 -> RECHAZADO
//   1003 -> REQUIERE_ANALISIS_MANUAL
//   otro -> RECHAZADO (valor por defecto seguro)
// =============================================================
const resultados = {
  '1001': 'APROBADO_PRELIMINAR',
  '1002': 'RECHAZADO',
  '1003': 'REQUIERE_ANALISIS_MANUAL'
};

router.post('/prescoring', (req, res) => {
  const { clienteId } = req.body;

  if (!clienteId) {
    return res.status(400).json({ error: 'clienteId es obligatorio' });
  }

  const resultado = resultados[clienteId] || 'RECHAZADO';

  return res.status(200).json({ clienteId, resultado });
});

module.exports = router;
