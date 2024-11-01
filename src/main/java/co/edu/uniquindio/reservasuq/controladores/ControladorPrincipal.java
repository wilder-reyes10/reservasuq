package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.modelo.ReservaPrincipal;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import co.edu.uniquindio.reservasuq.servicio.ServiciosUq;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ControladorPrincipal implements ServiciosUq {
    private final ReservaPrincipal reservaPrincipal;
    private ControladorPrincipal() {
        reservaPrincipal = new ReservaPrincipal();
    }
    public static ControladorPrincipal INSTANCIA;
    public static ControladorPrincipal getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new ControladorPrincipal();
        }
        return INSTANCIA;
    }
    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana){
        try {
            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);

            // Mostrar la nueva ventana
            stage.show();
            return loader;

        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }
    public ButtonType mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        return alert.getResult();
    }

    public void cerrarVentana(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @Override
    public Persona registrarUsuario(String cedula, String nombre, String correoInstitucional, String contrasena, TipoUsuario tipoUsuario) throws Exception {
        return reservaPrincipal.registrarUsuario(cedula, nombre, correoInstitucional, contrasena, tipoUsuario);
    }

    @Override
    public Persona iniciarSesion(String correoInstitucional, String contrasena) throws Exception {
        return reservaPrincipal.iniciarSesion(correoInstitucional, contrasena);
    }

    @Override
    public Persona obtenerUsuario(String cedula) throws Exception {
        return null;
    }

    @Override
    public List<Reserva> listarTodasReservas() {
        return reservaPrincipal.listarTodasReservas();
    }

    @Override
    public List<String> listarOpciones() {
        return reservaPrincipal.listarOpciones();
    }

    @Override
    public List<Reserva> listarReservasPorPersona(String cedulaPersona) {
        return null;
    }


    @Override
    public void editarInstalacion(TipoInstalacion tipoInstalacion, int nuevaCapacidadMaxima, double nuevoCostoExterno, LocalDateTime horario) throws Exception {

    }

    @Override
    public void realizarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws Exception {

    }

    @Override
    public void cancelarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha) {

    }


    @Override
    public void crearInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, List<Horario> horarios) {

    }

    @Override
    public void eliminarInstalacion(TipoInstalacion tipoInstalacion) {

    }

    @Override
    public void modificarHorariosInstalacion(TipoInstalacion tipoInstalacion, LocalTime nuevoHorarioInicio, LocalTime nuevoHorarioFin) {

    }
}
