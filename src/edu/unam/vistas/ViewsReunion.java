package edu.unam.vistas;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import edu.unam.App;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Involucrado;
import edu.unam.modelo.Reunion;
import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.ServicioInvolucrado;
import edu.unam.servicios.ServicioReunion;
import edu.unam.servicios.ServivcioExpediente;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.util.converter.LocalTimeStringConverter;

public class ViewsReunion {

    private Repositorio repositorio;
    private EntityManagerFactory emf;
    private ServicioInvolucrado servicioInvolucrado;
    private ServivcioExpediente servicioExpediente;
    private ServicioReunion servicioReunion;
    public ViewsReunion() {
        emf = Persistence.createEntityManagerFactory("controlExpediente");
        repositorio = new Repositorio(emf);
        servicioExpediente = new ServivcioExpediente(repositorio);
        servicioInvolucrado = new ServicioInvolucrado(repositorio);
        servicioReunion = new ServicioReunion(repositorio);
    }
    @FXML
    private Button ButtonActualizarReunion;

    @FXML
    private Button ButtonAgregarReunion;

    @FXML
    private Button ButtonEliminarReunion;

    @FXML
    private Button ButtonLimpiar;

    @FXML
    private Button ButtonVolver;

    @FXML
    private DatePicker CampoFecha;

    @FXML
    private AnchorPane PanelExpediente;

    @FXML
    private AnchorPane PanelExpedienteCampos;

    @FXML
    private AnchorPane PanelExpedienteTabla;

    @FXML
    private TableView<Reunion> PanelTablaReunion;

    @FXML
    private TextField TextBuscadorExpediente;

    @FXML
    private Label TextEstado;

    @FXML
    private Label TextExpedienteAbierto;

    @FXML
    private Label TextFecha;

    @FXML
    private Label TextHoraFin;

    @FXML
    private Label TextInicio;

    @FXML
    private Label TextMiembroConsejo;

    @FXML
    private Spinner<LocalTime> campoHoraFin;

    @FXML
    private Spinner<LocalTime> campoHoraInicio;

    @FXML
    private TableColumn<Reunion, String> columnaExpedientesTratar;

    @FXML
    private TableColumn<Reunion, LocalDate> columnaFecha;

    @FXML
    private TableColumn<Reunion, LocalTime> columnaHoraFin;

    @FXML
    private TableColumn<Reunion, LocalTime> columnaHorainicio;

    @FXML
    private TableColumn<Reunion, Long> columnaId;

    @FXML
    private TableColumn<Reunion, String> columnaMiembrosdelConsejo;

    @FXML
    private TableColumn<Reunion, String> columnaestadoReunion;

    @FXML
    private ComboBox<String> comboBoxEstado;

    @FXML
    private ListView<Expediente> listExpedienteAbierto;

