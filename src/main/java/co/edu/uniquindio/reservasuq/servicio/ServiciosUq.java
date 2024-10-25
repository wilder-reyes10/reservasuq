package co.edu.uniquindio.reservasuq.servicio;

import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Reserva;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ServiciosUq {
    Persona registrarUsuario(String cedula, String nombre, String correoInstitucional, String contrasena, TipoUsuario tipoUsuario) throws Exception;
    Persona iniciarSesion(String correoInstitucional, String contrasena) throws Exception;
    Persona obtenerUsuario(String cedula) throws Exception;
    List<Reserva> listarTodasReservas();
    List<String> listarOpciones();


    List<Reserva> listarReservasPorPersona(String cedulaPersona);
    void verificarDisponibilidad(TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws Exception;

    void gestionarInstalacion(TipoInstalacion tipoInstalacion, int nuevaCapacidadMaxima, double nuevoCostoExterno) throws Exception;

    void realizarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) throws Exception;
    void cancelarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha);
    void verHistorialReservas(String cedula);
    void enviarEmailConfirmacion(String correoInstitucional, TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime horaInicio);
    void crearInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, LocalDateTime horario);
    void eliminarInstalacion(TipoInstalacion tipoInstalacion);
    void modificarHorariosInstalacion(TipoInstalacion tipoInstalacion, LocalTime nuevoHorarioInicio, LocalTime nuevoHorarioFin);


}
