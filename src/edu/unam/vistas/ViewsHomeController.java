package edu.unam.vistas;



import javafx.application.Platform;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import edu.unam.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class ViewsHomeController {
    public ViewsHomeController() {
    }
    private Connection connection;
    @FXML
    private Button accionHome;

    @FXML
    private Button asistenciaHome;

    @FXML
    private Line barra1Home;

    @FXML
    private Line barra2Home;

    @FXML
    private Label bienvenidoHome;

    @FXML
    private Label controlexpedienteHome;

    @FXML
    private Button expedienteHome;

    @FXML
    private Button involucradoHome;

    @FXML
    private Button minutaHome;

    @FXML
    private AnchorPane panel2Home;

    @FXML
    private AnchorPane panelprincipalHome;

    @FXML
    private StackPane panelstackHome;

    @FXML
    private Button reunionHome;

    @FXML
    private Button salirHome;

    @FXML
    void eventbuttonAccion(ActionEvent event) throws IOException {
        App.setRoot("viewsAccionController");
    }

    @FXML
    void eventbuttonAsistencia(ActionEvent event)throws IOException {
        App.setRoot("viewsAsistenciaController");
    }

    @FXML
    void eventbuttonExpediente(ActionEvent event)throws IOException {
        App.setRoot("viewsExpedienteController");
    }

    @FXML
    void eventbuttonInvolucrado(ActionEvent event)throws IOException {
        App.setRoot("viewsInvolucradoController");
    }

    @FXML
    void eventbuttonMinuta(ActionEvent event)throws IOException {
        App.setRoot("viewsMinutaController");
    }

    @FXML
    void eventbuttonReunion(ActionEvent event)throws IOException {
        App.setRoot("viewsReunionController");
    }

    @FXML
    void eventbuttonSalir(ActionEvent event) {
        // Cierra la conexión con la base de datos
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
        // Cierra la aplicación
        Platform.exit();
    }

}


