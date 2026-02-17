-- ============================================================
-- RCRC SEED DATABASE SCHEMA (POSTGRESQL)
-- Author: Naphok MOFE
-- Version: 0.2
-- ============================================================

-- ============================================================
-- Update 2026-Feb-3
-- Combine with view and data seed
-- ============================================================

-- ============================================================
-- Tables
-- ============================================================

-- Begining of commit
BEGIN;

-- Table: rice_variety
CREATE TABLE rice_variety (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    image_url TEXT,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: rice_generation
CREATE TABLE rice_generation (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: season
CREATE TABLE season (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: seed_condition
CREATE TABLE seed_condition (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: seed_batch
CREATE TABLE seed_batch (
    id BIGSERIAL PRIMARY KEY,
    variety_id INTEGER NOT NULL REFERENCES rice_variety(id) ON DELETE CASCADE,
    season_id INTEGER NOT NULL REFERENCES season(id),
    generation_id INTEGER NOT NULL REFERENCES rice_generation(id),

    year INTEGER NOT NULL CHECK (year >= 1900 AND year <= 2900),
    grading BOOLEAN NOT NULL,
    germination BOOLEAN NOT NULL,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    UNIQUE (variety_id, year, season_id, grading, generation_id, germination)
);

-- Table: app_user
CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,

    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: user_profile (shared PK with app_user's as one-to-one)
CREATE TABLE user_profile(
    user_id INTEGER PRIMARY KEY REFERENCES app_user(id) ON DELETE CASCADE,

    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(50),
    phone VARCHAR(20) NOT NULL,
    address TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: role
CREATE TABLE role (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: user_role (many-to-many relationship)
CREATE TABLE user_role (
    user_id INTEGER NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: supplier_type
CREATE TABLE supplier_type(
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: supplier
CREATE TABLE supplier (
    id SERIAL PRIMARY KEY,
    supplier_type_id INTEGER REFERENCES supplier_type(id),

    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(50),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    description TEXT, -- Example 'VIP'

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: customer_type
CREATE TABLE customer_type(
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: customer
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    customer_type_id INTEGER REFERENCES customer_type(id),

    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(50),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    description TEXT, -- Example 'VIP'

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: stock_transaction_type
CREATE TABLE stock_transaction_type (
    id SERIAL PRIMARY KEY,

    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: stock_transaction
CREATE TABLE stock_transaction(
    id BIGSERIAL PRIMARY KEY,
    transaction_type_id INTEGER NOT NULL REFERENCES stock_transaction_type(id),
    performed_by INTEGER NOT NULL REFERENCES app_user(id),

    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: stock_movement_type
CREATE TABLE stock_movement_type (
    id SERIAL PRIMARY KEY,
    
    name VARCHAR(50) UNIQUE NOT NULL,
    effect_on_stock VARCHAR(3) NOT NULL CHECK (effect_on_stock IN ('IN', 'OUT')),
    description TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: stock_movement
CREATE TABLE stock_movement (
    id BIGSERIAL PRIMARY KEY,
    movement_type_id BIGINT NOT NULL REFERENCES stock_movement_type(id),
    batch_id BIGINT NOT NULL REFERENCES seed_batch(id),
    transaction_id BIGINT NOT NULL REFERENCES stock_transaction(id),

    quantity NUMERIC(10, 2) NOT NULL,
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: sale (shared PK with stock_transaction's as one-to-one)
CREATE TABLE sale (
    stock_transaction_id BIGINT PRIMARY KEY REFERENCES stock_transaction(id),
    customer_id INTEGER REFERENCES customer(id),

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: (shared PK with stock_movement's as one-to-one)
CREATE TABLE sale_item (
    stock_movement_id BIGINT PRIMARY KEY REFERENCES stock_movement(id),
    sale_id BIGINT NOT NULL REFERENCES sale(stock_transaction_id),

    price NUMERIC(19, 4) NOT NULL CHECK (price >= 0),
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: purchase (shared PK with stock_transaction's as one-to-one)
CREATE TABLE purchase (
    stock_transaction_id BIGINT PRIMARY KEY REFERENCES stock_transaction(id),
    supplier_id INTEGER REFERENCES supplier(id),

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Table: purchase_item (shared PK with stock_movement's as one-to-one)
CREATE TABLE purchase_item (
    stock_movement_id BIGINT PRIMARY KEY REFERENCES stock_movement(id),
    purchase_id BIGINT NOT NULL REFERENCES purchase(stock_transaction_id),

    price NUMERIC(19, 4) NOT NULL CHECK (price >= 0),
    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- ============================================================
-- Indexes for performance (optional, but recommended)
-- ============================================================

-- Table: rice_variety
CREATE INDEX idx_rice_variety_created_at ON rice_variety(created_at);
CREATE INDEX idx_rice_variety_updated_at ON rice_variety(updated_at);
CREATE INDEX idx_rice_variety_deleted_at_null ON rice_variety (deleted_at) WHERE deleted_at IS NULL;

-- Table: rice_generation
CREATE INDEX idx_rice_generation_created_at ON rice_generation(created_at);
CREATE INDEX idx_rice_generation_updated_at ON rice_generation(updated_at);
CREATE INDEX idx_rice_generation_deleted_at_null ON rice_generation (deleted_at) WHERE deleted_at IS NULL;

-- Table: season
CREATE INDEX idx_season_created_at ON season(created_at);
CREATE INDEX idx_season_updated_at ON season(updated_at);
CREATE INDEX idx_season_deleted_at_null ON season (deleted_at) WHERE deleted_at IS NULL;

-- Table: seed_condition
CREATE INDEX idx_seed_condition_created_at ON seed_condition(created_at);
CREATE INDEX idx_seed_condition_updated_at ON seed_condition(updated_at);
CREATE INDEX idx_seed_condition_deleted_at_null ON seed_condition (deleted_at) WHERE deleted_at IS NULL;

-- Table: stock_movement_type
CREATE INDEX idx_stock_movement_type_created_at ON stock_movement_type(created_at);
CREATE INDEX idx_stock_movement_type_updated_at ON stock_movement_type(updated_at);
CREATE INDEX idx_stock_movement_type_deleted_at_null ON stock_movement_type (deleted_at) WHERE deleted_at IS NULL;

-- Table: role
CREATE INDEX idx_role_created_at ON role(created_at);
CREATE INDEX idx_role_updated_at ON role(updated_at);
CREATE INDEX idx_role_deleted_at_null ON role (deleted_at) WHERE deleted_at IS NULL;

-- Table: app_user
CREATE INDEX idx_app_user_created_at ON app_user(created_at);
CREATE INDEX idx_app_user_updated_at ON app_user(updated_at);
CREATE INDEX idx_app_user_deleted_at_null ON app_user (deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_user_username ON app_user(username);

-- Table: user_profile
CREATE INDEX idx_user_profile_created_at ON user_profile(created_at);
CREATE INDEX idx_user_profile_updated_at ON user_profile(updated_at);
CREATE INDEX idx_user_profile_deleted_at_null ON user_profile (deleted_at) WHERE deleted_at IS NULL;

-- Table: customer_type
CREATE INDEX idx_customer_type_created_at ON customer_type(created_at);
CREATE INDEX idx_customer_type_updated_at ON customer_type(updated_at);
CREATE INDEX idx_customer_type_deleted_at_null ON customer_type (deleted_at) WHERE deleted_at IS NULL;

-- Table: customer
CREATE INDEX idx_customer_created_at ON customer(created_at);
CREATE INDEX idx_customer_updated_at ON customer(updated_at);
CREATE INDEX idx_customer_deleted_at_null ON customer (deleted_at) WHERE deleted_at IS NULL;

-- Table: supplier_type
CREATE INDEX idx_supplier_type_created_at ON supplier_type(created_at);
CREATE INDEX idx_supplier_type_updated_at ON supplier_type(updated_at);
CREATE INDEX idx_supplier_type_deleted_at_null ON supplier_type (deleted_at) WHERE deleted_at IS NULL;

-- Table: supplier
CREATE INDEX idx_supplier_created_at ON supplier(created_at);
CREATE INDEX idx_supplier_updated_at ON supplier(updated_at);
CREATE INDEX idx_supplier_deleted_at_null ON supplier (deleted_at) WHERE deleted_at IS NULL;

-- Table: seed_batch
CREATE INDEX idx_seed_batch_variety_id ON seed_batch(variety_id);
CREATE INDEX idx_seed_batch_created_at ON seed_batch(created_at);
CREATE INDEX idx_seed_batch_updated_at ON seed_batch(updated_at);
CREATE INDEX idx_seed_batch_deleted_at_null ON seed_batch (deleted_at) WHERE deleted_at IS NULL;

-- Table: stock_transaction_type
CREATE INDEX idx_stock_transaction_type_created_at ON stock_transaction_type(created_at);
CREATE INDEX idx_stock_transaction_type_updated_at ON stock_transaction_type(updated_at);
CREATE INDEX idx_stock_transaction_type_deleted_at_null ON stock_transaction_type (deleted_at) WHERE deleted_at IS NULL;

-- Table: stock_transaction
CREATE INDEX idx_stock_transaction_performed_by ON stock_transaction(performed_by);
CREATE INDEX idx_stock_transaction_created_at ON stock_transaction(created_at);
CREATE INDEX idx_stock_transaction_updated_at ON stock_transaction(updated_at);
CREATE INDEX idx_stock_transaction_deleted_at_null ON stock_transaction (deleted_at) WHERE deleted_at IS NULL;

-- Table: stock_movement
CREATE INDEX idx_stock_movement_batch_id ON stock_movement(batch_id);
CREATE INDEX idx_stock_movement_transaction_id ON stock_movement(transaction_id);
CREATE INDEX idx_stock_movement_created_at ON stock_movement(created_at);
CREATE INDEX idx_stock_movement_updated_at ON stock_movement(updated_at);
CREATE INDEX idx_stock_movement_deleted_at_null ON stock_movement (deleted_at) WHERE deleted_at IS NULL;

-- Table: user_role (many-to-many relationship)
CREATE INDEX idx_user_role_created_at ON user_role(created_at);
CREATE INDEX idx_user_role_updated_at ON user_role(updated_at);
CREATE INDEX idx_user_role_deleted_at_null ON user_role (deleted_at) WHERE deleted_at IS NULL;

-- Table: sale
CREATE INDEX idx_sale_customer_id ON sale(customer_id);
CREATE INDEX idx_sale_created_at ON sale(created_at);
CREATE INDEX idx_sale_updated_at ON sale(updated_at);
CREATE INDEX idx_sale_deleted_at_null ON sale (deleted_at) WHERE deleted_at IS NULL;

-- Table: sale_item
CREATE INDEX idx_sale_item_sale_id ON sale_item(sale_id);
CREATE INDEX idx_sale_item_created_at ON sale_item(created_at);
CREATE INDEX idx_sale_item_updated_at ON sale_item(updated_at);
CREATE INDEX idx_sale_item_deleted_at_null ON sale_item (deleted_at) WHERE deleted_at IS NULL;

-- Table: purchase
CREATE INDEX idx_purchase_supplier_id ON purchase(supplier_id);
CREATE INDEX idx_purchase_created_at ON purchase(created_at);
CREATE INDEX idx_purchase_updated_at ON purchase(updated_at);
CREATE INDEX idx_purchase_deleted_at_null ON purchase (deleted_at) WHERE deleted_at IS NULL;

-- Table: purchase_item
CREATE INDEX idx_purchase_item_purchase_id ON purchase_item(purchase_id);
CREATE INDEX idx_purchase_item_created_at ON purchase_item(created_at);
CREATE INDEX idx_purchase_item_updated_at ON purchase_item(updated_at);
CREATE INDEX idx_purchase_item_deleted_at_null ON purchase_item (deleted_at) WHERE deleted_at IS NULL;

-- Commit
COMMIT;

-- ============================================================
-- Views
-- ============================================================

BEGIN;

-- 	--------------------
--	  Stock Movement Details View
-- 	--------------------

CREATE OR REPLACE VIEW stock_movement_details_view AS
SELECT
    -- Stock Transaction Details
    st.id AS transaction_id,
    st.transaction_type_id,
    stt.name AS transaction_type_name,
    st.description AS transaction_description,
    st.transaction_date,
    st.created_at,
    st.updated_at,
    st.deleted_at,
    
    -- App User Details
    au.id AS user_id,
    au.username AS username,

    -- Stock Movement Details
    sm.id AS stock_movement_id,
    sm.quantity AS stock_movement_quantity,
    sm.description AS stock_movement_description,
    
    -- Stock Movement Type Details
    smt.id AS movement_type_id,
    smt.name AS movement_type_name,
    smt.effect_on_stock AS movement_type_effect_on_stock,
    smt.description AS movement_type_description,
    
    -- Seed Batch Details
    sb.id AS seed_batch_id,
    sb.year AS seed_batch_year,
    sb.grading AS seed_batch_grading,
    sb.germination AS seed_batch_germination,
    sb.description AS seed_batch_description,

    -- Rice Variety Details
    rv.id AS rice_variety_id,
    rv.name AS rice_variety_name,
    rv.description AS rice_variety_description,
    rv.image_url AS rice_variety_image_url,

    -- Rice Generation Details
    rg.id AS generation_id,
    rg.name AS generation_name,
    rg.description AS generation_description,
    
    -- Season Details
    s.id AS season_id,
    s.name AS season_name,
    s.description AS season_description,
    
    -- Sale Details
    sa.stock_transaction_id AS sale_id,
    sa.description AS sale_description,
    c.id AS customer_id,
    c.full_name AS customer_full_name,
    
    -- Sale Item Details
    si.price AS sale_item_price,
    si.description AS sale_item_description,

    -- Purchase Details
    pc.stock_transaction_id AS purchase_id,
    pc.description AS purchase_description,
    sp.id AS supplier_id,
    sp.full_name AS supplier_full_name,
    
    -- Purchase Item Details
    pi.price AS purchase_item_price,
    pi.description AS purchase_item_description

    
FROM
    stock_transaction st
LEFT JOIN
    stock_transaction_type stt ON st.transaction_type_id = stt.id
LEFT JOIN
    app_user au ON st.performed_by = au.id
LEFT JOIN
    stock_movement sm ON st.id = sm.transaction_id
LEFT JOIN
    stock_movement_type smt ON sm.movement_type_id = smt.id
LEFT JOIN
    seed_batch sb ON sm.batch_id = sb.id
LEFT JOIN
    rice_variety rv ON sb.variety_id = rv.id
LEFT JOIN
    rice_generation rg ON sb.generation_id = rg.id
LEFT JOIN
    season s ON sb.season_id = s.id
LEFT JOIN
    sale sa ON st.id = sa.stock_transaction_id
LEFT JOIN
    customer c ON sa.customer_id = c.id
LEFT JOIN
    sale_item si ON sm.id = si.stock_movement_id AND sa.stock_transaction_id = si.sale_id
LEFT JOIN
    purchase pc ON st.id = pc.stock_transaction_id
LEFT JOIN
    supplier sp ON pc.supplier_id = sp.id
LEFT JOIN
    purchase_item pi ON sm.id = pi.stock_movement_id AND pc.stock_transaction_id = pi.purchase_id;

-- 	--------------------
--	  Transaction Summary View
-- 	--------------------

CREATE OR REPLACE VIEW transaction_summary_view AS
SELECT
    -- 1. Transaction Identifiers
    st.id AS transaction_id,
    st.transaction_date,
    st.transaction_type_id,
    stt.name AS transaction_type_name,
    st.description AS transaction_description,
    au.username AS username,
    -- 2. Representative Info
    MAX(smt.name) AS main_movement_type, 
    
    -- 3. Aggregated Stats
    COUNT(sm.id) AS item_count,
    SUM(sm.quantity) AS total_quantity,
    
    -- 4. Corrected Financials: Quantity * Unit Price
    SUM(sm.quantity * si.price) AS total_sale_price,
    SUM(sm.quantity * pi.price) AS total_purchase_price,
    
    -- 5. Entity Information
    MAX(c.full_name) AS customer_name,
    MAX(sp.full_name) AS supplier_name,
    -- 6. Batch Helpers for Adjustment Logic
    MAX(CASE WHEN smt.effect_on_stock = 'OUT' THEN sb.id END) AS from_batch_id,
    MAX(CASE WHEN smt.effect_on_stock = 'IN' THEN sb.id END) AS to_batch_id
FROM
    stock_transaction st
LEFT JOIN stock_transaction_type stt ON st.transaction_type_id = stt.id
LEFT JOIN app_user au ON st.performed_by = au.id
LEFT JOIN stock_movement sm ON st.id = sm.transaction_id
LEFT JOIN stock_movement_type smt ON sm.movement_type_id = smt.id
LEFT JOIN seed_batch sb ON sm.batch_id = sb.id
LEFT JOIN sale sa ON st.id = sa.stock_transaction_id
LEFT JOIN customer c ON sa.customer_id = c.id
LEFT JOIN sale_item si ON sm.id = si.stock_movement_id AND sa.stock_transaction_id = si.sale_id
LEFT JOIN purchase pc ON st.id = pc.stock_transaction_id
LEFT JOIN supplier sp ON pc.supplier_id = sp.id
LEFT JOIN purchase_item pi ON sm.id = pi.stock_movement_id AND pc.stock_transaction_id = pi.purchase_id
WHERE 
    st.deleted_at IS NULL
GROUP BY
    st.id,
    st.transaction_date,
    st.transaction_type_id,
    stt.name,
    st.description,
    au.username
ORDER BY 
    st.transaction_date DESC;

COMMIT;

-- ============================================================
-- Data Seed
-- ============================================================

BEGIN;

-- ------------------------------------------------------------
-- Reference Data
-- ------------------------------------------------------------

-- stock transaction type
INSERT INTO stock_transaction_type (name, description) VALUES
('ADJUSTMENT', 'ປັບປ່ຽນສະພາບແນວພັນ'),
('STOCK_IN', 'ນຳເຂົ້າ'),
('STOCK_OUT', 'ສົ່ງອອກ');

-- season
INSERT INTO season (name, description) VALUES
('DRY', 'ລະດູແລ້ງ'),
('WET', 'ລະດູຝົນ');

-- Seed rice generation
INSERT INTO rice_generation (name, description) VALUES
('R1','R1'),
('R2', 'R2'),
('R3', 'R3');

-- Seed seed condition
INSERT INTO seed_condition (name, description) VALUES
('GOOD', 'ດີ'),
('MOLD', 'ເຊື້ອຣາ'),
('RAT_DMG', 'ໜູກັດ'),
('INSECT_DMG', 'ແມງໄມ້ກັດ'),
('MOIS_DMG', 'ມີຄວາມຊື້ນ'),
('DISCOL', 'ສີປ່ຽນ');

-- Customer type
INSERT INTO customer_type (name, description) VALUES
('FARMER', 'ຊາວນາ'),
('DISTRIBUTOR', 'ຂາຍສົ່ງ'),
('RETAIL', 'ຂາຍຍ່ອຍ');

-- ------------------------------------------------------------
-- Table: supplier_type
-- ------------------------------------------------------------
INSERT INTO supplier_type (name, description) VALUES
('STAFF', 'ພະນັກງານສູນ'),
('FARMER', 'ຊາວນາ'),
('RESEARCH', 'ສູນວິໄຈ'),
('IMPORTER', 'ຜູ້ນຳເຂົ້າ');

-- Stock movement type
INSERT INTO stock_movement_type (name, effect_on_stock, description) VALUES
('PURCHASE', 'IN', 'ຊື້'),
('ADJUSTMENT_IN', 'IN', 'ປັບປ່ຽນສະພາບແນວພັນ(ເຂົ້າ)'),
('SALE', 'OUT', 'ຂາຍ'),
('ADJUSTMENT_OUT', 'OUT', 'ປັບປ່ຽນສະພາບແນວພັນ(ອອກ)');

-- Seed rice variety
INSERT INTO rice_variety (name, description) VALUES
('TDK1', 'High-yield variety'),
('HTDK1', 'Hybrid TDK1'),
('TDK5', 'Resistant to drought'),
('TDK8', 'Short-grain rice'),
('HTDK8', 'Hybrid TDK8'),
('TDK11', 'Good for lowland farms'),
('TDK14', 'Early maturing'),
('HTDK15', 'Hybrid for dry season'),
('HTDK16', 'Pest resistant hybrid'),
('HTDK17', 'Long-grain variety'),
('XBF1', 'Research variety A'),
('XBF2', 'Research variety B'),
('PCH1', 'Popular commercial hybrid'),
('VTE450-2', 'Experimental strain'),
('CR203', 'Cold-resistant variety'),
('XBF4', 'Research variety C'),
('XBF5', 'Research variety D'),
('XBF4-52', 'Derived from XBF4'),
('KDML105', 'Jasmine rice type'),
('RD15', 'High-quality export variety'),
('HNN', 'New local hybrid');

-- Table: role
INSERT INTO role (name, description) VALUES
('ADMIN', 'System Administrator with full access.'),
('WAREHOUSE', 'Manages inventory and stock movements.'),
('SALE', 'Handles sales and customer transactions.');

-- Table: app_user
-- Passwords are fictional hashed values for security
INSERT INTO app_user (username, password, is_active) VALUES
('master-admin', '$2y$10$EcD98b1Acz2hKPG7Yc/LPePRvKO4sEa8t8Cd0uAxuBD8/2YAhsXSS', TRUE); -- admin12345 (generated from https://bcrypt.online/ with Cost Factor:10)

-- Table: user_role (many-to-many relationship)
INSERT INTO user_role (user_id, role_id) VALUES
(1, 1);  -- Assign ADMIN role to first user

COMMIT;