package com.pos.posnghalagako.controller;

import com.pos.posnghalagako.factory.SceneFactory;
import com.pos.posnghalagako.model.CartItem;
import com.pos.posnghalagako.model.Product;
import com.pos.posnghalagako.model.Transaction;
import com.pos.posnghalagako.repository.ProductRepository;
import com.pos.posnghalagako.repository.TransactionRepository;
import com.pos.posnghalagako.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class POSController {

    @FXML private ListView<Product> productListView;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> colCartProduct;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Double> colCartPrice;
    @FXML private TableColumn<CartItem, Double> colCartSubtotal;
    @FXML private Label totalLabel;
    @FXML private Label changeLabel;
    @FXML private Label cashierLabel;
    @FXML private TextField amountPaidField;

    private final ProductRepository productRepository = new ProductRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (SessionManager.isLoggedIn()) {
            cashierLabel.setText("Cashier: " + SessionManager.getCurrentUser().getFullName());
        }

        // Set up cart table columns
        colCartProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        cartTable.setItems(cartItems);

        // Listen to amount paid for live change calculation
        amountPaidField.textProperty().addListener((obs, oldVal, newVal) -> updateChange());

        // Double-click a product in the list to add to cart
        productListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Product selected = productListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    addToCart(selected);
                }
            }
        });

        loadProducts();
    }

    private void loadProducts() {
        try {
            List<Product> products = productRepository.findAllActive();
            productListView.setItems(FXCollections.observableArrayList(products));
        } catch (Exception e) {
            showError("Failed to load products", e.getMessage());
        }
    }

    private void addToCart(Product product) {
        // Check if already in cart
        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId() == product.getId()) {
                ci.setQuantity(ci.getQuantity() + 1);
                cartTable.refresh();
                updateTotal();
                updateChange();
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
        updateTotal();
        updateChange();
    }

    @FXML
    private void handleRemoveItem(ActionEvent event) {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cartItems.remove(selected);
            updateTotal();
            updateChange();
        }
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem ci : cartItems) {
            total += ci.getSubtotal();
        }
        totalLabel.setText(String.format("%.2f", total));
    }

    private double getTotal() {
        double total = 0;
        for (CartItem ci : cartItems) {
            total += ci.getSubtotal();
        }
        return total;
    }

    private void updateChange() {
        String text = amountPaidField.getText().trim();
        if (text.isEmpty()) {
            changeLabel.setText("");
            return;
        }
        try {
            double paid = Double.parseDouble(text);
            double total = getTotal();
            double change = paid - total;
            if (change >= 0) {
                changeLabel.setText(String.format("Change: %.2f", change));
            } else {
                changeLabel.setText(String.format("Insufficient: %.2f", Math.abs(change)));
            }
        } catch (NumberFormatException e) {
            changeLabel.setText("Enter a valid amount");
        }
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (cartItems.isEmpty()) {
            showError("Empty Cart", "Add products to the cart before checking out.");
            return;
        }

        String amountText = amountPaidField.getText().trim();
        if (amountText.isEmpty()) {
            showError("Missing Payment", "Enter the amount paid.");
            return;
        }

        double amountPaid;
        try {
            amountPaid = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showError("Invalid Amount", "Please enter a valid numeric amount.");
            return;
        }

        double total = getTotal();
        if (amountPaid < total) {
            showError("Insufficient Payment",
                    String.format("Amount paid (%.2f) is less than total (%.2f).", amountPaid, total));
            return;
        }

        double change = amountPaid - total;

        try {
            Transaction txn = new Transaction(
                    SessionManager.getCurrentUser().getId(),
                    total, amountPaid, change
            );

            int txnId = transactionRepository.save(txn, cartItems);

            // Show receipt
            StringBuilder sb = new StringBuilder();
            sb.append("Transaction #").append(txnId).append("\n\n");
            for (CartItem ci : cartItems) {
                sb.append(ci.getProduct().getName())
                  .append(" x").append(ci.getQuantity())
                  .append(" = ").append(String.format("%.2f", ci.getSubtotal()))
                  .append("\n");
            }
            sb.append("\nTotal: ").append(String.format("%.2f", total));
            sb.append("\nPaid: ").append(String.format("%.2f", amountPaid));
            sb.append("\nChange: ").append(String.format("%.2f", change));

            Alert receipt = new Alert(Alert.AlertType.INFORMATION, sb.toString(), ButtonType.OK);
            receipt.setTitle("Sale Complete");
            receipt.setHeaderText("Thank you!");
            receipt.showAndWait();

            // Clear cart
            cartItems.clear();
            amountPaidField.clear();
            changeLabel.setText("");
            updateTotal();
            loadProducts();

        } catch (Exception e) {
            showError("Checkout Error", e.getMessage());
        }
    }

    @FXML
    private void handleClearCart(ActionEvent event) {
        cartItems.clear();
        amountPaidField.clear();
        changeLabel.setText("");
        updateTotal();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(SceneFactory.createDashboardScene());
            stage.setTitle("POSngHALAGAko - Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
