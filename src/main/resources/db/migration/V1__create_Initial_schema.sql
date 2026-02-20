CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id          UUID         PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TYPE account_type AS ENUM (
    'CREDIT_CARD',  -- Cartão de crédito
    'DEBIT',        -- Cartão de débito
    'PIX',          -- PIX
    'CHECKING',     -- Conta corrente
    'SAVINGS'       -- Poupança
);

CREATE TABLE accounts (
    id          UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(100)  NOT NULL,
    type        account_type  NOT NULL,
    color       VARCHAR(7)    NOT NULL DEFAULT '#6366f1',
    balance     DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE TYPE transaction_type AS ENUM (
    'INCOME',
    'EXPENSE'
);

CREATE TABLE categories (
    id          UUID             PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id     UUID             REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(100)     NOT NULL,
    type        transaction_type NOT NULL,
    icon        VARCHAR(50)      NOT NULL DEFAULT 'tag',
    color       VARCHAR(7)       NOT NULL DEFAULT '#6366f1'
);

CREATE TABLE transactions (
    id           UUID             PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id      UUID             NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id   UUID             NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    category_id  UUID             REFERENCES categories(id) ON DELETE SET NULL,
    description  VARCHAR(255)     NOT NULL,
    amount       DECIMAL(15,2)    NOT NULL CHECK (amount > 0),
    type         transaction_type NOT NULL,
    date         DATE             NOT NULL,
    notes        TEXT,
    created_at   TIMESTAMP        NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_transactions_user_date ON transactions(user_id, date);

CREATE TABLE subscriptions (
    id           UUID          PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id      UUID          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    account_id   UUID          NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
    category_id  UUID          REFERENCES categories(id) ON DELETE SET NULL,
    name         VARCHAR(100)  NOT NULL,
    amount       DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    billing_day  SMALLINT      NOT NULL CHECK (billing_day BETWEEN 1 AND 31),
    active       BOOLEAN       NOT NULL DEFAULT TRUE,
    start_date   DATE          NOT NULL DEFAULT CURRENT_DATE,
    notes        TEXT,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);

INSERT INTO categories (name, type, icon, color) VALUES
    -- Despesas
    ('Alimentação',      'EXPENSE', 'utensils',        '#ef4444'),
    ('Transporte',       'EXPENSE', 'car',              '#f97316'),
    ('Moradia',          'EXPENSE', 'home',             '#eab308'),
    ('Saúde',            'EXPENSE', 'heart-pulse',      '#84cc16'),
    ('Educação',         'EXPENSE', 'book',             '#06b6d4'),
    ('Lazer',            'EXPENSE', 'gamepad',          '#8b5cf6'),
    ('Roupas',           'EXPENSE', 'shirt',            '#ec4899'),
    ('Assinaturas',      'EXPENSE', 'repeat',           '#6366f1'),
    ('Outros gastos',    'EXPENSE', 'circle-ellipsis',  '#94a3b8'),
    -- Receitas
    ('Salário',          'INCOME',  'briefcase',        '#22c55e'),
    ('Freelance',        'INCOME',  'laptop',           '#10b981'),
    ('Investimentos',    'INCOME',  'trending-up',      '#14b8a6'),
    ('Presente',         'INCOME',  'gift',             '#a855f7'),
    ('Outras receitas',  'INCOME',  'circle-ellipsis',  '#94a3b8');