/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package restaurant_management;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;
    
    
    // Database components
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }
    
    
    // Login
    public void login() {
        String sql = "SELECT * FROM admin_db WHERE username = ? AND password = ?";
        connect = DataBase.connectDb();  // Ensure your DataBase class and connectDb method are properly implemented
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, username.getText());
            prepare.setString(2, password.getText());

            result = prepare.executeQuery();
            
            Alert alert;
            
            if (username.getText().isEmpty() || password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields!");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    // Successful login
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Login successfully!");
                    alert.showAndWait();

                    // Load the new scene
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml")); // Replace with your actual FXML file path
                        Parent root = loader.load();
                        Stage stage = (Stage) loginBtn.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Print stack trace for debugging
                    }
                } else {
                    // Incorrect credentials
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong credentials!");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log any issues during the database query or processing
        } finally {
            // Close database resources
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
