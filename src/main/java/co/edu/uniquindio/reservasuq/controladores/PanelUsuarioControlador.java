package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Instalacion;
import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class PanelUsuarioControlador implements Initializable {
    private String correoInstitucional;

    @FXML
    private Tab TabCrearReserva;

    @FXML
    private Tab TabHistorialReservas;

    @FXML
    private TabPane TabPaneUsuario;

    @FXML
    private Button btBuscarReservas;

    @FXML
    private Button btEditarReservas;

    @FXML
    private Button btEliminarReservas;

    @FXML
    private Button btListarReservas;

    @FXML
    private ComboBox<String> cbHora;

    @FXML
    private ComboBox<String> cbTipoInstalacion;
    @FXML
    private ComboBox<String> filtroBox;

    @FXML
    private Button cerrarSesion;

    @FXML
    private TableColumn<Instalacion, String> colCapacidad;

    @FXML
    private TableColumn<Instalacion, String> colCosto;

    @FXML
    private TableColumn<Reserva, String> colFechaReserva;

    @FXML
    private TableColumn<Reserva, String> colHoraReserva;

    @FXML
    private TableColumn<Instalacion, String> colHorario;

    @FXML
    private TableColumn<Instalacion, String> colTipoInstalacion;
    @FXML
    private TableColumn<Instalacion, String> colTipoInstalacion1;


    @FXML
    private Button crearReserva;

    @FXML
    private DatePicker fechaDate;

    @FXML
    private Label labelNombre;

    @FXML
    private Label labelUsuario;

    @FXML
    private TableView<Instalacion> tablaInstalaciones;

    @FXML
    private TableView<Reserva> tablaHistorial;
    private Instalacion instalacionSeleccionada;
    private Reserva reservaSeleccionada;
    private ObservableList<Instalacion> instalacionesObservable;
    private ObservableList<Reserva> reservasObservable;

    private ControladorPrincipal controladorPrincipal;
    public PanelUsuarioControlador(){
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    @FXML
    void buscarReservas(ActionEvent event) {
        String filtroInstalaciones = String.valueOf(cbTipoInstalacion.getSelectionModel().getSelectedItem());

        try {
            String filtroSinEspacios = filtroInstalaciones.replace(" ", "");
            // Convertir el String 'filtro' a TipoInstalacion
            TipoInstalacion tipoInstalacion = TipoInstalacion.valueOf(filtroSinEspacios);

            List<Reserva> reservasFiltradas = controladorPrincipal.buscarReservas(tipoInstalacion);
            tablaHistorial.setItems(FXCollections.observableArrayList(reservasFiltradas));
        } catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta("El filtro seleccionado no es válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("Error al buscar las instalaciones", Alert.AlertType.ERROR);
        }

    }

    @FXML
    void cerrarSesion(ActionEvent event) {
            controladorPrincipal.cerrarVentana(cerrarSesion);
            controladorPrincipal.navegarVentana("/inicio.fxml", "Pantalla de inicio");
    }

    @FXML
    void crearReserva(ActionEvent event) {
        try {
            // Validar que se haya seleccionado una instalación en la tabla
            Instalacion instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();
            if (instalacionSeleccionada == null) {
                    controladorPrincipal.mostrarAlerta("Por favor seleccione una instalación", Alert.AlertType.WARNING);
                return;
            }

            // Obtener la fecha y hora desde los campos de la interfaz
            LocalDate fecha = fechaDate.getValue();
            LocalTime hora = LocalTime.parse(cbHora.getValue());

            // Validación de fecha y hora
            if (fecha == null || hora == null) {
                controladorPrincipal.mostrarAlerta("Por favor, complete la fecha y hora de la reserva", Alert.AlertType.WARNING);
                return;
            }

            // Supongamos que el tipo de usuario se determina por contexto, aquí sería EXTERNO o INTERNO
            TipoUsuario tipoUsuario = TipoUsuario.EXTERNO;  // O el tipo que corresponda según el usuario

            // Llamar al método de realizar reserva en la clase principal
            controladorPrincipal.realizarReserva(tipoUsuario, instalacionSeleccionada.getTipoInstalacion(), fecha, hora, instalacionSeleccionada, correoInstitucional);

            // Mostrar mensaje de confirmación
            controladorPrincipal.mostrarAlerta("Su reserva ha sido creada exitosamente. Verifique su correo para más detalles.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            // Manejar posibles errores
            controladorPrincipal.mostrarAlerta("No se pudo realizar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void editarReserva(ActionEvent event) {

    }

    @FXML
    void eliminarReserva(ActionEvent event) {

    }
    @FXML
    void mostrar(ActionEvent event) {
        List<Instalacion> todasLasInstalaciones = controladorPrincipal.listarInstalaciones();

        // Actualiza la tabla con todos los contactos
        tablaInstalaciones.setItems(FXCollections.observableArrayList(todasLasInstalaciones));

    }
    @FXML
    void buscar(ActionEvent event) {
        String filtro = String.valueOf(filtroBox.getSelectionModel().getSelectedItem());

        try {
            String filtroSinEspacios = filtro.replace(" ", "");
            // Convertir el String 'filtro' a TipoInstalacion
            TipoInstalacion tipoInstalacion = TipoInstalacion.valueOf(filtroSinEspacios);

            List<Instalacion> instalacionesFiltradas = controladorPrincipal.buscarInstalaciones(tipoInstalacion);
            tablaInstalaciones.setItems(FXCollections.observableArrayList(instalacionesFiltradas));
        } catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta("El filtro seleccionado no es válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("Error al buscar las instalaciones", Alert.AlertType.ERROR);
        }


    }
//lISTAR TODAS LAS RESERVAS
@FXML
void listarReservas(ActionEvent event) {

}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbHora.setItems(FXCollections.observableArrayList(controladorPrincipal.generarHorarios()));
        //COLUMNAS TABLA INSTALACIONES
        //Asignar las propiedades de la nota a las columnas de la tabla
        colCapacidad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colCosto.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCostoExterno())));
        colHorario.setCellValueFactory(cellData -> {
            List<Horario> horarios = cellData.getValue().getHorarios();
            // Convierte la lista de horarios en un solo String
            StringBuilder horariosStr = new StringBuilder();
            for (Horario horario : horarios) {
                horariosStr.append(horario.toString()).append("\n");
            }
            return new SimpleStringProperty(horariosStr.toString().trim());
        });
        colTipoInstalacion1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoInstalacion().name().replace(" ", "")));

        //Cargar categorias en el ComboBox
        filtroBox.setItems(FXCollections.observableList(controladorPrincipal.listarInstalacionesCombo()));

        //Inicializar lista observable y cargar las instalaciones
        instalacionesObservable = FXCollections.observableArrayList();
        cargarInstalaciones();

        tablaInstalaciones.setOnMouseClicked(e -> {
            instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();

            if (instalacionSeleccionada != null) {
                controladorPrincipal.mostrarAlerta("Instalación seleccionada: " + instalacionSeleccionada.getTipoInstalacion(), Alert.AlertType.INFORMATION);
            } else {
                controladorPrincipal.mostrarAlerta("Seleccione una instalación de la tabla para reservar", Alert.AlertType.WARNING);
            }
        });

        //COLUMNAS TABLA HISTORIAL DE RESERVAS
        colTipoInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoInstalacion().name().replace(" ", "")));
        colFechaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva().toString()));
        colHoraReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHora().toString()));
        cbTipoInstalacion.setItems(FXCollections.observableList(controladorPrincipal.listarInstalacionesCombo()));

        tablaHistorial.setOnMouseClicked(e -> {
            reservaSeleccionada = tablaHistorial.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada != null) {
                controladorPrincipal.mostrarAlerta("Reserva Seleccionada: " + reservaSeleccionada.getInstalacion(), Alert.AlertType.INFORMATION);
            } else {
                controladorPrincipal.mostrarAlerta("Seleccione una reserva para editar o eliminar", Alert.AlertType.WARNING);
            }
        });

    }
    private void cargarInstalaciones() {
        instalacionesObservable.setAll(controladorPrincipal.listarInstalaciones());
        tablaInstalaciones.setItems(instalacionesObservable);
    }
    private void cargarReservas(String cedulaPersona) {
        reservasObservable.setAll(controladorPrincipal.listarReservasPorPersona(cedulaPersona));
        tablaHistorial.setItems(reservasObservable);
    }


    }

