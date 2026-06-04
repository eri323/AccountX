const express = require('express');

// --- Rutas: un archivo por servicio (T0 a T6) ---
const t0 = require('./routes/t0.productos');
const t1 = require('./routes/t1.infoCliente');
const t2 = require('./routes/t2.infoAdicional');
const t3 = require('./routes/t3.solicitudPrincipal');
const t4 = require('./routes/t4.solicitudAdicional');
const t5 = require('./routes/t5.prescoring');
const t6 = require('./routes/t6.crearOportunidad');

const app = express();

// Middleware: parsea el body JSON de las peticiones POST.
app.use(express.json());

// Todas las rutas cuelgan del prefijo comun /api/v1
app.use('/api/v1', t0);
app.use('/api/v1', t1);
app.use('/api/v1', t2);
app.use('/api/v1', t3);
app.use('/api/v1', t4);
app.use('/api/v1', t5);
app.use('/api/v1', t6);

// Endpoint de salud: util para verificar que el server esta arriba
// (lo usaras en el pipeline de CI antes de correr los tests).
app.get('/health', (req, res) => {
  res.status(200).json({ status: 'UP', servicio: 'accountx-api-mock' });
});

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API mock de Account X corriendo en http://localhost:${PORT}`);
  console.log('Servicios disponibles: T0-T6 bajo /api/v1');
});

module.exports = app;
