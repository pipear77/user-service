CREATE TABLE public.roles (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);
CREATE TABLE public.usuarios (
    id UUID PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(50) NOT NULL UNIQUE,
    fecha_nacimiento DATE,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    correo_electronico VARCHAR(150) NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    salario_base NUMERIC(15, 2) CHECK (salario_base >= 0),
    id_rol UUID REFERENCES public.roles(id)
);
