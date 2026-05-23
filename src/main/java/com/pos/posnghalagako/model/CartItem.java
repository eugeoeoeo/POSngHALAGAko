package com.pos.posnghalagako.model;

/**
 * In-memory cart entry used during a POS session.
 * Not persisted directly — converted to TransactionItems on checkout.
 */
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    // JavaFX TableView property accessors
    public String getProductName() { return product.getDisplayLabel(); }
    public double getUnitPrice() { return product.getPrice(); }
}
