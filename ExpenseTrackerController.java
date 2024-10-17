package demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.time.LocalDate;

public class ExpenseTrackerController {

    @FXML
    private TextField expenseField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Label totalLabel;

    @FXML
    private TableView<Expense> expenseTable;

    @FXML
    private TableColumn<Expense, String> categoryColumn;

    @FXML
    private TableColumn<Expense, String> descriptionColumn;

    @FXML
    private TableColumn<Expense, Double> amountColumn;

    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    private static final String FILE_PATH = "expenses.csv";  // Path to the CSV file

    @FXML
    public void initialize() {
        // Initialize the category ComboBox with preset categories
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Utilities", "Transportation", "Food", "Clothing", "Medical/Healthcare",
                "Insurance", "Personal", "Entertainment"
        ));

        // Initialize the table columns
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);

        expenseTable.getSortOrder().add(dateColumn);
        expenseTable.setItems(expenseList);
        // Load expenses from the CSV file at startup
        loadExpensesFromFile();
    }

    @FXML
    private void handleAddExpense() {
        String expenseText = expenseField.getText();
        String descriptionText = descriptionField.getText();
        String selectedCategory = categoryComboBox.getValue();

        if (!expenseText.isEmpty() && selectedCategory != null && !descriptionText.isEmpty()) {
            try {
                double expenseAmount = Double.parseDouble(expenseText);
                LocalDate currentDate = LocalDate.now();
                Expense expense = new Expense(selectedCategory, descriptionText, expenseAmount, currentDate);
                expenseList.add(expense);
                updateTotal();
                expenseField.clear();
                descriptionField.clear();
                categoryComboBox.getSelectionModel().clearSelection();

                expenseTable.sort();

                saveExpensesToFile();


            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number.");
            }
        } else {
            showAlert("Missing Information", "Please fill in all fields.");
        }
    }

    @FXML
    private void handleClearExpenses() {
        expenseList.clear();
        updateTotal();
    }

    private void updateTotal() {
        double total = expenseList.stream().mapToDouble(Expense::getAmount).sum();
        totalLabel.setText(String.format("Total Expenses: $%.2f", total));
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Save the expenses to a CSV file.
     */
    /**
     * Save the expenses to a CSV file.
     */
    public void saveExpensesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenseList) {
                writer.write(String.format("%s,%s,%.2f,%s\n",
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getDate().toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the expenses from the CSV file at startup.
     */


    public void loadExpensesFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String category = parts[0];
                        String description = parts[1];
                        double amount = Double.parseDouble(parts[2]);
                        LocalDate date = LocalDate.parse(parts[3]);
                        Expense expense = new Expense(category, description, amount, date);
                        expenseList.add(expense);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

