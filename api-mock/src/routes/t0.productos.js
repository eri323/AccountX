const express = require('express');
const router = express.Router();

// =============================================================
// T0 - Productos disponibles para el cliente
// GET /api/v1/productos-no-disponibles/:clienteId
// Devuelve las tarjetas que el cliente SI puede solicitar.
// =============================================================
router.get('/productos-no-disponibles/:clienteId', (req, res) => {
  const { clienteId } = req.params;

  // Cliente desconocido -> no hay catalogo de productos
  if (!clienteId) {
    return res.status(400).json({ error: 'clienteId es obligatorio' });
  }

  const productos = [
    { tipoTarjeta: 'T001', nombre: 'Mastercard Oro', cupoMaximo: 8000000 },
    { tipoTarjeta: 'T002', nombre: 'Visa Platino', cupoMaximo: 15000000 },
    { tipoTarjeta: 'T003', nombre: 'Mastercard Clasica', cupoMaximo: 4000000 }
  ];

  return res.status(200).json({ clienteId, productos });
});

module.exports = router;
