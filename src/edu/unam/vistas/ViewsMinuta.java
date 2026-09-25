package edu.unam.vistas;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import edu.unam.App;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Minuta;
import edu.unam.modelo.Reunion;
import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.ServicioMinuta;
import edu.unam.servicios.ServicioReunion;
import edu.unam.servicios.ServivcioExpediente;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class ViewsMinuta {
    private EntityManagerFactory emf;
    private Repositorio repositorio;
    private ServivcioExpediente servicioExpediente;
    private ServicioReunion servicioReunion;
    private ServicioMinuta servicioMinuta;
    public ViewsMinuta() {
        emf = Persistence.createEntityManagerFactory("controlExpediente");
        repositorio = new Repositorio(emf);
        servicioExpediente = new ServivcioExpediente(repositorio);
        servicioReunion = new ServicioReunion(repositorio);
        servicioMinuta = new ServicioMinuta(repositorio);
    }
    @FXML
    private TableView<Minuta> PanelTablaMinuta;

    @FXML
    private Button buttonActualizar;

    @FXML
    private Button buttonAgregar;

    @FXML
    private Button buttonEliminar;

    @FXML
    private Button buttonLimpiar;

    @FXML
    private Button buttonVolver;

    @FXML
    private TextArea campoDescripcion;

    @FXML
    private TextArea campoNotas;

    @FXML
    private TableColumn<Minuta, String> columnaDesiciones;

    @FXML
    private TableColumn<Minuta, String> columnaNotas;

    @FXML
    private TableColumn<Minuta, String> columnaReunionTratada;

    @FXML
    private TableColumn<Minuta, String> columnaTemaExpediente;

    @FXML
    private TableColumn<Minuta, Long> columnaid;

    @FXML
    private ComboBox<Expediente> comboBoxExpediente;

    @FXML
    private ComboBox<Reunion> comboBoxReunion;

    @FXML
    private TextField textBuscador;

    @FXML
    private Label textDesiciones;

    @FXML
    private Label textExpediente;

    @FXML
    private Label textNota;

    @FXML
    private Label textReuion;
    @FXML
    public void initialize(){
        buttonActualizar.setDisable(true);
        buttonEliminar.setDisable(true);
        
        List<Expediente> temaExpediente = servicioExpediente.listarExpedientes();
            // Limpiar ComboBox y luego agregar involucrados que sean miembros disponibles
        comboBoxExpediente.getItems().addAll(temaExpediente);

        List<Reunion> reunionSeleccionada = servicioReunion.listarReuniones();
            // Limpiar ComboBox y luego agregar reuniones que sean miembros disponibles
        comboBoxReunion.getItems().addAll(reunionSeleccionada);

       

         
        columnaid.setCellValueFactory(new PropertyValueFactory<>("idMinuta"));
        columnaDesiciones.setCellValueFactory(new PropertyValueFactory<>("desicionesTomadas"));
        columnaNotas.setCellValueFactory(new PropertyValueFactory<>("notasAdicionales"));
        columnaTemaExpediente.setCellValueFactory(new PropertyValueFactory<>("expediente"));
        columnaTemaExpediente.setCellValueFactory(cellData -> {
            Minuta minuta = cellData.getValue();
            Expediente expedinete = minuta.getExpediente();
            if (expedinete != null) {
                // Obtener el número de expediente y el tema del expediente asociado a la acción
                Long nroExpediente = expedinete.getNroExpediente();
                String tema = expedinete.getTema();
                return new SimpleStringProperty("N° Exp:"+nroExpediente + " - Tema: " + tema);
            } else {
                return new SimpleStringProperty(""); // Si no hay reuniones asociado, retornar cadena vacía
            }
        });
        //columnaReunionTratada.setCellValueFactory(new PropertyValueFactory<>("reunion"));
        columnaReunionTratada.setCellValueFactory(cellData -> {
            Minuta minuta = cellData.getValue();
            Reunion reunion = minuta.getReunion();
            if (reunion != null) {
                // Obtener el número de expediente y el tema del expediente asociado a la acción
                LocalDate fecha = reunion.getFecha();
                LocalTime horaInicio = reunion.getHoraInicio();
                LocalTime horaFin = reunion.getHoraFin();
                return new SimpleStringProperty("Fecha:"+fecha + " - Hora Inicio: " + horaInicio+ " - Hora Fin: " + horaFin);
            } else {
                return new SimpleStringProperty(""); // Si no hay reuniones asociado, retornar cadena vacía
            }
        });
      
        //columnaexpediente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpediente().getnroExpediente()));
        // Obtener todas las Minutas de la base de datos
        List<Minuta> minuta = servicioMinuta.listarMinuta();
        
        if (minuta.isEmpty()) { 
            PanelTablaMinuta.setPlaceholder(new Label("No existen Minutas para visualizar."));
        } else {
            PanelTablaMinuta.getItems().addAll(minuta);
        }
        // Listener para la selección de la tabla
        PanelTablaMinuta.getSelectionModel().selectedItemProperty().addListener((obs, oldSeleccion, newSeleccion) -> {
            if (newSeleccion != null) {
                buttonActualizar.setDisable(false);
                buttonEliminar.setDisable(false); 
                campoDescripcion.setText(newSeleccion.getDesicionesTomadas());
                campoNotas.setText(newSeleccion.getNotasAdicionales());
                comboBoxExpediente.setValue(newSeleccion.getExpediente());
                comboBoxReunion.setValue(newSeleccion.getReunion());
            } else {
                buttonActualizar.setDisable(true);
                buttonEliminar.setDisable(true);
                campoDescripcion.setText(null);
                campoNotas.setText(null);
                comboBoxExpediente.setValue(null);
                comboBoxReunion.setValue(null);
            }
        });
        // Refrescar la tabla después de agregar una asistencia
        PanelTablaMinuta.refresh();
        PanelTablaMinuta.refresh();
        //botonActualizar o EditarDatos
        buttonActualizar.setOnAction((ActionEvent event) -> {
            // Obtener el expediente seleccionado en la tabla
            Minuta datosMinuta = PanelTablaMinuta.getSelectionModel().getSelectedItem();
        
            try {
                
                if ( campoDescripcion == null || campoNotas == null || comboBoxExpediente == null || comboBoxReunion == null) {
                    Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                    alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                    alertError.showAndWait();
                    return;
                }
        
                
                datosMinuta.setDesicionesTomadas(campoDescripcion.getText());
                datosMinuta.setNotasAdicionales(campoNotas.getText());
                datosMinuta.setExpediente(comboBoxExpediente.getValue());
                datosMinuta.setReunion(comboBoxReunion.getValue());
                
                // Llamar al servicio para editar el expedientedatosMinuta
                servicioMinuta.editarMinuta(datosMinuta.getIdMinuta(),datosMinuta.getDesicionesTomadas(),datosMinuta.getNotasAdicionales(),datosMinuta.getReunion(),datosMinuta.getExpediente());
                // Refrescar la tabla con los nuevos datos de la base de datos
                PanelTablaMinuta.refresh();
                // Después de editar, deseleccionar el elemento
                PanelTablaMinuta.getSelectionModel().clearSelection();
            
                // Mostrar mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("La Minuta se se editó correctamente.");
            
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // En caso de error, mostrar mensaje de error genérico
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede editar la Minuta");
        
                alertError.showAndWait();
            }
        });
    }

    @FXML
    void accionActualizar(ActionEvent event) {
        
    }

    @FXML
    void accionAgregar(ActionEvent event) {
        Alert alertError = new Alert(Alert.AlertType.INFORMATION);
        alertError.setTitle("Éxito");
        alertError.setHeaderText(null);
        alertError.setContentText("Los Datos del Expediente se han agregado Correctamente");
        String descipcionIngresada;
        String notaIngresada;
        Reunion reunionSeleccionado;
        Expediente expedienteSeleccionado;
        try {
            // Se obtienen los datos de los campos y se realiza la validación            
           
            descipcionIngresada = campoDescripcion.getText();
            notaIngresada = campoNotas.getText();
            reunionSeleccionado = comboBoxReunion.getValue();
            expedienteSeleccionado = comboBoxExpediente.getValue();

            // Validar que no haya campos vacíos
            if ( descipcionIngresada == null || notaIngresada == null || expedienteSeleccionado == null || reunionSeleccionado == null) {
                alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                alertError.showAndWait();
                return;
            }
            Minuta newMinuta = new Minuta();
            newMinuta.setNotasAdicionales(notaIngresada);
            newMinuta.setDesicionesTomadas(descipcionIngresada);
            newMinuta.setExpediente(expedienteSeleccionado);
            newMinuta.setReunion(reunionSeleccionado);
            // Extraer el número de expediente y el tema del expediente seleccionado en el ComboBox

            
            Long idMinuta = servicioMinuta.agregarMinuta(newMinuta.getDesicionesTomadas(),newMinuta.getNotasAdicionales(),newMinuta.getReunion(),newMinuta.getExpediente());            
            newMinuta.setIdMinuta(idMinuta);
            PanelTablaMinuta.getItems().add(newMinuta);
    
            // Mostrar mensaje de éxito
            alertError.showAndWait();

            // Limpiar los campos de texto después de agregar la Acción
            campoDescripcion.setText(null);
            campoNotas.setText(null);
            comboBoxExpediente.setValue(null);
            comboBoxReunion.setValue(null);

            PanelTablaMinuta.refresh();

        } catch (IllegalArgumentException e) {
            // Capturar excepción si hay datos inválidos
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("Error ,Los Datos de la Minuta no se han agregado correctaamente");
        }
    }

    @FXML
    void accionEliminar(ActionEvent event) {
        Minuta datosMinuta = PanelTablaMinuta.getSelectionModel().getSelectedItem();
        try {
            PanelTablaMinuta.refresh();
            // Eliminar el Involucrado de la tabla
            servicioMinuta.eliminarMinuta(datosMinuta.getIdMinuta());
            PanelTablaMinuta.getItems().remove(datosMinuta);
            PanelTablaMinuta.refresh();      
            // Para mostrar un mensaje de éxito
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Éxito");
            alertSuccess.setHeaderText(null);
            alertSuccess.setContentText("La Minuta se eliminó correctamente.");
            alertSuccess.showAndWait();
        } catch (Exception e) {
            // Mostrar mensaje de que el expediente no se pudo eliminar
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("No se puede eliminar la Minuta.");
            alertError.showAndWait();
        }
    PanelTablaMinuta.refresh();
    }

    @FXML
    void accionLimpiar(ActionEvent event) {
        campoDescripcion.setText(null);
        campoNotas.setText(null);
        comboBoxExpediente.setValue(null);
        comboBoxReunion.setValue(null);
    }

    @FXML
    void accionVolver(ActionEvent event)throws IOException {
        App.setRoot("ViewsHomeController");
    }

    @FXML
    void buscarnroFecha(KeyEvent event) {

    }

    @FXML
    void campoBuscar(ActionEvent event) {

    }

    @FXML
    void seleccionExpediente(ActionEvent event) {

    }

    @FXML
    void seleccionReunion(ActionEvent event) {

    }


}
