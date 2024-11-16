/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant_management;

//import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


public class DashBoardController implements Initializable{
    
    // Dashboard
    @FXML
    private BarChart<?, ?> dashboard_ICChart;

    @FXML
    private Label dashboard_NC;

    @FXML
    private BarChart<?, ?> dashboard_NOCChart;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TIncome;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button logout;
    
    
    // Available Food Drink
    @FXML
    private Button availableFD_addBtn;

    @FXML
    private Button availableFD_btn;

    @FXML
    private Button availableFD_clearBtn;

    @FXML
    private TableView<categories> availableFD_tableView;
    
    @FXML
    private TableColumn<categories, String> availableFD_col_price;

    @FXML
    private TableColumn<categories, String> availableFD_col_productID;

    @FXML
    private TableColumn<categories, String> availableFD_col_productName;

    @FXML
    private TableColumn<categories, String> availableFD_col_status;

    @FXML
    private TableColumn<categories, String> availableFD_col_type;

    @FXML
    private Button availableFD_deleteBtn;

    @FXML
    private AnchorPane availableFD_form;

    @FXML
    private TextField availableFD_productID;

    @FXML
    private TextField availableFD_productName;

    @FXML
    private TextField availableFD_productPrice;

    @FXML
    private ComboBox<?> availableFD_productStatus;

    @FXML
    private ComboBox<?> availableFD_productType;

    @FXML
    private TextField availableFD_search;

    @FXML
    private Button availableFD_updateBtn;
    
    
    // Order
    @FXML
    private Button order_addBtn;

    @FXML
    private Button order_btn;
    
    @FXML
    private TextField order_amount;
    
    @FXML
    private Label order_balance;
    
    @FXML
    private TableView<product> order_tableView;

    @FXML
    private TableColumn<product, String> order_col_price;

    @FXML
    private TableColumn<product, String> order_col_productName;

    @FXML
    private TableColumn<product, String> order_col_productID;

    @FXML
    private TableColumn<product, String> order_col_quantity;

    @FXML
    private TableColumn<product, String> order_col_type;

    @FXML
    private AnchorPane order_form;

    @FXML
    private Button order_payBtn;

    @FXML
    private ComboBox<?> order_productID;

    @FXML
    private ComboBox<?> order_productName;

    @FXML
    private Spinner<Integer> order_quantity;

    @FXML
    private Button order_receiptBtn;

    @FXML
    private Button order_removeBtn;

    @FXML
    private Label order_total;
    
    // Database components
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    /* ------------------------------ 
    |         Available FD          |
    ------------------------------ */
    
    private String[] categories = {"Meal", "Drinks"};
    private String[] status = {"Available", "Not Available"};
    
