package com.pos.posnghalagako.model;

/**
 * Represents a product (emotion/feeling) available for sale.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private String emoji;
    private double price;
    private int stock;
    private String category;
    private boolean active;

    public Product() {
    }

    public Product(int id, String name, String description, String emoji,
                   double price, int stock, String category, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.active = active;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    /**
     * Display string for POS product tiles.
     */
    public String getDisplayLabel() {
        return (emoji != null ? emoji + " " : "") + name;
    }

    @Override
    public String toString() {
        return getDisplayLabel() + " - ₱" + String.format("%.2f", price);
    }
}
