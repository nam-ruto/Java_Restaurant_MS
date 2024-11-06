/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package restaurant_management;

//import java.awt.event.ActionEvent;
import java.sql.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


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
    private TableColumn<?, ?> order_col_price;

    @FXML
    private TableColumn<?, ?> order_col_producName;

    @FXML
    private TableColumn<?, ?> order_col_productID;

    @FXML
    private TableColumn<?, ?> order_col_quantity;

    @FXML
    private TableColumn<?, ?> order_col_type;

    @FXML
    private AnchorPane order_form;

    @FXML
    private Button order_payBtn;

    @FXML
    private ComboBox<?> order_productID;

    @FXML
    private ComboBox<?> order_productName;

    @FXML
    private Spinner<?> order_quantity;

    @FXML
    private Button order_receiptBtn;

    @FXML
    private Button order_removeBtn;

    @FXML
    private TableView<?> order_tableView;

    @FXML
    private Label order_total;
    
    // Database components
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    // Logic
    
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
    
    public void availableFDAdd ()
    {
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
//                    availableFDClear();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
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
        }
        else if(event.getSource()==order_btn)
        {
            dashboard_form.setVisible(false);
            availableFD_form.setVisible(false);
            order_form.setVisible(true);
            
            order_btn.setStyle("-fx-background-color: #3796a7; -fx-text-fill: #fff; -fx-border-width: 0px;");
            availableFD_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
            dashboard_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #C9D6FF, #E2E2E2);");
        }
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
        availableFDStatus();
        availableFDTypes();
        availableFDShowData();
    }
    
}