    // Food and Drinks
    public void availableFDStatus()
    {
        List<String> listStatus = new ArrayList<>();
        
        for (String data : status)
        {
            listStatus.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(listStatus);
        availableFD_productStatus.setItems(listData);
    }
    
    public void availableFDTypes ()
    {
        List<String> listCat = new ArrayList<>();

        for (String data : categories) {
            listCat.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listCat);
        availableFD_productType.setItems(listData);
    }
    
    public ObservableList<categories> availableFDListData() {

        ObservableList<categories> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM food_drinks_storage";

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            categories cat;

            while (result.next()) {
                cat = new categories(result.getString("product_id"),
                        result.getString("product_name"), result.getString("type"),
                        result.getDouble("price"), result.getString("status"));

                listData.add(cat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    
    private ObservableList<categories> availableFDList;
    public void availableFDShowData() {
        availableFDList = availableFDListData();

        availableFD_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        availableFD_col_productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        availableFD_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        availableFD_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableFD_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        availableFD_tableView.setItems(availableFDList);

    }
    
    public void availableFDClear() {

        availableFD_productID.setText("");
        availableFD_productName.setText("");
        availableFD_productType.getSelectionModel().clearSelection();
        availableFD_productPrice.setText("");
        availableFD_productStatus.getSelectionModel().clearSelection();

    }
    
    public void availableFDSelect() {

        categories catData = availableFD_tableView.getSelectionModel().getSelectedItem();

        int num = availableFD_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        availableFD_productID.setText(catData.getProductId());
        availableFD_productName.setText(catData.getName());
        availableFD_productPrice.setText(String.valueOf(catData.getPrice()));

    }
    
    public void availableFDUpdate() {

        String sql = "UPDATE food_drinks_storage SET product_name = '"
                + availableFD_productName.getText() + "', type = '"
                + availableFD_productType.getSelectionModel().getSelectedItem() + "', price = '"
                + availableFD_productPrice.getText() + "', status = '"
                + availableFD_productStatus.getSelectionModel().getSelectedItem()
                + "' WHERE product_id = '" + availableFD_productID.getText() + "'";

        connect = DataBase.connectDb();

        try {

            Alert alert;

            if (availableFD_productID.getText().isEmpty()
                    || availableFD_productName.getText().isEmpty()
                    || availableFD_productType.getSelectionModel().getSelectedItem() == null
                    || availableFD_productPrice.getText().isEmpty()
                    || availableFD_productStatus.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Product ID: "
                        + availableFD_productID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    // TO SHOW THE DATA
                    availableFDShowData();
                    // TO CLEAR THE FIELDS
                    availableFDClear();

                } else {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void availableFDDelete() {

        String sql = "DELETE FROM food_drinks_storage WHERE product_id = '"
                + availableFD_productID.getText() + "'";

        connect = DataBase.connectDb();

        try {

            Alert alert;

            if (availableFD_productID.getText().isEmpty()
                    || availableFD_productName.getText().isEmpty()
                    || availableFD_productType.getSelectionModel().getSelectedItem() == null
                    || availableFD_productPrice.getText().isEmpty()
                    || availableFD_productStatus.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Product ID: "
                        + availableFD_productID.getText() + "?");

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    // TO SHOW THE DATA
                    availableFDShowData();
                    // TO CLEAR THE FIELDS
                    availableFDClear();

                } else {
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void availableFDSearch() {

        FilteredList<categories> filter = new FilteredList<>(availableFDList, e -> true);

        availableFD_search.textProperty().addListener((observabl, newValue, oldValue) -> {

            filter.setPredicate(predicateCategories -> {

                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateCategories.getProductId().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getType().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getPrice().toString().contains(searchKey)) {
                    return true;
                } else if (predicateCategories.getStatus().toLowerCase().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<categories> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(availableFD_tableView.comparatorProperty());
        availableFD_tableView.setItems(sortList);

    }
    
    public void availableFDAdd () {
        String sql = "INSERT INTO food_drinks_storage (product_id, product_name, type, price, status) "
                + "VALUES(?,?,?,?,?)";

        connect = DataBase.connectDb();
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, availableFD_productID.getText());
            prepare.setString(2, availableFD_productName.getText());
            prepare.setString(3, (String) availableFD_productType.getSelectionModel().getSelectedItem());
            prepare.setString(4, availableFD_productPrice.getText());
            prepare.setString(5, (String) availableFD_productStatus.getSelectionModel().getSelectedItem());

            Alert alert;

            if (availableFD_productID.getText().isEmpty()
                    || availableFD_productName.getText().isEmpty()
                    || availableFD_productType.getSelectionModel() == null
                    || availableFD_productPrice.getText().isEmpty()
                    || availableFD_productStatus.getSelectionModel() == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

            } else {

                String checkData = "SELECT product_id FROM food_drinks_storage WHERE product_id = '"
                        + availableFD_productID.getText() + "'";

                connect = DataBase.connectDb();

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID: " + availableFD_productID.getText() + " is already exist!");
                    alert.showAndWait();
                } else {
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    // TO SHOW THE DATA
                    availableFDShowData();
                    // TO CLEAR THE FIELDS
                    availableFDClear();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    
    /* ------------------------------ 
    |            Order FD           |
    ------------------------------ */
    
    public void orderReceipt() throws SQLException {
        // Path to your JRXML and Jasper files
    String jrxmlPath = "E:\\18. Java Project\\Restaurant_Management\\src\\restaurant_management\\hello.jrxml";
    String jasperPath = "E:\\18. Java Project\\Restaurant_Management\\src\\restaurant_management\\hello.jasper";

    try {
        // Compile the report if the .jasper file doesn't exist
        File jasperFile = new File(jasperPath);
        if (!jasperFile.exists()) {
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
        }

        // Fill the report
        Map<String, Object> parameters = new HashMap<>();
        System.out.println(customerId);
        parameters.put("data_parameter", customerId); // Ensure `customerId` is properly initialized

        try (Connection connection = DataBase.connectDb()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, connection);

            // View the report
            JasperViewer.viewReport(jasperPrint, false); // false to keep the app running
        }
    } catch (JRException e) {
        // Log JasperReports-specific exceptions
        Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE, "Error generating the report", e);
    }
        
    }
    
    private int customerId;
    public void orderCustomerId() {

        String sql = "SELECT customer_id FROM product";

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                customerId = result.getInt("customer_id");
            }

            String checkData = "SELECT customer_id FROM product_info";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            int customerInfoId = 0;

            while (result.next()) {
                customerInfoId = result.getInt("customer_id");
            }

            if (customerId == 0) {
                customerId += 1;
            } else if (customerId == customerInfoId) {
                customerId += 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private double totalP = 0;
    public void orderTotal() {
        orderCustomerId();

        String sql = "SELECT SUM(price) AS total_price FROM product WHERE customer_id = " + customerId;

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                System.out.println(result);
                totalP = result.getDouble("total_price");
            }
//            orderDisplayTotal();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // Add button  
    public void orderAdd() {

        orderCustomerId();
        orderTotal();

        String sql = "INSERT INTO product "
                + "(customer_id, product_id, product_name, type, price, quantity, date) "
                + "VALUES(?,?,?,?,?,?,?)";

        connect = DataBase.connectDb();

        try {
            String orderType = "";
            double orderPrice = 0;

            String checkData = "SELECT * FROM food_drinks_storage WHERE product_id = '"
                    + order_productID.getSelectionModel().getSelectedItem() + "'";

            statement = connect.createStatement();
            result = statement.executeQuery(checkData);

            if (result.next()) {
                orderType = result.getString("type");
                orderPrice = result.getDouble("price");
            }

            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, customerId);
            prepare.setString(2, (String) order_productID.getSelectionModel().getSelectedItem());
            prepare.setString(3, (String) order_productName.getSelectionModel().getSelectedItem());
            prepare.setString(4, orderType);

            double totalPrice = orderPrice * qty;

//            prepare.setString(5, String.valueOf(totalPrice));
            prepare.setDouble(5, totalPrice);

//            prepare.setString(6, String.valueOf(qty));
            prepare.setInt(6, qty);
            
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//            prepare.setString(7, String.valueOf(sqlDate));
            prepare.setDate(7, sqlDate);

            prepare.executeUpdate();

            orderDisplayTotal();
            orderDisplayData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    // Display total  
    public void orderDisplayTotal() {
        orderTotal();
        order_total.setText("$" + String.valueOf(totalP));

    }
        
    private ObservableList<product> orderData;
    
    // Display table
    public void orderDisplayData() {
        orderData = orderListData();

        order_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        order_col_productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        order_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        order_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        order_tableView.setItems(orderData);
    }
    
    private double amount;
    private double balance;
    
    // Amount of cash    
    public void orderAmount(){
        orderTotal();
        
        Alert alert;
        
        if(order_amount.getText().isEmpty() || order_amount.getText() == null || order_amount.getText() == "")
        {
            alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please type the amount:");
            alert.showAndWait();
            
        }
        else
        {
            amount = Double.parseDouble(order_amount.getText());
            if(amount < totalP)
            {
                order_amount.setText("");
            }
            else
            {
                balance = amount - totalP;
                order_balance.setText(String.valueOf(balance));
            }
            
        }
    }
    
    // Pay
    public void orderPay(){
        
        orderCustomerId();
        orderTotal();

        String sql = "INSERT INTO product_info (customer_id, total, date) VALUES(?,?,?)";

        connect = DataBase.connectDb();

        try {

            Alert alert;

            if (balance == 0 || String.valueOf(balance) == "$0.0" || String.valueOf(balance) == null
                    || totalP == 0 || String.valueOf(totalP) == "$0.0" || String.valueOf(totalP) == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, customerId);
                    prepare.setDouble(2, totalP);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
//                    prepare.setString(3, String.valueOf(sqlDate));
                    prepare.setDate(3, sqlDate);

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();

                    order_total.setText("$0.0");
                    order_balance.setText("$0.0");
                    order_amount.setText("");
                    
//                    orderDisplayData();

                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Select row
    
    
    // Remove row
    public void orderRemove() {

        String sql = "DELETE FROM product WHERE id = " + item;
        System.out.println("remove: " + item);

        connect = DataBase.connectDb();

        try {
            Alert alert;

            if (item == 0 || String.valueOf(item) == null || String.valueOf(item) == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Remove Item: " + item + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Removed!");
                    alert.showAndWait();

                    orderDisplayData();
                    orderDisplayTotal();

                    order_amount.setText("");
                    order_balance.setText("$0.0");

                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int item;
    public void orderSelectData() {

        product prod = order_tableView.getSelectionModel().getSelectedItem();
        int num = order_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        item = prod.getId();
        System.out.println("select: "+ item);
        System.out.println("select: "+ prod.getProductId());

    }
   
    
    
    
    public ObservableList<product> orderListData() {

        orderCustomerId();

        ObservableList<product> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product WHERE customer_id = " + customerId;

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            product prod;

            while (result.next()) {
                prod = new product(result.getInt("id"),
                         result.getString("product_id"),
                         result.getString("product_name"),
                         result.getString("type"),
                         result.getDouble("price"),
                         result.getInt("quantity"));

                listData.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
    
    public void orderProductId() {

        String sql = "SELECT product_id FROM food_drinks_storage WHERE status = 'Available'";

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("product_id"));
            }

            order_productID.setItems(listData);

            orderProductName();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void orderProductName() {

        String sql = "SELECT product_name FROM food_drinks_storage WHERE product_id = '"
                + order_productID.getSelectionModel().getSelectedItem() + "'";

        connect = DataBase.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("product_name"));
            }

            order_productName.setItems(listData);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private SpinnerValueFactory<Integer> spinner;
    
    public void orderSpinner() {
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0);

        order_quantity.setValueFactory(spinner);
    }

    private int qty;

    public void orderQuantity() {
        qty = order_quantity.getValue();
    }
    
    
    // Logout
    public void logout()
    {
        try
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to logout?");
            Optional<ButtonType> option = alert.showAndWait();
            
            if(option.get().equals(ButtonType.OK))
            {
                logout.getScene().getWindow().hide();
                
                // Link Login Form
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Restaurant");
                stage.setScene(scene);
                stage.show();
                
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // Switch
    public void switchForm(ActionEvent event)
    {
        if(event.getSource()==dashboard_btn)
        {
            dashboard_form.setVisible(true);
            availableFD_form.setVisible(false);
            order_form.setVisible(false);
            
            dashboard_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            availableFD_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            order_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");

        }
        else if(event.getSource()==availableFD_btn)
        {
            dashboard_form.setVisible(false);
            availableFD_form.setVisible(true);
            order_form.setVisible(false);
            
            availableFD_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            order_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            
            availableFDShowData();
            availableFDSearch();
        }
        else if(event.getSource()==order_btn)
        {
            dashboard_form.setVisible(false);
            availableFD_form.setVisible(false);
            order_form.setVisible(true);
            
            order_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            availableFD_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            
            orderProductId();
            orderProductName();
            orderSpinner();
            orderDisplayData();
            orderDisplayTotal();
        }
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
        availableFDStatus();
        availableFDTypes();
        availableFDShowData();
        orderProductId();
        orderProductName();
        orderSpinner();
        orderDisplayData();
        orderDisplayTotal();
    }
    
}
