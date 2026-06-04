const express = require('express');
const router = express.Router();

// =============================================================
// T1 - Informacion del cliente principal
// GET /api/v1/info-cliente/:clienteId
// Datos personales que la pantalla autocompleta.
// =============================================================
const clientes = {
  '1001': { nombre: 'Erick Gutierrez', cedula: '1098765432', celular: '3011234567', ingresoReportado: 2500000 },
  '1002': { nombre: 'Laura Mendez', cedula: '1087654321', celular: '3029876543', ingresoReportado: 1500000 },
  '1003': { nombre: 'Carlos Rojas', cedula: '1076543210', celular: '3041112233', ingresoReportado: 3000000 }
};

router.get('/info-cliente/:clienteId', (req, res) => {
  const { clienteId } = req.params;
  const cliente = clientes[clienteId];

  if (!cliente) {
    return res.status(404).json({ error: `Cliente ${clienteId} no encontrado` });
  }

  return res.status(200).json({ clienteId, ...cliente });
});

module.exports = router;