    @FXML
    private ListView<Involucrado> listMiembroConsejo;
    @FXML
    public void initialize(){
        PanelTablaReunion.refresh();
        listExpedienteAbierto.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listMiembroConsejo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ButtonActualizarReunion.setDisable(true);
        ButtonEliminarReunion.setDisable(true);
        
       // Configurar fábricas de valores para los spinners de hora de inicio y fin
        SpinnerValueFactory<LocalTime> horaInicioFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null));
                setValue(LocalTime.MIDNIGHT);
            }

            @Override
            public void decrement(int steps) {
                setValue(getValue().minusMinutes(steps));
            }

            @Override
            public void increment(int steps) {
                setValue(getValue().plusMinutes(steps));
            }
        };

        SpinnerValueFactory<LocalTime> horaFinFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null));
                setValue(LocalTime.MIDNIGHT);
            }

            @Override
            public void decrement(int steps) {
                setValue(getValue().minusMinutes(steps));
            }

            @Override
            public void increment(int steps) {
                setValue(getValue().plusMinutes(steps));
            }
        };

        // Aplicar las fábricas de valores a los spinners de hora de inicio y fin
        campoHoraInicio.setValueFactory(horaInicioFactory);
        campoHoraFin.setValueFactory(horaFinFactory);

        // Permitir la edición de los valores en los spinners
        campoHoraInicio.setEditable(true);
        campoHoraFin.setEditable(true);
                
        
        comboBoxEstado.getItems().addAll("Iniciando", "En progreso", "Finalizada");

        List<Expediente> expedientesAbiertos = servicioExpediente.obtenerExpedientesAbiertos();
        listExpedienteAbierto.getItems().addAll(expedientesAbiertos);

        // Cargar solo los miembros del consejo en la lista
        List<Involucrado> miembrosConsejo = servicioInvolucrado.obtenerMiembrosConsejo();
        listMiembroConsejo.getItems().addAll(miembrosConsejo);

        // Configurar las columnas de la tabla de reuniones
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idReunion"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaestadoReunion.setCellValueFactory(new PropertyValueFactory<>("estadoReunion"));
        columnaHorainicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        columnaHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));

        // Columna para los expedientes a tratar
        columnaExpedientesTratar.setCellValueFactory(cellData -> {
            Reunion reunion = cellData.getValue();
            StringBuilder expedientes = new StringBuilder();
            for (Expediente expediente : reunion.getExpediente()) {
                if (expedientes.length() > 0) {
                    expedientes.append(", ");
                }
                expedientes.append("N° Exp: ").append(expediente.getNroExpediente())
                        .append(" - Tema: ").append(expediente.getTema());
            }
            return new SimpleStringProperty(expedientes.toString());
        });

        // Columna para los miembros del consejo
        columnaMiembrosdelConsejo.setCellValueFactory(cellData -> {
            Reunion reunion = cellData.getValue();
            StringBuilder involucrados = new StringBuilder();
            for (Involucrado involucrado : reunion.getInvolucrado()) {
                if (involucrados.length() > 0) {
                    involucrados.append(", ");
                }
                involucrados.append("Nombre: ").append(involucrado.getNombre());
            }
            return new SimpleStringProperty(involucrados.toString());
        });

        // Cargar reuniones en la tabla
        // Convertir la lista de reuniones a ObservableList para actualizar la tabla
        
        List<Reunion> reuniones = servicioReunion.listarReuniones();
        if (reuniones.isEmpty()) { 
            PanelTablaReunion.setPlaceholder(new Label("No existen Reuniones para visualizar."));
        } else {
            PanelTablaReunion.getItems().addAll(reuniones);
        }

        PanelTablaReunion.getSelectionModel().selectedItemProperty().addListener((obs, oldSeleccion, newSeleccion) -> {
            if (newSeleccion != null) {
                ButtonActualizarReunion.setDisable(false);
                ButtonEliminarReunion.setDisable(false);
                CampoFecha.setValue(newSeleccion.getFecha());
                comboBoxEstado.setValue(newSeleccion.getEstadoReunion());
                campoHoraInicio.getValueFactory().setValue(newSeleccion.getHoraInicio());
                campoHoraFin.getValueFactory().setValue(newSeleccion.getHoraFin());
            } else {
                ButtonActualizarReunion.setDisable(true);
                ButtonEliminarReunion.setDisable(true);
                CampoFecha.setValue(null);
                comboBoxEstado.setValue(null);
                campoHoraInicio.getValueFactory().setValue(LocalTime.MIDNIGHT);
                campoHoraFin.getValueFactory().setValue(LocalTime.MIDNIGHT);
            }
        });
        PanelTablaReunion.refresh();
        PanelTablaReunion.refresh();

        ButtonActualizarReunion.setOnAction((ActionEvent event) -> {
            // Obtener la reunión seleccionada en la tabla
            Alert alertError = new Alert(Alert.AlertType.INFORMATION);
            Reunion datosReunion = PanelTablaReunion.getSelectionModel().getSelectedItem();
            // Convertir ObservableList de expedientes e involucrados a conjuntos Set
            
            try {
                // Obtener los datos de la reunión desde los campos de entrada
                datosReunion.setFecha(CampoFecha.getValue());
                datosReunion.setEstadoReunion(comboBoxEstado.getValue());
                datosReunion.setHoraInicio(campoHoraInicio.getValue());
                datosReunion.setHoraFin(campoHoraFin.getValue());
                Set<Expediente> expedientesSet = new HashSet<>(listExpedienteAbierto.getSelectionModel().getSelectedItems());
                Set<Involucrado> involucradosSet = new HashSet<>(listMiembroConsejo.getSelectionModel().getSelectedItems());
                if (datosReunion.getHoraFin() != null && datosReunion.getHoraFin().equals(LocalTime.MIDNIGHT) && (datosReunion.getHoraFin().isAfter(LocalTime.MAX) || datosReunion.getHoraFin().equals(LocalTime.MIN)))  {
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("La hora de fin no puede exceder las 24 horas.");
                    alertError.showAndWait();
                    return; // Salir del método para evitar agregar la reunión incorrecta
                }
                if (datosReunion.getHoraInicio() != null && datosReunion.getHoraFin() != null && datosReunion.getHoraInicio().isAfter(datosReunion.getHoraFin().minusMinutes(1))) {
                    
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("La hora de inicio no puede ser posterior o igual a la hora de fin.");
                    alertError.showAndWait();
                    return; // Salir del método para evitar agregar la reunión incorrecta
                }  
                if (expedientesSet.isEmpty()) {
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("Por favor, seleccione al menos un expediente para la reunión.");
                    alertError.showAndWait();
                    return; // Salir del método para evitar agregar la reunión incorrecta
                }
                if (involucradosSet.isEmpty()) {
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("Por favor, seleccione al menos un miembro para asistir a la reunión.");
                    alertError.showAndWait();
                    return; // Salir del método para evitar agregar la reunión incorrecta
                }
                if (datosReunion.getFecha() == null || datosReunion.getEstadoReunion() == null || datosReunion.getHoraInicio() == null || datosReunion.getHoraFin() == null) {
                    alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                    alertError.showAndWait();
                    return;
                }
        
                if (!datosReunion.getFecha().equals(LocalDate.now())) {
                    alertError.setContentText("La fecha de ingreso debe ser la fecha actual.");
                    alertError.showAndWait();
                    return;
                }
                
                // Llamar al servicio para actualizar la reunión
                servicioReunion.editarReunion(datosReunion.getIdReunion(), datosReunion.getFecha(), datosReunion.getEstadoReunion(), datosReunion.getHoraInicio(), datosReunion.getHoraFin(), expedientesSet, involucradosSet);
        
                // Refrescar la tabla con los nuevos datos de la base de datos
                PanelTablaReunion.refresh();
        
                // Después de editar, deseleccionar el elemento
                PanelTablaReunion.getSelectionModel().clearSelection();
        
                // Mostrar mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("La reunión se actualizó correctamente.");
                alertSuccess.showAndWait();
        
                // Limpiar los campos después de actualizar la reunión
                CampoFecha.setValue(null);
                comboBoxEstado.setValue(null);
                campoHoraInicio.getValueFactory().setValue(LocalTime.MIDNIGHT);
                campoHoraFin.getValueFactory().setValue(LocalTime.MIDNIGHT);
            } catch (Exception e) {
                // Mostrar mensaje de error si la reunión no puede ser actualizada
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Error");
                alertErro.setHeaderText(null);
                alertErro.setContentText("No se puede editar la Reunión: " + e.getMessage());
                alertErro.showAndWait();
            }
        });
    }
    
    @FXML
    void AccionActualizarReunion(ActionEvent event) {

    }

    @FXML
    void AccionAgregarReunion(ActionEvent event) {
        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
        alertSuccess.setTitle("Éxito");
        alertSuccess.setHeaderText(null);
        alertSuccess.setContentText("Los Datos de la Reunion se han agregado Correctamente");

        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.setContentText("Error, los datos de la Reunion no se han agregado correctamente");

        LocalDate fechaIngresada;
        String estadoReunionIngresado;
        LocalTime horaInicioIngresada;
        LocalTime horaFinIngresada;
        ObservableList<Expediente> expedientesSeleccionados = listExpedienteAbierto.getSelectionModel().getSelectedItems();
        ObservableList<Involucrado> involucradosSeleccionados = listMiembroConsejo.getSelectionModel().getSelectedItems();
        try {
            fechaIngresada = CampoFecha.getValue();
            estadoReunionIngresado = comboBoxEstado.getValue();
            // Obtener la hora de inicio seleccionada del Spinner
            horaInicioIngresada = campoHoraInicio.getValue();
            horaFinIngresada = campoHoraFin.getValue();
            if (horaFinIngresada != null && horaFinIngresada.equals(LocalTime.MIDNIGHT) && (horaFinIngresada.isAfter(LocalTime.MAX) || horaFinIngresada.equals(LocalTime.MIN)))  {
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("La hora de fin no puede exceder las 24 horas.");
                alertError.showAndWait();
                return; // Salir del método para evitar agregar la reunión incorrecta
            }
            if (horaInicioIngresada != null && horaFinIngresada != null && horaInicioIngresada.isAfter(horaFinIngresada.minusMinutes(1))) {
                
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("La hora de inicio no puede ser posterior o igual a la hora de fin.");
                alertError.showAndWait();
                return; // Salir del método para evitar agregar la reunión incorrecta
            }
            // Obtener los expedientes seleccionados en la lista
            Set<Expediente> expedientesSet = new HashSet<>(expedientesSeleccionados);
            // Obtener los involucrados seleccionados en la lista
            Set<Involucrado> involucradosSet = new HashSet<>(involucradosSeleccionados);
            
            

            
            if (expedientesSeleccionados.isEmpty()) {
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("Por favor, seleccione al menos un expediente para la reunión.");
                alertError.showAndWait();
                return; // Salir del método para evitar agregar la reunión incorrecta
            }
            if (involucradosSeleccionados.isEmpty()) {
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("Por favor, seleccione al menos un miembro para asistir a la reunión.");
                alertError.showAndWait();
                return; // Salir del método para evitar agregar la reunión incorrecta
            }
            if (fechaIngresada == null || estadoReunionIngresado == null || horaFinIngresada == null || horaInicioIngresada == null) {
                alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                alertError.showAndWait();
                return;
            }
    
            if (!fechaIngresada.equals(LocalDate.now())) {
                alertError.setContentText("La fecha de ingreso debe ser la fecha actual.");
                alertError.showAndWait();
                return;
            }
            
            Reunion newReunion = new Reunion();
            newReunion.setFecha(fechaIngresada);
            newReunion.setEstadoReunion(estadoReunionIngresado);
            newReunion.setHoraInicio(horaInicioIngresada);
            newReunion.setHoraFin(horaFinIngresada);



            // Agregar la reunión utilizando el servicio
            Long idReunion = servicioReunion.agregarReunion(newReunion.getFecha(), newReunion.getEstadoReunion(), newReunion.getHoraInicio(), newReunion.getHoraFin(), expedientesSet, involucradosSet);
            newReunion.setIdReunion(idReunion);
            // Mostrar mensaje de éxito
            // Agregar las reuniones a la tabla de reuniones
            //PanelTablaReunion.getItems().add(newReunion);
            // Volver a cargar las reuniones desde la base de datos
            List<Reunion> reuniones = servicioReunion.listarReuniones();
            PanelTablaReunion.getItems().clear(); // Limpiar la tabla
            if (reuniones.isEmpty()) { 
                PanelTablaReunion.setPlaceholder(new Label("No existen Reuniones para visualizar."));
            } else {
                PanelTablaReunion.getItems().addAll(reuniones);
            }
            PanelTablaReunion.refresh();
            alertSuccess.showAndWait();
            // Limpiar los campos después de agregar la reunión
            CampoFecha.setValue(null);
            comboBoxEstado.setValue(null);
            campoHoraInicio.getValueFactory().setValue(LocalTime.MIDNIGHT);
            campoHoraFin.getValueFactory().setValue(LocalTime.MIDNIGHT);
            
        }catch (Exception e) {
            e.printStackTrace();
            alertError.setContentText("Error: " + e.getMessage());
            alertError.showAndWait();
        }
        

    }

    
    /*void AccionEliminarReunion(ActionEvent event) {
        Reunion datosReunion = PanelTablaReunion.getSelectionModel().getSelectedItem();
        try {
            // Eliminar La Reunion de la tabla
            servicioReunion.eliminarReunion(datosReunion.getIdReunion());
            PanelTablaReunion.getItems().remove(datosReunion);
            // Mostrar un mensaje de éxito
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Éxito");
            alertSuccess.setHeaderText(null);
            alertSuccess.setContentText("La Reunion se eliminó correctamente.");
            alertSuccess.showAndWait();
        } catch (Exception e) {
            // Mostrar mensaje de error con detalles sobre la excepción
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("No se puede eliminar la Reunion. Error: " + e.getMessage());
            alertError.showAndWait();
        }
    }*/
    @FXML
    void AccionEliminarReunion(ActionEvent event) {
        // Obtener la reunión seleccionada de la tabla
        Reunion reunionSeleccionada = PanelTablaReunion.getSelectionModel().getSelectedItem();
        
        if (reunionSeleccionada == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ninguna reunión
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setTitle("Advertencia");
            alertWarning.setHeaderText(null);
            alertWarning.setContentText("Por favor, seleccione una reunión para eliminar.");
            alertWarning.showAndWait();
            return;
        }

        // Confirmar la acción con el usuario
        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setTitle("Confirmación");
        alertConfirmation.setHeaderText(null);
        alertConfirmation.setContentText("¿Está seguro de que desea eliminar esta reunión, sus minutas y asistencias asociadas?");
        Optional<ButtonType> result = alertConfirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Intentar eliminar la reunión y manejar el resultado
            int resultado = servicioReunion.eliminarReunion(reunionSeleccionada.getIdReunion());
            
            // Manejar el resultado de la operación
            if (resultado == 0) {
                // Eliminación exitosa
                PanelTablaReunion.getItems().remove(reunionSeleccionada);
                PanelTablaReunion.refresh();
                
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("La reunión y sus minutas y asistencias se eliminaron correctamente.");
                alertSuccess.showAndWait();
            } else if (resultado == 2) {
                // La reunión no fue encontrada
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("La reunión no fue encontrada.");
                alertError.showAndWait();
            } else if (resultado == 3) {
                // Ocurrió un error inesperado
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("Ocurrió un error inesperado al intentar eliminar la reunión.");
                alertError.showAndWait();
            } else {
                // Manejar otros posibles errores
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se pudo eliminar la reunión debido a un problema desconocido.");
                alertError.showAndWait();
            }
        }
    }

    @FXML
    void AccionLimpiar(ActionEvent event) {
        CampoFecha.setValue(null);
        comboBoxEstado.setValue(null);
        campoHoraInicio.getValueFactory().setValue(LocalTime.MIDNIGHT);
        campoHoraFin.getValueFactory().setValue(LocalTime.MIDNIGHT);
    }

    @FXML
    void AccionVolver(ActionEvent event)throws IOException {
        App.setRoot("ViewsHomeController");
    }

    @FXML
    void BuscarExpedienteNumero(ActionEvent event) {
        
    }
    

    @FXML
    void SeleccionExpedienteAbierto(ActionEvent event) {

    }

    @FXML
    void SeleccionFechaIngreso(ActionEvent event) {

    }

    @FXML
