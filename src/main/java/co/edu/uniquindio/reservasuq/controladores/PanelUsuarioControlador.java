package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.controladores.observer.Observer;
import co.edu.uniquindio.reservasuq.modelo.*;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
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

public class PanelUsuarioControlador implements Initializable, Observer {

    @FXML
    private Tab TabCrearReserva;

    @FXML
    private Tab TabHistorialReservas;

    @FXML
    private TabPane TabPaneUsuario;
    @FXML
    private ComboBox<String> cbHora;

    @FXML
    private ComboBox<String> filtroBox;

    @FXML
    private Button cerrarSesion;

    @FXML
    private TableColumn<Instalacion, String> colCapacidadInstalacion;

    @FXML
    private TableColumn<Instalacion, String> colCostoInstalacion;

    @FXML
    private TableColumn<Instalacion, String> colHorarioInstalacion;

    @FXML
    private TableColumn<Instalacion, String> colTipoInstalacion;


    @FXML
    private TableColumn<Reserva, String> colFechaReserva;

    @FXML
    private TableColumn<Reserva, String> colHoraReserva;

    @FXML
    private TableColumn<Reserva, String> colTipoInstalacionReserva;

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

    private Persona personaActual;
    public PanelUsuarioControlador(){
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
        personaActual = Sesion.getInstanciaSesion().getPersona();
    }


    @FXML
    void cerrarSesion(ActionEvent event) {
            controladorPrincipal.cerrarVentana(cerrarSesion);
            controladorPrincipal.navegarVentana("/inicio.fxml", "Pantalla de inicio");
    }

    @FXML
    void crearReserva(ActionEvent event) {
        try {
            Instalacion instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();
            if (instalacionSeleccionada == null) {
                controladorPrincipal.mostrarAlerta("Por favor seleccione una instalación", Alert.AlertType.WARNING);
                return;
            }

            LocalDate fecha = fechaDate.getValue();
            LocalTime hora = LocalTime.parse(cbHora.getValue());

            // Validación de fecha y hora
            if (fecha == null || hora == null) {
                controladorPrincipal.mostrarAlerta("Por favor, complete la fecha y hora de la reserva", Alert.AlertType.WARNING);
                return;
            }

            // Intentar realizar la reserva
            controladorPrincipal.realizarReserva(personaActual, fecha, hora, instalacionSeleccionada);

            // Notificar y mostrar mensaje de confirmación
            notificar();
            controladorPrincipal.mostrarAlerta("Su reserva ha sido creada exitosamente. Verifique su correo para más detalles.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("No se pudo realizar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminarReserva(ActionEvent event) {
        try {
            Reserva reservaSeleccionada = tablaHistorial.getSelectionModel().getSelectedItem();
            if (reservaSeleccionada == null) {
                controladorPrincipal.mostrarAlerta("Por favor seleccione una reserva para cancelar", Alert.AlertType.WARNING);
                return;
            }

            controladorPrincipal.cancelarReserva(reservaSeleccionada);
            notificar(); // Método para actualizar la vista después de la cancelación

            controladorPrincipal.mostrarAlerta("Su reserva ha sido cancelada exitosamente. Verifique su correo para más detalles.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("No se pudo cancelar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }

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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbHora.setItems(FXCollections.observableArrayList(controladorPrincipal.generarHorarios()));
        //COLUMNAS TABLA INSTALACIONES
        colCapacidadInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colCostoInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCostoExterno())));
        colHorarioInstalacion.setCellValueFactory(cellData -> {
            List<Horario> horarios = cellData.getValue().getHorarios();
            // Convierte la lista de horarios en un solo String
            StringBuilder horariosStr = new StringBuilder();
            for (Horario horario : horarios) {
                horariosStr.append(horario.toString()).append("\n");
            }
            return new SimpleStringProperty(horariosStr.toString().trim());
        });
        colTipoInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoInstalacion().name().replace(" ", "")));

        //Cargar categorias en el ComboBox
        filtroBox.setItems(FXCollections.observableList(controladorPrincipal.listarInstalacionesCombo()));

        //Inicializar lista observable y cargar las instalaciones
        instalacionesObservable = FXCollections.observableArrayList();
        tablaInstalaciones.setItems(instalacionesObservable);
        cargarInstalaciones();

        reservasObservable = FXCollections.observableArrayList();
        tablaHistorial.setItems(reservasObservable);

        tablaInstalaciones.setOnMouseClicked(e -> {
            instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();

            if (instalacionSeleccionada != null) {
                controladorPrincipal.mostrarAlerta("Instalación seleccionada: " + instalacionSeleccionada.getTipoInstalacion(), Alert.AlertType.INFORMATION);
            } else {
                controladorPrincipal.mostrarAlerta("Seleccione una instalación de la tabla para reservar", Alert.AlertType.WARNING);
            }
        });

        //COLUMNAS TABLA HISTORIAL DE RESERVAS
        colTipoInstalacionReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstalacion().getTipoInstalacion().name().replace(" ", "")));
        colFechaReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaReserva().toString()));
        colHoraReserva.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHora().toString()));

        tablaHistorial.setOnMouseClicked(e -> {
            reservaSeleccionada = tablaHistorial.getSelectionModel().getSelectedItem();

            if (reservaSeleccionada != null) {
                controladorPrincipal.mostrarAlerta("Reserva Seleccionada: " + reservaSeleccionada.getInstalacion(), Alert.AlertType.INFORMATION);
            } else {
                controladorPrincipal.mostrarAlerta("Seleccione una reserva para cancelarla", Alert.AlertType.WARNING);
            }
        });

        labelNombre.setText( personaActual.getNombre() + "   Bienvenido al panel de usuario de reservas uq " );
        labelUsuario.setText( "Usted es: " + personaActual.getTipoUsuario().toString() );

    }
    private void cargarInstalaciones() {
        instalacionesObservable.setAll(controladorPrincipal.listarInstalaciones());
    }
    private void cargarReservas() {
        reservasObservable.setAll(controladorPrincipal.listarReservasPorPersona(personaActual.getCedula()));
    }

    @Override
    public void notificar() {
        cargarReservas();
    }
}

