package edu.unam;


import java.io.IOException;

import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.ServicioAccion;
import edu.unam.servicios.ServicioAsistencia;
import edu.unam.servicios.ServicioInvolucrado;
import edu.unam.servicios.ServicioMinuta;
import edu.unam.servicios.ServicioReunion;
import edu.unam.servicios.ServivcioExpediente;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App  extends Application {
    private static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("controlExpediente");
        Repositorio repositorio = new Repositorio(emf);
        ServivcioExpediente ServivcioExpediente = new ServivcioExpediente(repositorio);
        ServicioInvolucrado ServivcioInvolucrado = new ServicioInvolucrado(repositorio);
        ServicioAccion ServicioAccion = new ServicioAccion(repositorio);
        ServicioReunion ServicioReunion = new ServicioReunion(repositorio);
        ServicioAsistencia ServicioAsistencia = new ServicioAsistencia(repositorio);
        ServicioMinuta ServicioMinuta = new ServicioMinuta(repositorio);
        scene = new Scene(loadFXML("ViewsHomeController"), 1100, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/edu/unam/vistas/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}


 