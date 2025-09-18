-- Crear tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    salario_base NUMERIC(15,2) NOT NULL,
    id_rol UUID NOT NULL,
    CONSTRAINT fk_rol FOREIGN KEY (id_rol) REFERENCES roles(id)
);

-- Insertar roles
INSERT INTO roles (id, name, description) VALUES
('f12cb23a-d56e-4678-89a1-bc123def4567', 'ROL_ADMIN', 'Administrador del sistema'),
('a1b2c3d4-5e6f-4788-8911-abc456def789', 'ROL_ASESOR', 'Asesor comercial'),
('b9c8d7e6-5a4b-3c2d-1e0f-fedcba987654', 'ROL_CLIENTE', 'Usuario cliente')
ON CONFLICT (id) DO NOTHING;

-- Insertar usuario admin de prueba
INSERT INTO usuarios (
    id, nombres, apellidos, numero_documento, fecha_nacimiento, direccion,
    telefono, correo, contrasena, salario_base, id_rol
) VALUES (
    'd6e6d3d7-9d37-44f6-acbd-764d6f751a22',
    'Leidy',
    'Gómez',
    '1234567890',
    '1995-09-04',
    'Calle 123 #45-67',
    '3001234567',
    'leidy@example.com',
    -- Contraseña encriptada con BCrypt ("123456" por ejemplo)
    '$2a$10$lzH4Y.jOH.DgeguB7zjvRuv3xe.Qn4mQNbryd9GdaHx9Wg/SAdcPO',
    3500000.00,
    'f12cb23a-d56e-4678-89a1-bc123def4567'
)
ON CONFLICT (id) DO NOTHING;
