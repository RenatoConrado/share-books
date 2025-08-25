CREATE TABLE public.users (
    id         uuid         NOT NULL DEFAULT gen_random_uuid(),
    username   varchar(70)  NOT NULL UNIQUE,
    email      varchar(255) NOT NULL UNIQUE,
    password   varchar(255) NOT NULL,
    real_name  varchar(70),
    birthdate  timestamp,
    created_at timestamp             DEFAULT NOW(),
    updated_at timestamp             DEFAULT NOW(),
    is_locked  boolean      NOT NULL DEFAULT FALSE,
    is_active  boolean      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE public.roles (
    id   uuid        NOT NULL DEFAULT gen_random_uuid(),
    name varchar(20) NOT NULL UNIQUE,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

INSERT INTO roles (name) VALUES ('USER');

CREATE TABLE public.user_roles (
    user_id     uuid NOT NULL,
    role_id     uuid NOT NULL,
    assigned_at timestamp DEFAULT NOW(),
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE tokens (
    id           uuid         NOT NULL DEFAULT gen_random_uuid(),
    content      varchar(255) NOT NULL,
    created_at   timestamp,
    expires_at   timestamp,
    validated_at timestamp,
    user_id      uuid         NOT NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
