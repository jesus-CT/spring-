CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         email VARCHAR(150) UNIQUE NOT NULL
);


INSERT INTO usuario (nombre, email) VALUES
                                        ('Juan Pérez',      'juan.perez@example.com'),
                                        ('María García',    'maria.garcia@example.com'),
                                        ('Carlos Rodríguez','carlos.rodriguez@example.com'),
                                        ('Ana López',       'ana.lopez@example.com'),
                                        ('Luis Fernández',  'luis.fernandez@example.com');