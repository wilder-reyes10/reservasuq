package co.edu.uniquindio.reservasuq.servicio;

import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Instalacion;
import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ServiciosUq {
    Persona registrarUsuario(String cedula, String nombre, String correoInstitucional, String contrasena, TipoUsuario tipoUsuario) throws Exception;
    Persona iniciarSesion(String correoInstitucional, String contrasena) throws Exception;
    Persona obtenerUsuario(String cedula) throws Exception;
    List<Reserva> listarTodasReservas();
    List<String> listarOpciones();
    List<String> listarInstalacionesCombo();

    List<Instalacion> buscarInstalaciones(TipoInstalacion tipoInstalacion) throws Exception;

    List<Reserva> buscarReservas(TipoInstalacion tipoInstalacion) throws Exception;


    List<Reserva> listarReservasPorPersona(String cedulaPersona);
    List<Instalacion> listarInstalaciones();

    void editarInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, List<Horario> horarios, String id) throws Exception;

    void realizarReserva(Persona persona, LocalDate fecha, LocalTime hora, Instalacion instalacion) throws Exception;
    void cancelarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha);
    void crearInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, List<Horario> horarios) throws Exception;
    void eliminarInstalacion(String id) throws Exception;
    List<String> generarHorarios();

}
