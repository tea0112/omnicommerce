CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS resources (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGSERIAL,
    role_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS roles_permissions (
    role_id BIGSERIAL,
    permission_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS permissions_resources (
    permission_id BIGSERIAL,
    resource_id BIGSERIAL
);

CREATE TABLE IF NOT EXISTS role_hierarchies (
    id BIGSERIAL,
    child_id BIGSERIAL
);

ALTER TABLE users_roles
DROP CONSTRAINT IF EXISTS users_roles_pkey;
ALTER TABLE users_roles
ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE roles_permissions
DROP CONSTRAINT IF EXISTS roles_permissions_pkey;
ALTER TABLE roles_permissions
ADD CONSTRAINT roles_permissions_pkey PRIMARY KEY (role_id, permission_id);

ALTER TABLE permissions_resources
DROP CONSTRAINT IF EXISTS permissions_resources_pkey;
ALTER TABLE permissions_resources
ADD CONSTRAINT permissions_resources_pkey PRIMARY KEY (permission_id, resource_id);

ALTER TABLE role_hierarchies
DROP CONSTRAINT IF EXISTS role_hierarchies_pkey;
ALTER TABLE role_hierarchies
ADD CONSTRAINT role_hierarchies_pkey PRIMARY KEY (id, child_id);