void buscarnroExpediente(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
        String textoBusqueda = TextBuscadorExpediente.getText();
        if (textoBusqueda.isEmpty()) {
            // Si el campo de búsqueda está vacío, muestra todas las reuniones de la base de datos
            try {
                List<Reunion> todasLasReuniones = servicioReunion.listarReuniones();
                PanelTablaReunion.getItems().clear();
                PanelTablaReunion.getItems().addAll(todasLasReuniones);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al buscar reuniones");
                alert.setContentText("Se produjo un error al buscar todas las reuniones.");
                alert.showAndWait();
                e.printStackTrace();
            }
        } else {
            try {
                LocalDate fechaBusqueda = LocalDate.parse(textoBusqueda);
                List<Reunion> reunionesEncontradas = servicioReunion.buscarReunionesPorFecha(fechaBusqueda);
                if (!reunionesEncontradas.isEmpty()) {
                    PanelTablaReunion.getItems().clear();
                    PanelTablaReunion.getItems().addAll(reunionesEncontradas);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Resultado de la búsqueda");
                    alert.setContentText("No se encontraron reuniones para la fecha " + fechaBusqueda);
                    alert.showAndWait();
                }
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Formato de fecha incorrecto");
                alert.setContentText("Por favor, ingrese una fecha válida en el formato AAAA-MM-DD.");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al buscar reuniones");
                alert.setContentText("Se produjo un error al buscar reuniones por fecha.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }
}
}
