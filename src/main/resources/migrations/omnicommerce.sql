CREATE SCHEMA IF NOT EXISTS omnicommerce;

CREATE TABLE IF NOT EXISTS omnicommerce.users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS omnicommerce.roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS omnicommerce.permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS omnicommerce.users_roles (
    user_id BIGSERIAL,
    role_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS omnicommerce.roles_permissions (
    role_id BIGSERIAL,
    permission_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS omnicommerce.role_hierarchies (
    id BIGSERIAL,
    child_id BIGSERIAL
);

ALTER TABLE omnicommerce.users_roles
DROP CONSTRAINT IF EXISTS users_roles_pkey;
ALTER TABLE omnicommerce.users_roles
ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE omnicommerce.roles_permissions
DROP CONSTRAINT IF EXISTS roles_permissions_pkey;
ALTER TABLE omnicommerce.roles_permissions
ADD CONSTRAINT roles_permissions_pkey PRIMARY KEY (role_id, permission_id);

ALTER TABLE omnicommerce.role_hierarchies
DROP CONSTRAINT IF EXISTS role_hierarchies_pkey;
ALTER TABLE omnicommerce.role_hierarchies
ADD CONSTRAINT role_hierarchies_pkey PRIMARY KEY (id, child_id);
