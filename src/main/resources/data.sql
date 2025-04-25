-- -------------------------------------------------
-- data.sql: seeders para Gestión Citas Médicas
-- -------------------------------------------------

-- 1) Usuarios (JOINED inheritance con discriminator tipo_usuario)
INSERT INTO usuario
(id, tipo_usuario, nombre, apellidos, usuario, clave)
VALUES
    (1, 'MEDICO',   'María', 'García',  'mgarcia',   'clave123'),   -- @DiscriminatorValue("MEDICO") :contentReference[oaicite:0]{index=0}&#8203;:contentReference[oaicite:1]{index=1}
    (2, 'PACIENTE', 'Ana',   'Pérez',   'ana.perez', 'pwd123'),      -- @DiscriminatorValue("PACIENTE") :contentReference[oaicite:2]{index=2}&#8203;:contentReference[oaicite:3]{index=3}
    (3, 'PACIENTE', 'Juan',  'López',   'juan.lopez','pwd456');

-- 2) Pacientes (PK=FK → usuario.id)
INSERT INTO paciente
(id, nss,          num_tarjeta, telefono,     direccion)
VALUES
    (2, 'NSS12345678', 'TARJ1234',  '600123456', 'Calle Falsa 123'),
    (3, 'NSS87654321', 'TARJ5678',  '600654321', 'Av. Siempre Viva 742');

-- 3) Médicos (PK=FK → usuario.id)
INSERT INTO medico
(id, num_colegiado)
VALUES
    (1, 'MED2021001');

-- 4) Relación Paciente ↔ Médico (tabla paciente_medico)
INSERT INTO paciente_medico
(paciente_id, medico_id)
VALUES
    (2, 1),
    (3, 1);

-- 5) Citas (@JoinColumn paciente_id y medico_id en Cita.java) :contentReference[oaicite:4]{index=4}&#8203;:contentReference[oaicite:5]{index=5}
INSERT INTO cita
(id, fecha_hora,           motivo_cita,        attribute11, paciente_id, medico_id)
VALUES
    (1, '2025-05-10 10:30:00', 'Revisión general',  0,           2,           1),
    (2, '2025-05-11 11:00:00', 'Dolor de cabeza',   1,           3,           1);

-- Indicar la secuencia
SELECT setval(
               pg_get_serial_sequence('cita','id'),
               (SELECT MAX(id) FROM cita)
       );

-- 6) Diagnósticos (@JoinColumn cita_id en Diagnostico.java) :contentReference[oaicite:6]{index=6}&#8203;:contentReference[oaicite:7]{index=7}
INSERT INTO diagnostico
(id, valoracion_especialista, enfermedad, cita_id)
VALUES
    (1, 'Todo en orden',   'Ninguna',   1),
    (2, 'Requiere reposo', 'Migraña',   2);
