package com.pos.posnghalagako.controller;

import com.pos.posnghalagako.factory.SceneFactory;
import com.pos.posnghalagako.model.Product;
import com.pos.posnghalagako.repository.ProductRepository;
import com.pos.posnghalagako.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colId;
    @FXML private TableColumn<Product, String> colEmoji;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, Boolean> colActive;
    @FXML private TableColumn<Product, String> colDescription;

    @FXML private Label formTitle;
    @FXML private TextField emojiField;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField descriptionField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    private final ProductRepository productRepository = new ProductRepository();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private Product editingProduct = null; // null = adding, non-null = editing

    @FXML
    public void initialize() {
        // Set up table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmoji.setCellValueFactory(new PropertyValueFactory<>("emoji"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("active"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Format price column
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("₱%.2f", item));
            }
        });

        // Format active column
        colActive.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (item ? "✅" : "❌"));
            }
        });

        productTable.setItems(products);
        loadProducts();
    }

    private void loadProducts() {
        try {
            products.setAll(productRepository.findAll());
        } catch (Exception e) {
            showError("Load Error", e.getMessage());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        // Validate inputs
        String name = nameField.getText().trim();
        String emoji = emojiField.getText().trim();
        String category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            showError("Validation Error", "Name, Category, Price, and Stock are required.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Validation Error", "Price must be a valid positive number.");
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Validation Error", "Stock must be a valid non-negative integer.");
            return;
        }

        try {
            if (editingProduct == null) {
                // Adding new product
                Product product = new Product();
                product.setName(name);
                product.setEmoji(emoji.isEmpty() ? "💫" : emoji);
                product.setCategory(category);
                product.setPrice(price);
                product.setStock(stock);
                product.setDescription(description);
                product.setActive(true);
                productRepository.save(product);

                showInfo("Product Added", "\"" + name + "\" has been added to the catalog.");
            } else {
                // Updating existing product
                editingProduct.setName(name);
                editingProduct.setEmoji(emoji.isEmpty() ? "💫" : emoji);
                editingProduct.setCategory(category);
                editingProduct.setPrice(price);
                editingProduct.setStock(stock);
                editingProduct.setDescription(description);
                productRepository.update(editingProduct);

                showInfo("Product Updated", "\"" + name + "\" has been updated.");
                handleCancelEdit(event);
            }

            clearForm();
            loadProducts();
        } catch (Exception e) {
            showError("Save Error", e.getMessage());
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a product to edit.");
            return;
        }

        editingProduct = selected;
        formTitle.setText("Edit Product (ID: " + selected.getId() + ")");
        emojiField.setText(selected.getEmoji());
        nameField.setText(selected.getName());
        categoryField.setText(selected.getCategory());
        priceField.setText(String.valueOf(selected.getPrice()));
        stockField.setText(String.valueOf(selected.getStock()));
        descriptionField.setText(selected.getDescription());
        saveBtn.setText("💾 Update Product");
        cancelBtn.setVisible(true);
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No Selection", "Please select a product to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + selected.getName() + "\"? This cannot be undone.",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    productRepository.delete(selected.getId());
                    loadProducts();
                    showInfo("Deleted", "\"" + selected.getName() + "\" has been removed.");
                } catch (Exception e) {
                    showError("Delete Error", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleCancelEdit(ActionEvent event) {
        editingProduct = null;
        formTitle.setText("Add New Product");
        saveBtn.setText("💾 Save Product");
        cancelBtn.setVisible(false);
        clearForm();
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadProducts();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(SceneFactory.createDashboardScene());
            stage.setTitle("POSngHALAGAko — Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        emojiField.clear();
        nameField.clear();
        categoryField.clear();
        priceField.clear();
        stockField.clear();
        descriptionField.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
