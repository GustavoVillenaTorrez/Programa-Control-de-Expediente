package edu.unam.vistas;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import edu.unam.servicios.ServivcioExpediente;
import edu.unam.App;
import edu.unam.repositorios.Repositorio;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import edu.unam.modelo.Expediente;


public class ViewsExpedientes {
    private EntityManagerFactory emf;
    private Repositorio repositorio;
    private ServivcioExpediente servivcioExpediente;

    public ViewsExpedientes() {
        emf = Persistence.createEntityManagerFactory("controlExpediente");
        repositorio = new Repositorio(emf);
        servivcioExpediente = new ServivcioExpediente(repositorio);
        
    }
    @FXML
    private Button ButtonActualizarExpediente;

    @FXML
    private Button ButtonAgregarExpediente;

    @FXML
    private Button ButtonEliminarExpediente;

    @FXML
    private Button ButtonLimpiar;

    @FXML
    private Button ButtonVolver;

    @FXML
    private ComboBox<Boolean> CampoExpediente;

    @FXML
    private DatePicker CampoFecha;

    @FXML
    private TextField CampoInciante;

    @FXML
    private TextArea CampoIngresarNota;

    @FXML
    private TextField CampoNroExpediente;

    @FXML
    private TextField CampoTema;

    @FXML
    private TableColumn<Expediente, Boolean> ColumnaEstadoExpediente;

    @FXML
    private TableColumn<Expediente, LocalDate> ColumnaFechaIngresoFacultad;

    @FXML
    private TableColumn<Expediente, Long> ColumnaId;

    @FXML
    private TableColumn<Expediente, String> ColumnaIniciante;

    @FXML
    private TableColumn<Expediente, Long> ColumnaNroExpediente;

    @FXML
    private TableColumn<Expediente, String> ColumnaTema;

    @FXML
    private TableColumn<Expediente, String> ColumnaTextoNota;

    @FXML
    private AnchorPane PanelExpediente;

    @FXML
    private AnchorPane PanelExpedienteCampos;

    @FXML
    private AnchorPane PanelExpedienteTabla;

    @FXML
    private TableView<Expediente> PanelTablaExpediente;

    @FXML
    private TextField TextBuscadorExpediente;

    @FXML
    private Label TextExpediente;

    @FXML
    private Label TextExpedienteAbierto;

    @FXML
    private Label TextFechaIngreso;

    @FXML
    private Label TextIniciante;

    @FXML
    private Label TextNota;

    @FXML
    private Label TextTema;

    @FXML
    private Button buttonVolver;

    @FXML
    void AccionActualizarExpediente(ActionEvent event) {

    }
    @FXML
    public void initialize() {

        ButtonActualizarExpediente.setDisable(true);
        ButtonEliminarExpediente.setDisable(true);
    
        // Configurar el ComboBox para mostrar "si" y "no"
        CampoExpediente.getItems().addAll(true, false);
    
        // Configurar el StringConverter para convertir entre las cadenas y los valores booleanos
        CampoExpediente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Boolean value) {
                // Convertir el valor booleano a cadena ("Abierto" o "Cerrado")
                return value ? "si" : "no";
            }
    
