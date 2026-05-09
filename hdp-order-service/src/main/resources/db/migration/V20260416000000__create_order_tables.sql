-- =====================================================
-- Order Service Database Schema
-- UUID: Uses PostgreSQL gen_random_uuid() (v7 - time-ordered)
-- =====================================================

-- Create ENUM types
CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'COMPLETED', 'CANCELLED', 'PAYMENT_FAILED');
CREATE TYPE sub_order_status AS ENUM ('PAID', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'COMPLETED', 'CANCELLED');
CREATE TYPE payment_method AS ENUM ('CREDIT_CARD', 'BANK_TRANSFER', 'E_WALLET', 'COD');

-- =====================================================
-- ORDERS TABLE
-- =====================================================
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(20) NOT NULL UNIQUE,
    buyer_id UUID NOT NULL,
    shipping_address_id UUID NOT NULL,
    payment_method payment_method NOT NULL,
    status order_status NOT NULL DEFAULT 'PENDING',
    subtotal DECIMAL(19,4) NOT NULL DEFAULT 0,
    shipping_fee DECIMAL(19,4) NOT NULL DEFAULT 0,
    discount DECIMAL(19,4) NOT NULL DEFAULT 0,
    tax DECIMAL(19,4) NOT NULL DEFAULT 0,
    total_amount DECIMAL(19,4) NOT NULL DEFAULT 0,
    payment_intent_id VARCHAR(100),
    expires_at TIMESTAMP WITH TIME ZONE,
    paid_at TIMESTAMP WITH TIME ZONE,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    cancellation_reason VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- =====================================================
-- SUB_ORDERS TABLE
-- =====================================================
CREATE TABLE sub_orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL,
    status sub_order_status NOT NULL DEFAULT 'PAID',
    tracking_number VARCHAR(100),
    carrier VARCHAR(50),
    estimated_delivery DATE,
    processed_at TIMESTAMP WITH TIME ZONE,
    shipped_at TIMESTAMP WITH TIME ZONE,
    note VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- =====================================================
-- ORDER_ITEMS TABLE
-- =====================================================
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    sub_order_id UUID NOT NULL REFERENCES sub_orders(id) ON DELETE CASCADE,
    seller_id UUID NOT NULL,
    product_id UUID NOT NULL,
    variant_id UUID,
    product_name VARCHAR(255) NOT NULL,
    variant_name VARCHAR(255),
    price DECIMAL(19,4) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- =====================================================
-- APPLIED_COUPONS TABLE
-- =====================================================
CREATE TABLE applied_coupons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    code VARCHAR(50) NOT NULL,
    coupon_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(19,4) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- =====================================================
-- ORDER_STATUS_HISTORY TABLE
-- =====================================================
CREATE TABLE order_status_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    previous_status order_status,
    new_status order_status NOT NULL,
    changed_by UUID,
    reason VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_orders_buyer_id ON orders(buyer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at DESC);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_expires_at ON orders(expires_at);

CREATE INDEX idx_sub_orders_order_id ON sub_orders(order_id);
CREATE INDEX idx_sub_orders_seller_id ON sub_orders(seller_id);
CREATE INDEX idx_sub_orders_status ON sub_orders(status);

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_sub_order_id ON order_items(sub_order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);

CREATE INDEX idx_applied_coupons_order_id ON applied_coupons(order_id);
CREATE INDEX idx_applied_coupons_code ON applied_coupons(code);

CREATE INDEX idx_order_status_history_order_id ON order_status_history(order_id);

-- =====================================================
-- AUTO-UPDATE TRIGGER FOR updated_at
-- =====================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_sub_orders_updated_at
    BEFORE UPDATE ON sub_orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();