-- Sales Database Schema - MySQL
-- Improved version with English field names and best practices

CREATE TABLE Client (
    id_client SMALLINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(200),
    cpf CHAR(11) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_client),
    INDEX idx_cpf (cpf),
    INDEX idx_email (email)
);

CREATE TABLE PaymentMethod (
    id_payment_method SMALLINT AUTO_INCREMENT NOT NULL,
    method_name VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id_payment_method)
);

CREATE TABLE Product (
    id_product SMALLINT AUTO_INCREMENT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_product),
    INDEX idx_product_name (product_name)
);

CREATE TABLE SalesOrder (
    id_order SMALLINT AUTO_INCREMENT NOT NULL,
    order_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(8, 2) DEFAULT 0.00,
    id_client SMALLINT NOT NULL,
    id_payment_method SMALLINT NOT NULL,
    order_status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_order),
    FOREIGN KEY (id_client) REFERENCES Client(id_client) ON DELETE RESTRICT,
    FOREIGN KEY (id_payment_method) REFERENCES PaymentMethod(id_payment_method) ON DELETE RESTRICT,
    INDEX idx_order_date (order_date),
    INDEX idx_client (id_client)
);

CREATE TABLE SalesOrderDetail (
    id_order_detail SMALLINT AUTO_INCREMENT NOT NULL,
    id_order SMALLINT NOT NULL,
    id_product SMALLINT NOT NULL,
    quantity SMALLINT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL,
    line_total DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    PRIMARY KEY (id_order_detail),
    FOREIGN KEY (id_order) REFERENCES SalesOrder(id_order) ON DELETE CASCADE,
    FOREIGN KEY (id_product) REFERENCES Product(id_product) ON DELETE RESTRICT,
    UNIQUE KEY unique_order_product (id_order, id_product),
    INDEX idx_order (id_order)
);

-- Insert Payment Methods
INSERT INTO PaymentMethod (method_name) VALUES
('PIX'),
('Credit Card'),
('Debit Card'),
('Cash'),
('Bank Transfer');

-- Insert Sample Clients
INSERT INTO Client (name, phone, email, address, cpf) VALUES
('João Silva', '11987654321', 'joao.silva@email.com', 'Rua das Flores, 123 - São Paulo/SP', '12345678901'),
('Maria Santos', '11876543210', 'maria.santos@email.com', 'Av. Paulista, 456 - São Paulo/SP', '23456789012'),
('Pedro Oliveira', '11765432109', 'pedro.oliveira@email.com', 'Rua Augusta, 789 - São Paulo/SP', '34567890123'),
('Ana Costa', '11654321098', 'ana.costa@email.com', 'Rua Oscar Freire, 321 - São Paulo/SP', '45678901234'),
('Carlos Lima', '11543210987', 'carlos.lima@email.com', 'Av. Faria Lima, 654 - São Paulo/SP', '56789012345');

-- Insert Sample Products
INSERT INTO Product (product_name, unit_price) VALUES
('Smartphone Samsung Galaxy', 1299.99),
('Notebook Dell Inspiron', 2499.50),
('Headphone JBL', 199.90),
('Mouse Logitech', 89.99),
('Keyboard Mechanical', 299.99),
('Monitor 24 inches', 899.00),
('USB Cable', 29.90),
('Power Bank', 149.99);