            @Override
            public Boolean fromString(String string) {
                // Convertir la cadena a valor booleano (true si es "Abierto", false si es "Cerrado")
                return "si".equalsIgnoreCase(string);
            }
        });
    
        ColumnaId.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
        ColumnaNroExpediente.setCellValueFactory(new PropertyValueFactory<>("nroExpediente"));
        ColumnaIniciante.setCellValueFactory(new PropertyValueFactory<>("iniciante"));
        ColumnaTema.setCellValueFactory(new PropertyValueFactory<>("tema"));
        ColumnaTextoNota.setCellValueFactory(new PropertyValueFactory<>("textoNota"));
        ColumnaFechaIngresoFacultad.setCellValueFactory(new PropertyValueFactory<>("fechaingresoFacultad"));
        ColumnaEstadoExpediente.setCellValueFactory(new PropertyValueFactory<>("estadoExpediente"));
    
        // Obtener todos los expedientes de la base de datos
        List<Expediente> expedientes = servivcioExpediente.buscarTodos();
    
        if (expedientes.isEmpty()) { 
            PanelTablaExpediente.setPlaceholder(new Label("No existen expedientes para visualizar."));
        } else {
            PanelTablaExpediente.getItems().addAll(expedientes);
        }
    
        // Listener para la selección de la tabla
        PanelTablaExpediente.getSelectionModel().selectedItemProperty().addListener((obs, oldSeleccion, newSeleccion) -> {
            if (newSeleccion != null) {
                ButtonActualizarExpediente.setDisable(false);
                ButtonEliminarExpediente.setDisable(false); 
                CampoNroExpediente.setText(String.valueOf(newSeleccion.getNroExpediente()));
                CampoInciante.setText(newSeleccion.getIniciante());
                CampoIngresarNota.setText(newSeleccion.getTextoNota());
                CampoTema.setText(newSeleccion.getTema());
                CampoFecha.setValue(newSeleccion.getFechaingresoFacultad());
                CampoExpediente.setValue(newSeleccion.getEstadoExpediente());
            } else {
                ButtonActualizarExpediente.setDisable(true);
                ButtonEliminarExpediente.setDisable(true);
                CampoNroExpediente.clear();
                CampoInciante.clear();
                CampoIngresarNota.clear();
                CampoTema.clear();
                CampoFecha.setValue(null);
                CampoExpediente.setValue(null);
            }
        });
    
        // Refrescar la tabla después de agregar un expediente
        PanelTablaExpediente.refresh();
        PanelTablaExpediente.refresh();
        //botonActualizar o EditarDatos
        ButtonActualizarExpediente.setOnAction((ActionEvent event) -> {
            // Obtener el expediente seleccionado en la tabla
            Expediente datosExpediente = PanelTablaExpediente.getSelectionModel().getSelectedItem();
        
            try {
                String nroExpedienteString = CampoNroExpediente.getText().trim();
                if (nroExpedienteString.isEmpty()) {
                    throw new IllegalArgumentException("El número de expediente no puede estar vacío.");
                }
                // Corrección aquí: obtener el número de expediente del campo de texto
                datosExpediente.setNroExpediente(Long.parseLong(CampoNroExpediente.getText()));
                datosExpediente.setIniciante(CampoInciante.getText());
                datosExpediente.setTema(CampoTema.getText());
                datosExpediente.setTextoNota(CampoIngresarNota.getText());
                datosExpediente.setFechaingresoFacultad(CampoFecha.getValue());
                datosExpediente.setEstadoExpediente(CampoExpediente.getValue());
                // Validar que el número de expediente sea mayor que cero
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                if (!datosExpediente.getIniciante().matches("[a-zA-Z\\s]+")) {
                    alertError.setContentText("El nombre del iniciante solo puede contener letras y espacios.");
                    alertError.showAndWait();
                    return;
                }
                //String nombreInicianteMayuscula = datosExpediente.getIniciante().toUpperCase();
                //datosExpediente.setIniciante(nombreInicianteMayuscula);

                // Verificar que el número de expediente sea solo números
                if (!CampoNroExpediente.getText().matches("\\d+")) {
                    alertError.setContentText("El número de expediente debe contener solo números.");
                    alertError.showAndWait();
                    return;
                }
                if (datosExpediente.getNroExpediente() <= 0) {
                    alertError.setContentText("El número de expediente debe ser mayor que cero y solo Numero.");
                    alertError.showAndWait();
                    return;
                }
                // Verificar que la fecha de ingreso sea la fecha actual
                if (!datosExpediente.getFechaingresoFacultad().equals(LocalDate.now())) {
                    alertError.setContentText("La fecha de ingreso debe ser la fecha actual.");
                    alertError.showAndWait();
                    return;
                }
        
                
                // Llamar al servicio para editar el expediente
                servivcioExpediente.editarExpediente(datosExpediente.getIdExpediente(),datosExpediente.getNroExpediente(), datosExpediente.getIniciante(), datosExpediente.getTema(), datosExpediente.getTextoNota(), datosExpediente.getFechaingresoFacultad(), datosExpediente.getEstadoExpediente());
                    
                // Refrescar la tabla con los nuevos datos de la base de datos
                PanelTablaExpediente.refresh();
                // Después de editar, deseleccionar el elemento
                PanelTablaExpediente.getSelectionModel().clearSelection();
            
                // Mostrar mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("El expediente se editó correctamente.");
            
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // En caso de error, mostrar mensaje de error genérico
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede editar el expediente");
        
                alertError.showAndWait();
            }
        });
    }    
    @FXML
    void AccionAgregarExpediente(ActionEvent event) {
        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
        alertSuccess.setTitle("Éxito");
        alertSuccess.setHeaderText(null);
        alertSuccess.setContentText("Los Datos del Expediente se han agregado Correctamente");
    
        Alert alertError = new Alert(Alert.AlertType.INFORMATION);
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.setContentText("Error ,Los Datos del Expediente no se han agregado correctaamente");

        Long nroExpedienteIngresado;
        String inicianteIngresado;
        String temaIngresado;
        String notaIngresado;
        LocalDate fechaIngresado;
        Boolean expedienteAbiertoSeleccionado;
    
        try {
            // Se obtienen los datos de los campos y se realiza la validación
            nroExpedienteIngresado = Long.parseLong(CampoNroExpediente.getText().trim());
            inicianteIngresado = CampoInciante.getText().trim();
            temaIngresado = CampoTema.getText().trim();
            notaIngresado = CampoIngresarNota.getText().trim();
            fechaIngresado = CampoFecha.getValue();
            expedienteAbiertoSeleccionado = CampoExpediente.getValue();
            //valida si ya hay un expediente con el mismo numero de expediente
            if (servivcioExpediente.expedienteExiste(nroExpedienteIngresado)) {
                alertError.setContentText("El número de expediente ya existe en la base de datos.");
                alertError.showAndWait();
                return;
            }
            
            if (!inicianteIngresado.matches("[a-zA-Z\\s]+")) {
                alertError.setContentText("El nombre del iniciante solo puede contener letras y espacios.");
                alertError.showAndWait();
                return;
            }
            inicianteIngresado = inicianteIngresado.toUpperCase();
            
            // Validar que no haya campos vacíos
            if (inicianteIngresado.isEmpty() || temaIngresado.isEmpty() || notaIngresado.isEmpty() || fechaIngresado == null || expedienteAbiertoSeleccionado == null) {
                alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                alertError.showAndWait();
                return;
            }
            if (!temaIngresado.matches("[a-zA-Z0-9\\s\\p{Punct}]+")) {
                alertError.setContentText("El tema solo puede contener letras, números, espacios y símbolos Puntuación.");
                alertError.showAndWait();
                return;
            }
            temaIngresado = temaIngresado.toUpperCase();
    
            // Validar que el número de expediente sea mayor que cero
            if (nroExpedienteIngresado <= 0) {
                alertError.setContentText("El número de expediente debe ser mayor que cero.");
                alertError.showAndWait();
                return;
            }
    
            // Verificar que la fecha de ingreso sea la fecha actual
            if (!fechaIngresado.equals(LocalDate.now())) {
                alertError.setContentText("La fecha de ingreso debe ser la fecha actual.");
                alertError.showAndWait();
                return;
            }
            // Verificar que el número de expediente sea solo números
            if (!CampoNroExpediente.getText().matches("\\d+")) {
                    alertError.setContentText("El número de expediente debe contener solo números.");
                    alertError.showAndWait();
                    return;
            }

            // Agregar el expediente si todos los datos son válidos
            Expediente newExpediente = new Expediente();
            newExpediente.setNroExpediente(nroExpedienteIngresado);
            newExpediente.setIniciante(inicianteIngresado);
            newExpediente.setTema(temaIngresado);
            newExpediente.setTextoNota(notaIngresado);
            newExpediente.setFechaingresoFacultad(fechaIngresado);
            newExpediente.setEstadoExpediente(expedienteAbiertoSeleccionado);

            Long idExpediente = servivcioExpediente.agregarExpediente(newExpediente.getNroExpediente(), newExpediente.getIniciante(), newExpediente.getTema(), newExpediente.getTextoNota(), newExpediente.getFechaingresoFacultad(), newExpediente.getEstadoExpediente());

            newExpediente.setIdExpediente(idExpediente); // Establecer el ID generado en el expediente

            PanelTablaExpediente.getItems().add(newExpediente);
            //servivcioExpediente.agregarExpediente(newExpediente.getNroExpediente(), newExpediente.getIniciante(), newExpediente.getTema(), newExpediente.getTextoNota(), newExpediente.getFechaingresoFacultad(), newExpediente.getEstadoExpediente());
            //PanelTablaExpediente.getItems().add(newExpediente);
    
            // Mostrar mensaje de éxito
            alertSuccess.showAndWait();
    
            // Limpiar los campos de texto después de agregar el Expediente
            CampoNroExpediente.clear();
            CampoInciante.clear();
            CampoIngresarNota.clear();
            CampoTema.clear();
            CampoFecha.setValue(null);
            CampoExpediente.getSelectionModel().clearSelection();
    
            PanelTablaExpediente.refresh();
        } catch (NumberFormatException e) {
            // Capturar excepción si el número de expediente no es un número válido
            alertError.setContentText("El número de expediente debe ser un valor numérico válido.");
            alertError.showAndWait();
        } catch (IllegalArgumentException e) {
            // Capturar excepción si hay datos inválidos
            alertError.setContentText(e.getMessage());
            alertError.showAndWait();
        }
    }

    /*@FXML
    void AccionEliminarExpediente(ActionEvent event) {
        Expediente datosExpediente = PanelTablaExpediente.getSelectionModel().getSelectedItem();
                try {
                    PanelTablaExpediente.refresh();
                    // Eliminar el expediente de la tabla
                    servivcioExpediente.eliminarExpediente(datosExpediente.getIdExpediente());
                    PanelTablaExpediente.getItems().remove(datosExpediente);
                    PanelTablaExpediente.refresh();      
                    // Para mostrar un mensaje de éxito
                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                    alertSuccess.setTitle("Éxito");
                    alertSuccess.setHeaderText(null);
                    alertSuccess.setContentText("El expediente se eliminó correctamente.");
                    alertSuccess.showAndWait();
                } catch (Exception e) {
                    // Mostrar mensaje de que el expediente no se pudo eliminar
                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("No se puede eliminar el expediente.");
                    alertError.showAndWait();
                }
                PanelTablaExpediente.refresh();
    }*/
    @FXML
    void AccionEliminarExpediente(ActionEvent event) {
        // Obtener el expediente seleccionado
        Expediente expedienteSeleccionado = PanelTablaExpediente.getSelectionModel().getSelectedItem();

        if (expedienteSeleccionado == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ningún expediente
            Alert alertAdvertencia = new Alert(Alert.AlertType.WARNING);
            alertAdvertencia.setTitle("Advertencia");
            alertAdvertencia.setHeaderText(null);
            alertAdvertencia.setContentText("Por favor, seleccione un expediente para eliminar.");
            alertAdvertencia.showAndWait();
            return;
        }

        // Confirmar la acción con el usuario
        Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmacion.setTitle("Confirmación");
        alertConfirmacion.setHeaderText(null);
        alertConfirmacion.setContentText("¿Está seguro de que desea eliminar este expediente?");
        Optional<ButtonType> resultado = alertConfirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Intentar eliminar el expediente
            int resultadoEliminacion = servivcioExpediente.eliminarExpediente(expedienteSeleccionado.getIdExpediente());

            // Manejar el resultado de la operación
            if (resultadoEliminacion == 0) {
                // Eliminación exitosa
                PanelTablaExpediente.getItems().remove(expedienteSeleccionado);
                PanelTablaExpediente.refresh();

                Alert alertExito = new Alert(Alert.AlertType.INFORMATION);
                alertExito.setTitle("Éxito");
                alertExito.setHeaderText(null);
                alertExito.setContentText("El expediente se eliminó correctamente.");
                alertExito.showAndWait();
            } else if (resultadoEliminacion == 1) {
                // El expediente tiene reuniones asociadas
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede eliminar el expediente porque tiene reuniones asociadas.");
                alertError.showAndWait();
            } else if (resultadoEliminacion == 2) {
                // El expediente tiene involucrados asociados
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede eliminar el expediente porque tiene involucrados asociados.");
                alertError.showAndWait();
            } else {
                // Error inesperado
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("Ocurrió un error inesperado al intentar eliminar el expediente.");
                alertError.showAndWait();
            }
        }
    }
    
    @FXML
    void AccionLimpiar(ActionEvent event) {
        CampoInciante.clear();
        CampoNroExpediente.clear();
        CampoTema.clear();
        CampoIngresarNota.clear();
        CampoFecha.setValue(null);
        CampoExpediente.setValue(null);
    }

    @FXML
    void AccionVolver(ActionEvent event)throws IOException {
        App.setRoot("ViewsHomeController");
    }
    @FXML
    void BuscarExpedienteNumero(ActionEvent event) {
        
    }

    @FXML
    void IngresoIniciante(ActionEvent event) {

    }

    @FXML
    void IngresoNroExpediente(ActionEvent event) {

    }

    @FXML
    void IngresoTema(ActionEvent event) {

    }

    @FXML
    void SeleccionExpedienteAbierto(ActionEvent event) {

    }

    @FXML
    void SeleccionFechaIngreso(ActionEvent event) {

    }

    @FXML
    void accionVolver(ActionEvent event) {

    }
    
    @FXML
    void buscarnroExpediente(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textoBusqueda = TextBuscadorExpediente.getText().trim();
    
            // Verificar si el texto de búsqueda es un número válido
            try {
                Long numeroExpediente = Long.parseLong(textoBusqueda);
    
                // Realizar la búsqueda de expedientes según el número de expediente
                List<Expediente> expedientesEncontrados = servivcioExpediente.buscarExpedientePorNumero(numeroExpediente);
    
                // Limpiar la tabla antes de agregar los nuevos resultados
                PanelTablaExpediente.getItems().clear();
                // Actualizar la tabla con los resultados de la búsqueda
                PanelTablaExpediente.getItems().addAll(expedientesEncontrados);
            } catch (NumberFormatException e) {
                // Manejar el caso en que el texto de búsqueda no es un número válido
                System.out.println("El texto de búsqueda no es un número válido.");
                // Puedes mostrar un mensaje de error al usuario si lo deseas
            }
        }
        // Si el campo de búsqueda está vacío después de la pulsación de tecla, mostrar todos los datos de la tabla nuevamente
        if (event.getCode() == KeyCode.ENTER && TextBuscadorExpediente.getText().isEmpty()) {
            List<Expediente> todosExpedientes = servivcioExpediente.listarExpedientes();
            PanelTablaExpediente.getItems().clear();
            PanelTablaExpediente.getItems().addAll(todosExpedientes);
        }

    }

}
