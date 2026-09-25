package edu.unam.vistas;

import edu.unam.App;
import edu.unam.modelo.Expediente;
import edu.unam.modelo.Involucrado;
import edu.unam.repositorios.Repositorio;
import edu.unam.servicios.ServicioInvolucrado;
import edu.unam.servicios.ServivcioExpediente;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class ViewsInvolucrados {
    private final Repositorio repositorio;
    private EntityManagerFactory emf;
    private ServicioInvolucrado servicioInvolucrado;
    private ServivcioExpediente servicioExpediente;
    public ViewsInvolucrados() {
        emf = Persistence.createEntityManagerFactory("controlExpediente");
        repositorio = new Repositorio(emf);
        servicioExpediente = new ServivcioExpediente(repositorio);
        servicioInvolucrado = new ServicioInvolucrado(repositorio);
    }
    @FXML
    private Button ButtonActualizar;

    @FXML
    private Button ButtonAgregar;

    @FXML
    private Button ButtonEliminar;

    @FXML
    private Button ButtonLimpiar;

    @FXML
    private Button ButtonVolver;

    @FXML
    private TextField CampoCorreo;

    @FXML
    private TextField CampoDni;

    @FXML
    private ListView<Expediente> CampoExpediente;

    @FXML
    private ComboBox<Boolean> CampoMiembro;

    @FXML
    private TextField CampoNombreCompeto;

    @FXML
    private TextField CampoRol;

    @FXML
    private TextField CampoTelefono;

    @FXML
    private TableColumn<Involucrado, Long> ColumnaDni;

    @FXML
    private TableColumn<Involucrado, Boolean> ColumnaEsMiembro;

    @FXML
    private TableColumn<Involucrado, Long> ColumnaId;

    @FXML
    private TableColumn<Involucrado, String> ColumnaRol;

    @FXML
    private AnchorPane PanelInvolucrado;

    @FXML
    private TableView<Involucrado> TablaInvolucrados;

    @FXML
    private TextField TextBuscador;

    @FXML
    private TextField TextBuscador1;

    @FXML
    private Label TextDNI;

    @FXML
    private Label TextEmail;

    @FXML
    private Label TextExpediente;

    @FXML
    private Label TextMiembro;

    @FXML
    private Label TextNombreCompleto;

    @FXML
    private Label TextRol;

    @FXML
    private Label TexttTlefono;

    @FXML
    private TableColumn<Involucrado, String> columnaCorreo;

    @FXML
    private TableColumn<Involucrado, String> columnaExpediente;

    @FXML
    private TableColumn<Involucrado, String> columnaNombreCompleto;

    @FXML
    private TableColumn<Involucrado, Long> columnaTelefono;

    @FXML
    public void initialize(){
        //mientras que no haya datos seleccionados en la tabla o cargados los botones de eliminar y actualizar estaran desabilitados
        ButtonActualizar.setDisable(true);
        ButtonEliminar.setDisable(true);
        try {
            // Obtener lista de expedientes disponibles          
            List<Expediente> expedientesDisponibles = servicioExpediente.buscarTodos();
            // Limpiar ListView y luego agregar expedientes disponibles
            CampoExpediente.getItems().clear();
            CampoExpediente.getItems().addAll(expedientesDisponibles);
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir al cargar los expedientes
            e.printStackTrace();
        }
    
        // Configurar el ComboBox para mostrar "si" y "no" para miembro
        CampoMiembro.getItems().addAll(true, false);
    
        // Configurar el StringConverter para convertir entre las cadenas y los valores booleanos
        CampoMiembro.setConverter(new StringConverter<>() {
            @Override
            public String toString(Boolean value) {
                // Convertir el valor booleano a cadena ("Si " es o "No")
                return value ? "si" : "no";
            }
    
            @Override
            public Boolean fromString(String string) {
                // Convertir la cadena a valor booleano 
                return "si".equalsIgnoreCase(string);
            }
        });
        
        ColumnaId.setCellValueFactory(new PropertyValueFactory<>("idInvolucrado"));
        columnaNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnaDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        ColumnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        ColumnaEsMiembro.setCellValueFactory(new PropertyValueFactory<>("esmiembroConsejo"));
        columnaExpediente.setCellValueFactory(new PropertyValueFactory<>("expediente"));
        
        // Obtener todos los involucrados de la base de datos
        List<Involucrado> involucrados = servicioInvolucrado.listarInvolucrados();

        if (involucrados.isEmpty()) {
            TablaInvolucrados.setPlaceholder(new Label("No existen Involucrados para visualizar."));
        } else {
            TablaInvolucrados.getItems().addAll(involucrados);
        }
        // Listener para la selección de la tabla
        TablaInvolucrados.getSelectionModel().selectedItemProperty().addListener((obs, oldSeleccion, newSeleccion) -> {
            if (newSeleccion != null) {
                ButtonActualizar.setDisable(false);
                ButtonEliminar.setDisable(false); 
                CampoNombreCompeto.setText(newSeleccion.getNombre());
                CampoDni.setText(String.valueOf(newSeleccion.getDni()));
                CampoTelefono.setText(String.valueOf(newSeleccion.getTelefono()));
                CampoCorreo.setText(newSeleccion.getEmail());
                CampoRol.setText(newSeleccion.getRol());
                CampoMiembro.setValue(newSeleccion.getEsmiembroConsejo());
            } else {
                ButtonActualizar.setDisable(true);
                ButtonEliminar.setDisable(true);
                CampoNombreCompeto.clear();
                CampoDni.clear();
                CampoTelefono.clear();
                CampoCorreo.clear();
                CampoRol.clear();
                CampoMiembro.setValue(null);
            }
            TablaInvolucrados.refresh();
            TablaInvolucrados.refresh();
        });
        ButtonActualizar.setOnAction((ActionEvent event) -> {
            // Obtener el expediente seleccionado en la tabla
            Involucrado datosInvolucrado = TablaInvolucrados.getSelectionModel().getSelectedItem();
            try {
                // Obtener el expediente seleccionado en el ListView CampoExpediente
                Expediente expedienteSeleccionado = CampoExpediente.getSelectionModel().getSelectedItem();

                // Corrección aquí: obtener el número de expediente del campo de texto
                datosInvolucrado.setNombre(CampoNombreCompeto.getText().toUpperCase());
                datosInvolucrado.setDni(Integer.parseInt(CampoDni.getText()));
                datosInvolucrado.setTelefono(Long.parseLong(CampoTelefono.getText()));
                datosInvolucrado.setEmail(CampoCorreo.getText());
                datosInvolucrado.setRol(CampoRol.getText());
                datosInvolucrado.setEsmiembroConsejo(CampoMiembro.getValue());
                datosInvolucrado.setExpediente(expedienteSeleccionado);
                
                //valida si ya hay una persona con mismo DNI
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                if (!datosInvolucrado.getNombre().matches("[a-zA-Z\\s]+")) {
                    alertError.setContentText("El Nombre de la Persona solo puede contener letrasy espacios");
                    alertError.showAndWait();
                    return;
                }
                if (!datosInvolucrado.getRol().matches("[a-zA-Z]+")) {
                    alertError.setContentText("El nombre del rol solo puede contener letras.");
                    alertError.showAndWait();
                    return;
                }
                // Verificar que el número telefonico sea solo números
                if (!CampoTelefono.getText().matches("\\d+")) {
                    alertError.setContentText("El número de telefono debe contener solo números.");
                    alertError.showAndWait();
                    return;
                }
                // Verificar que el número de DNi sea solo números
                if (!CampoDni.getText().matches("\\d+")) {
                    alertError.setContentText("El número de DNI debe contener solo números.");
                    alertError.showAndWait();
                    return;
                }
                // Validar que el número y dni de involucrado sea mayor que cero
                if (datosInvolucrado.getDni() <= 0 && datosInvolucrado.getTelefono() <= 0 ) {
                    alertError.setContentText("El Campo Dni y Número telefonico deben contener valores numericos positivos");
                    alertError.showAndWait();
                    return;
                }
                // Validar que no haya campos vacíos
                if (datosInvolucrado.getNombre().isEmpty() || datosInvolucrado.getDni() == 0 || datosInvolucrado.getTelefono() == 0 || datosInvolucrado.getEmail().isEmpty() || datosInvolucrado.getRol().isEmpty()||CampoMiembro==null||CampoExpediente == null) {
                    alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                    alertError.showAndWait();
                    return;
                }
                // Llamar al servicio para editar el expediente
                servicioInvolucrado.editarInvolucrado(datosInvolucrado.getIdInvolucrado(), datosInvolucrado.getNombre(),datosInvolucrado.getDni(), datosInvolucrado.getTelefono(), datosInvolucrado.getEmail(),datosInvolucrado.getRol(), datosInvolucrado.getEsmiembroConsejo(), datosInvolucrado.getExpediente());
                    
                // Refrescar la tabla con los nuevos datos de la base de datos
                TablaInvolucrados.refresh();
                // Después de editar, deseleccionar el elemento
                TablaInvolucrados.getSelectionModel().clearSelection();
            
                // Mostrar mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("El Involucrado se editó correctamente.");
            
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // En caso de error, mostrar mensaje de error genérico
                Alert alertError = new Alert(Alert.AlertType.INFORMATION);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede editar el involucrado");
        
                alertError.showAndWait();
            }
        });
    }
    @FXML
    void AccionActualizar(ActionEvent event) {

    }

    @FXML
    void AccionAgregar(ActionEvent event) {
        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
        alertSuccess.setTitle("Éxito");
        alertSuccess.setHeaderText(null);
        alertSuccess.setContentText("Los Datos del Involucrado se han agregado Correctamente");
    
        Alert alertError = new Alert(Alert.AlertType.INFORMATION);
        alertError.setTitle("Error");
        alertError.setHeaderText(null);
        alertError.setContentText("Error ,Los Datos del involucrado no se han agregado correctamente");
        
        String nombreCompletoIngresado;
        int dniIngresado;
        Long telefonoIngresado;
        String correoIngresado;
        String rolIngresado;
        Boolean esMiembroIngresado;
        
        try{ // Se obtienen los datos de los campos y se realiza la validación
            nombreCompletoIngresado = CampoNombreCompeto.getText().toUpperCase().trim();
            dniIngresado = Integer.parseInt(CampoDni.getText().trim());
            telefonoIngresado = Long.parseLong(CampoTelefono.getText().trim());
            correoIngresado = CampoCorreo.getText().trim();
            rolIngresado = CampoRol.getText().trim();
            esMiembroIngresado = CampoMiembro.getValue();

            //valida si ya hay una persona con mismo DNI
            if (servicioInvolucrado.involucradoExiste(dniIngresado)) {
                alertError.setContentText("El DNI Ingresado ya Existe");
                alertError.showAndWait();
                return;
            } 

            nombreCompletoIngresado = nombreCompletoIngresado.toUpperCase();
            // Validar que solo contenga letras y espacios.
            if (!nombreCompletoIngresado.matches("[a-zA-Z\\s]+")) {
                alertError.setContentText("El Nombre de la Persona solo puede contener letras y espacios.");
                alertError.showAndWait();
                return;
            }

            if (!rolIngresado.matches("[a-zA-Z]+")) {
                alertError.setContentText("El nombre del rol solo puede contener letras.");
                alertError.showAndWait();
                return;
            }
            // Verificar que el número telefonico sea solo números
            if (!CampoTelefono.getText().matches("\\d+")) {
                alertError.setContentText("El número de telefono debe contener solo números.");
                alertError.showAndWait();
                return;
            }
            // Verificar que el número de DNi sea solo números
            if (!CampoDni.getText().matches("\\d+")) {
                alertError.setContentText("El número de DNI debe contener solo números.");
                alertError.showAndWait();
                return;
            }
            // Validar que el número y dni de involucrado sea mayor que cero
            if (dniIngresado <= 0 && telefonoIngresado <= 0 ) {
                alertError.setContentText("El Campo Dni y Número telefonico deben contener valores numericos positivos");
                alertError.showAndWait();
                return;
            }
            // Validar que no haya campos vacíos
            if (nombreCompletoIngresado.isEmpty() || dniIngresado == 0 || telefonoIngresado == 0 || correoIngresado.isEmpty() || rolIngresado.isEmpty()||esMiembroIngresado==null) {
                alertError.setContentText("Por favor, complete todos los campos con los datos correctos.");
                alertError.showAndWait();
                return;
            }
        // Agregar el expediente si todos los datos son válidos
        Involucrado newInvolucrado = new Involucrado();
        newInvolucrado.setNombre(nombreCompletoIngresado);
        newInvolucrado.setDni(dniIngresado);
        newInvolucrado.setTelefono(telefonoIngresado);
        newInvolucrado.setEmail(correoIngresado);
        newInvolucrado.setRol(rolIngresado);
        newInvolucrado.setEsmiembroConsejo(esMiembroIngresado);
        
        // Obtener el expediente seleccionado en el ListView CampoExpediente
        Expediente expedienteSeleccionado = CampoExpediente.getSelectionModel().getSelectedItem();

        // Asignar el expediente seleccionado al nuevo involucrado
        newInvolucrado.setExpediente(expedienteSeleccionado);
        
        //se llama al servicio Involucrado con el metodo agregar Involucrado
        Long idInvolucrado= servicioInvolucrado.agregarInvolucrado(newInvolucrado.getNombre(),newInvolucrado.getDni(),newInvolucrado.getTelefono(),newInvolucrado.getEmail(),newInvolucrado.getRol(),newInvolucrado.getEsmiembroConsejo(),expedienteSeleccionado);
        newInvolucrado.setIdInvolucrado(idInvolucrado);
        TablaInvolucrados.getItems().add(newInvolucrado);
        // Mostrar mensaje de éxito
        alertSuccess.showAndWait();

        // Limpiar los campos de texto después de agregar el Expediente
        CampoNombreCompeto.clear();
        CampoDni.clear();
        CampoTelefono.clear();
        CampoCorreo.clear();
        CampoRol.clear();
        CampoMiembro.setValue(null);

        TablaInvolucrados.refresh();
    } catch (IllegalArgumentException e) {
        // Capturar excepción si hay datos inválidos
        alertError.setContentText("Error: " + e.getMessage());
        alertError.setContentText("Los campos estan incompletos o puesto datos erroneos, Revise los Campos de DNI, Telefono y Correo.");
        alertError.showAndWait();
        }
    }

    /*@FXML
    void AccionEliminar(ActionEvent event) {
        Involucrado datosInvolucrado = TablaInvolucrados.getSelectionModel().getSelectedItem();
            try {
                TablaInvolucrados.refresh();
                // Eliminar el Involucrado de la tabla
                servicioInvolucrado.eliminarInvolucrados(datosInvolucrado.getIdInvolucrado());
                TablaInvolucrados.getItems().remove(datosInvolucrado);   
                // Para mostrar un mensaje de éxito
                Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertSuccess.setTitle("Éxito");
                alertSuccess.setHeaderText(null);
                alertSuccess.setContentText("El Involucrado se eliminó correctamente.");
                alertSuccess.showAndWait();
            } catch (Exception e) {
                // Mostrar mensaje de que el expediente no se pudo eliminar
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setTitle("Error");
                alertError.setHeaderText(null);
                alertError.setContentText("No se puede eliminar el involucrado.");
                alertError.showAndWait();
            }
            TablaInvolucrados.refresh();
    }*/

    /*@FXML
    void AccionEliminar(ActionEvent event) {
        Involucrado datosInvolucrado = TablaInvolucrados.getSelectionModel().getSelectedItem();
        
        if (datosInvolucrado == null) {
            // Mostrar mensaje de advertencia si no se ha seleccionado ningún elemento
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setTitle("Advertencia");
            alertWarning.setHeaderText(null);
            alertWarning.setContentText("Por favor, seleccione un involucrado para eliminar.");
            alertWarning.showAndWait();
            return;
        }
    
        // Intentar eliminar el involucrado y obtener el resultado
        int resultado = servicioInvolucrado.eliminarInvolucrados(datosInvolucrado.getIdInvolucrado());
            
        // Manejar el resultado de la operación
        if (resultado == 0) {
                // Eliminación exitosa
            TablaInvolucrados.getItems().remove(datosInvolucrado);
            TablaInvolucrados.refresh();
                
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Éxito");
            alertSuccess.setHeaderText(null);
            alertSuccess.setContentText("El involucrado se eliminó correctamente.");
            alertSuccess.showAndWait();
        } else if (resultado == 1) {
                // El involucrado no se pudo eliminar debido a asistencia asociada
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("El involucrado no se puede eliminar porque tiene asistencia asociada.");
            alertError.showAndWait();
        } else if (resultado == 2) {
                // El involucrado no fue encontrado
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("El involucrado no fue encontrado.");
            alertError.showAndWait();
        }
        TablaInvolucrados.refresh();
    }*/
    @FXML
    void AccionEliminar(ActionEvent event) {
    // Obtener el `Involucrado` seleccionado de la tabla
    Involucrado datosInvolucrado = TablaInvolucrados.getSelectionModel().getSelectedItem();
    
    if (datosInvolucrado == null) {
        // Mostrar mensaje de advertencia si no se ha seleccionado ningún elemento
        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
        alertWarning.setTitle("Advertencia");
        alertWarning.setHeaderText(null);
        alertWarning.setContentText("Por favor, seleccione un involucrado para eliminar.");
        alertWarning.showAndWait();
        return;
    }

    // Confirmar la acción con el usuario
    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
    alertConfirmation.setTitle("Confirmación");
    alertConfirmation.setHeaderText(null);
    alertConfirmation.setContentText("¿Está seguro de que desea eliminar este involucrado y todas sus asistencias?");
    Optional<ButtonType> result = alertConfirmation.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        // Intentar eliminar el involucrado y manejar el resultado
        int resultado = servicioInvolucrado.eliminarInvolucrados(datosInvolucrado.getIdInvolucrado());
        
        // Manejar el resultado de la operación
        if (resultado == 0) {
            // Eliminación exitosa
            TablaInvolucrados.getItems().remove(datosInvolucrado);
            TablaInvolucrados.refresh();
            
            Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
            alertSuccess.setTitle("Éxito");
            alertSuccess.setHeaderText(null);
            alertSuccess.setContentText("El involucrado y sus asistencias se eliminaron correctamente.");
            alertSuccess.showAndWait();
        } else if (resultado == 1) {
            // El involucrado no se pudo eliminar debido a asistencia asociada
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("El involucrado no se puede eliminar porque tiene asistencia asociada.");
            alertError.showAndWait();
        } else if (resultado == 2) {
            // El involucrado no fue encontrado
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("El involucrado no fue encontrado.");
            alertError.showAndWait();
        } else {
            // Manejar otros posibles errores
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText(null);
            alertError.setContentText("Ocurrió un error inesperado al intentar eliminar el involucrado.");
            alertError.showAndWait();
        }
    }
}

    @FXML
    void AccionLimpiar(ActionEvent event) {
        CampoNombreCompeto.clear();
        CampoDni.clear();
        CampoTelefono.clear();
        CampoCorreo.clear();
        CampoRol.clear();
        CampoMiembro.setValue(null);
    }

    @FXML
    void AccionVolver(ActionEvent event)throws IOException {
        App.setRoot("ViewsHomeController");
    }

    @FXML
    void CampoBuscarDni(ActionEvent event) {

    }

    @FXML
    void CampoBuscarNombre(ActionEvent event) {

    }

    @FXML
    void IngresarDNI(ActionEvent event) {

    }

    @FXML
    void IngresarEmail(ActionEvent event) {

    }

    @FXML
    void IngresarRol(ActionEvent event) {

    }

    @FXML
    void IngresoNombreCompleto(ActionEvent event) {

    }

    @FXML
    void SeleccionMiembro(ActionEvent event) {

    }

    @FXML
    void buscarDni(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textoBusqueda = TextBuscador1.getText().trim();
    
            // Verificar si el texto de búsqueda es un número válido
            try {
                int dniInvolucrado = Integer.parseInt(textoBusqueda);
    
                // Realizar la búsqueda de expedientes según el número de expediente
                List<Involucrado> involucradosEncontrados = servicioInvolucrado.buscarInvolucradoPorDni(dniInvolucrado);
    
                // Limpiar la tabla antes de agregar los nuevos resultados
                TablaInvolucrados.getItems().clear();
                // Actualizar la tabla con los resultados de la búsqueda
                TablaInvolucrados.getItems().addAll( involucradosEncontrados );
            } catch (NumberFormatException e) {
                // Manejar el caso en que el texto de búsqueda no es un número válido
                System.out.println("El texto de búsqueda no es un número válido.");
                // Puedes mostrar un mensaje de error al usuario si lo deseas
            }
        }
        // Si el campo de búsqueda está vacío después de la pulsación de tecla, mostrar todos los datos de la tabla nuevamente
        if (event.getCode() == KeyCode.ENTER && TextBuscador1.getText().isEmpty()) {
            List<Involucrado> todosInvolucrado = servicioInvolucrado.listarInvolucrados();
            TablaInvolucrados.getItems().clear();
            TablaInvolucrados.getItems().addAll(todosInvolucrado );
        }
    }

    @FXML
    void buscarNombre(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String textoBusqueda = TextBuscador.getText().toUpperCase().trim();
    
            // Verificar si el texto de búsqueda es un número válido
            try {
                String nombreInvolucrado = textoBusqueda;
    
                // Realizar la búsqueda de involucrado según el Nombre en la tabla
                List<Involucrado> involucradosEncontrados = servicioInvolucrado.buscarInvolucradoPorNombre(nombreInvolucrado);
    
                // Limpiar la tabla antes de agregar los nuevos resultados
                TablaInvolucrados.getItems().clear();
                // Actualizar la tabla con los resultados de la búsqueda
                TablaInvolucrados.getItems().addAll( involucradosEncontrados );
            } catch (NumberFormatException e) {
                // Manejar el caso en que el texto de búsqueda no es un Nombre válido
                System.out.println("El texto de búsqueda no es un Nombre válido.");
                // Puedes mostrar un mensaje de error al usuario si lo deseas
            }
        }
        // Si el campo de búsqueda está vacío después de la pulsación de tecla, mostrar todos los datos de la tabla nuevamente
        if (event.getCode() == KeyCode.ENTER && TextBuscador.getText().isEmpty()) {
            List<Involucrado> todosInvolucrado = servicioInvolucrado.listarInvolucrados();
            TablaInvolucrados.getItems().clear();
            TablaInvolucrados.getItems().addAll(todosInvolucrado );
        }
    }
}
