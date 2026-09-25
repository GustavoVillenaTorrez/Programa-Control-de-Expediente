package edu.unam.vistas;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javafx.scene.control.TableView;
import edu.unam.App;
import edu.unam.modelo.Asistencia;
import edu.unam.modelo.Involucrado;
import edu.unam.modelo.Reunion;
import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.ServicioAsistencia;
import edu.unam.servicios.ServicioInvolucrado;
import edu.unam.servicios.ServicioReunion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class ViewsAsistencia {
    private EntityManagerFactory emf;
    private Repositorio repositorio;
    private ServicioInvolucrado servicioInvolucrado;
    private ServicioReunion servicioReunion;
    private ServicioAsistencia servicioAsistencia;

    public ViewsAsistencia() {
        emf = Persistence.createEntityManagerFactory("controlExpediente");
        repositorio = new Repositorio(emf);
        servicioInvolucrado = new ServicioInvolucrado(repositorio);
        servicioReunion = new ServicioReunion(repositorio);
        servicioAsistencia = new ServicioAsistencia(repositorio);
    }
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
    private DatePicker campoFecha;

    @FXML
    private ComboBox<Boolean> campoPresencia;

    @FXML
    private TableColumn<Asistencia, LocalDate> columnaFecha;

    @FXML
    private TableColumn<Asistencia, String> columnaInvolucrado;

    @FXML
    private TableColumn<Asistencia, Boolean> columnaPresencia;

    @FXML
    private TableColumn<Asistencia, String> columnaReunion;

    @FXML
    private TableColumn<Asistencia, Long> columnaid;

    @FXML
    private ComboBox<Reunion> conboBoxInvoReunion;

    @FXML
    private ComboBox<Involucrado> conboBoxInvolucrado;
    @FXML
    private TableView<Asistencia> tableViewsAsistencia;
    @FXML
    private TextField textBuscador;
    @FXML
    public void initialize(){
        buttonActualizar.setDisable(true);
        buttonEliminar.setDisable(true);
        
        // Obtener lista de involucrados disponibles
        List<Involucrado> miembrosConsejo = servicioInvolucrado.obtenerMiembrosConsejo();
        // Limpiar ComboBox y luego agregar involucrados que sean miembros disponibles
        conboBoxInvolucrado.getItems().addAll(miembrosConsejo);

        List<Reunion> reunionSeleccionada = servicioReunion.listarReuniones();
            // Limpiar C omboBox y luego agregar reuniones que sean miembros disponibles
        conboBoxInvoReunion.getItems().addAll(reunionSeleccionada);

        
        // Configurar el ComboBox para mostrar "si" y "no"
        campoPresencia.getItems().addAll(true, false);
    
        // Configurar el StringConverter para convertir entre las cadenas y los valores booleanos
        campoPresencia.setConverter(new StringConverter<>() {
            @Override
            public String toString(Boolean value) {
                // Convertir el valor booleano a cadena ("si" o "no")
                return value ? "si" : "no";
            }
    
            @Override
            public Boolean fromString(String string) {
                // Convertir la cadena a valor booleano (true si es "asistira", false si es "no asistira")
                return "si".equalsIgnoreCase(string);
            }
        });

        columnaid.setCellValueFactory(new PropertyValueFactory<>("idAsistencia"));
        //columnaInvolucrado.setCellValueFactory(new PropertyValueFactory<>("involucrado"));
        columnaInvolucrado.setCellValueFactory(cellData -> {
            Asistencia asistencia = cellData.getValue();
            Involucrado involucrado = asistencia.getInvolucrado();
            if (involucrado != null) {
                // Obtener el número de expediente y el tema del expediente asociado a la acción
                String nombre = involucrado.getNombre();
                String rol = involucrado.getRol();
                return new SimpleStringProperty("Nombre:"+nombre + " - Rol: " + rol);
            } else {
                return new SimpleStringProperty(""); // Si no hay involucrados asociado, retornar cadena vacía
            }
        });
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //columnaReunion.setCellValueFactory(new PropertyValueFactory<>("reunion"));
        // Configurar la fábrica de celdas para la columna Reunion
        columnaReunion.setCellValueFactory(cellData -> {
            Asistencia asistencia = cellData.getValue();
            Reunion reunion = asistencia.getReunion();
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

        columnaPresencia.setCellValueFactory(new PropertyValueFactory<>("presencia"));
        
        //columnaexpediente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpediente().getnroExpediente()));
        // Obtener todas las asistencias de la base de datos
        List<Asistencia> asistencia = servicioAsistencia.listarAsistencias();
        
        if (asistencia.isEmpty()) { 
            tableViewsAsistencia.setPlaceholder(new Label("No existen Acciones para visualizar."));
        } else {
            tableViewsAsistencia.getItems().addAll(asistencia);
        }
        // Listener para la selección de la tabla
        tableViewsAsistencia.getSelectionModel().selectedItemProperty().addListener((obs, oldSeleccion, newSeleccion) -> {
            if (newSeleccion != null) {
                buttonActualizar.setDisable(false);
                buttonEliminar.setDisable(false); 
                conboBoxInvolucrado.setValue(newSeleccion.getInvolucrado());
                campoFecha.setValue(newSeleccion.getFecha());
                conboBoxInvoReunion.setValue(newSeleccion.getReunion());
                campoPresencia.setValue(newSeleccion.getPresencia());
            } else {
                buttonActualizar.setDisable(true);
                buttonEliminar.setDisable(true);
                campoPresencia.setValue(null);
                campoFecha.setValue(null);
                conboBoxInvolucrado.setValue(null);
                conboBoxInvoReunion.setValue(null);
            }
        });
    
        // Refrescar la tabla después de agregar una asistencia
        tableViewsAsistencia.refresh();
        tableViewsAsistencia.refresh();
        //botonActualizar o EditarDatos
        buttonActualizar.setOnAction((ActionEvent event) -> {
            // Obtener el expediente seleccionado en la tabla
            Asistencia datosAsistencia = tableViewsAsistencia.getSelectionModel().getSelectedItem();
        
            try {
                if (conboBoxInvolucrado == null || campoFecha == null || conboBoxInvoReunion == null || campoPresencia == null) {
                    Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                    alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                    alertError.showAndWait();
                    return;
                }
                
                datosAsistencia.setInvolucrado(conboBoxInvolucrado.getSelectionModel().getSelectedItem());
                datosAsistencia.setFecha(campoFecha.getValue());
                datosAsistencia.setReunion(conboBoxInvoReunion.getSelectionModel().getSelectedItem());
                datosAsistencia.setPresencia(campoPresencia.getValue());
                
                // Llamar al servicio para editar el expediente
                servicioAsistencia.editarAsistencia(datosAsistencia.getIdAsistencia(),datosAsistencia.getPresencia(),datosAsistencia.getFecha(),datosAsistencia.getReunion(),datosAsistencia.getInvolucrado());
                // Refrescar la tabla con los nuevos datos de la base de datos
                tableViewsAsistencia.refresh();
                // Después de editar, deseleccionar el elemento
                tableViewsAsistencia.getSelectionModel().clearSelection();
            
                // Mostrar mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("La Asistencia se se editó correctamente.");
            
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // En caso de error, mostrar mensaje de error genérico
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede editar la Asistencia");
        
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
        alertError.setContentText("Los Datos de la Asistencia se han agregado Correctamente");
        
        Involucrado involucradoSeleccionado;
        LocalDate fechaIngresada;
        Reunion reunionSeleccionado;
        Boolean presenciaIngresada;

        try {
            // Se obtienen los datos de los campos y se realiza la validación
            involucradoSeleccionado = conboBoxInvolucrado.getSelectionModel().getSelectedItem();
            fechaIngresada = campoFecha.getValue();
            reunionSeleccionado = conboBoxInvoReunion.getSelectionModel().getSelectedItem();
            presenciaIngresada = campoPresencia.getValue();            
            

            // Validar que no haya campos vacíos
            if (involucradoSeleccionado == null || fechaIngresada == null || reunionSeleccionado == null || presenciaIngresada == null) {
                alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                alertError.showAndWait();
                return;
            }
            // Verificar que la fecha de ingreso sea la fecha actual
            if (!fechaIngresada.equals(LocalDate.now())) {
                alertError.setContentText("La fecha de ingreso debe ser la fecha actual.");
                alertError.showAndWait();
                return;
            }
            // Verificar si el involucrado ya tiene una asistencia para la misma fecha y reunión
            boolean asistenciaExistente = tableViewsAsistencia.getItems().stream().anyMatch(asistencia ->
            asistencia.getInvolucrado().getIdInvolucrado().equals(involucradoSeleccionado.getIdInvolucrado()) &&
            asistencia.getFecha().equals(fechaIngresada) &&
            asistencia.getReunion().equals(reunionSeleccionado)
            );

            if (asistenciaExistente) {
                alertError.setContentText("El involucrado ya tiene una asistencia registrada para esta fecha y reunión.");
                alertError.showAndWait();
                return;
            }
            Asistencia newAsistencia = new Asistencia();
            newAsistencia.setInvolucrado(involucradoSeleccionado);
            newAsistencia.setFecha(fechaIngresada);
            newAsistencia.setReunion(reunionSeleccionado);
            newAsistencia.setPresencia(presenciaIngresada);
            // Extraer el número de expediente y el tema del expediente seleccionado en el ComboBox
            // Imprimir el contenido de la reunión
            if (newAsistencia.getReunion() != null) {
                System.out.println("Contenido de la reunión asociada a la nueva asistencia:");
                System.out.println(newAsistencia.getReunion().toString());
            } else {
                System.out.println("La nueva asistencia no tiene una reunión asociada.");
            }
            
            Long idAsistencia = servicioAsistencia.agregarAsistencia(newAsistencia.getPresencia(),newAsistencia.getFecha(),newAsistencia.getReunion(),newAsistencia.getInvolucrado());            
            newAsistencia.setIdAsistencia(idAsistencia);
            tableViewsAsistencia.getItems().add(newAsistencia);
    
            // Mostrar mensaje de éxito
            alertError.showAndWait();

            // Limpiar los campos de texto después de agregar la Acción
            campoPresencia.setValue(null);
            campoFecha.setValue(null);
            conboBoxInvolucrado.setValue(null);
            conboBoxInvoReunion.setValue(null);
            tableViewsAsistencia.refresh();
        } catch (IllegalArgumentException e) {
            // Capturar excepción si hay datos inválidos
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("Error ,Los Datos de la asistencia no se han agregado correctaamente");
        }
    }
    
    @FXML
    void accionEliminar(ActionEvent event) {
        Asistencia datosAsistencia = tableViewsAsistencia.getSelectionModel().getSelectedItem();
            try {
                tableViewsAsistencia.refresh();
                // Eliminar asistencia de la tabla
                servicioAsistencia.eliminarAsistencia(datosAsistencia.getIdAsistencia());
                tableViewsAsistencia.getItems().remove(datosAsistencia);
                tableViewsAsistencia.refresh();      
                // Para mostrar un mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("La Asistencia se eliminó correctamente.");
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // Mostrar mensaje de que asistencia no se pudo eliminar
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede eliminar la asistencia.");
                alertError.showAndWait();
            }
            tableViewsAsistencia.refresh();
    }

    @FXML
    void accionLimpiar(ActionEvent event) {
        campoPresencia.setValue(null);
        campoFecha.setValue(null);
        conboBoxInvolucrado.setValue(null);
        conboBoxInvoReunion.setValue(null);
    }

    @FXML
    void accionVolver(ActionEvent event)throws IOException {
         App.setRoot("ViewsHomeController");
    }
    @FXML
    void buscarFecha(KeyEvent event) {

    }
    @FXML
    void campoBuscar(ActionEvent event) {

    }

    @FXML
    void seleccionFecha(ActionEvent event) {

    }

    @FXML
    void seleccionInvolucrado(ActionEvent event) {

    }

    @FXML
    void seleccionPresencia(ActionEvent event) {

    }

    @FXML
    void seleccionReunion(ActionEvent event) {

    }

}

