package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import co.edu.uniquindio.reservasuq.servicio.ServiciosUq;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaPrincipal implements ServiciosUq {
    private static final String ADMIN_EMAIL = "admin@uq.edu.co"; // Email del administrador
    private static final String ADMIN_PASSWORD = "12345admin";   // Contraseña del administrador

    private final ArrayList<Persona> personas;
    private final ArrayList<Instalacion> instalaciones;
    private final ArrayList<Reserva> reservas;


    public ReservaPrincipal() {
        personas = new ArrayList<Persona>();
        instalaciones = new ArrayList<Instalacion>();
        reservas = new ArrayList<Reserva>();
    }
    @Override
    public Persona registrarUsuario(String cedula, String nombre, String correoInstitucional, String contrasena, TipoUsuario tipoUsuario) throws Exception {
        if (cedula == null || cedula.isBlank()) {
            throw new Exception("El número de cédula es obligatorio");
        }

        if (obtenerUsuario(cedula) != null) {
            throw new Exception("Ya existe un usuario con el número de identificación: " + cedula);
        }

        if (nombre == null || nombre.isBlank()) {
            throw new Exception("El nombre es obligatorio");
        }

        if (correoInstitucional == null || correoInstitucional.isBlank()) {
            throw new Exception("El correo es obligatorio");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new Exception("La contraseña es obligatoria");
        }
        Persona persona;
        try {
            persona = Persona.builder()
                    .cedula(cedula)
                    .nombre(nombre)
                    .correoInstitucional(correoInstitucional)
                    .contrasena(contrasena)
                    .tipoUsuario(tipoUsuario)
                    .build();

            personas.add(persona);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

        if (correoInstitucional != null && !correoInstitucional.isBlank()) {
            persona.setCorreoInstitucional(correoInstitucional);
        }
        return persona;

    }

    @Override
    public Persona iniciarSesion(String correoInstitucional, String contrasena) throws Exception {
        // Verificar si el correo y la contraseña coinciden con las del administrador
        if (correoInstitucional.equals(ADMIN_EMAIL) && contrasena.equals(ADMIN_PASSWORD)) {
            return new Persona("12345", "Administrador", ADMIN_EMAIL, ADMIN_PASSWORD, TipoUsuario.PERSONALADMINISTRATIVO);
        }

        // Si no es administrador, buscar el usuario en la lista de usuarios
        for (Persona persona : personas) {
            if (persona.getCorreoInstitucional().equals(correoInstitucional) && persona.getContrasena().equals(contrasena)) {
                return persona; // Usuario encontrado
            }
        }
        // Si no se encuentra el usuario, lanzar una excepción
        throw new Exception("Usuario no encontrado o credenciales incorrectas.");
    }

    @Override
    public Persona obtenerUsuario(String cedula) throws Exception {
        for (Persona persona : personas) {
            if (persona.getCedula().equals(cedula)) {
                return persona;
            }
        }
        return null;
    }

    @Override
    public List<Reserva> listarTodasReservas() {
        return null;
    }

    @Override
    public ArrayList<String> listarOpciones() {
            ArrayList<String> categorias = new ArrayList<>();
            categorias.add("ESTUDIANTE");
            categorias.add("PROFESOR");
            categorias.add("PERSONAL ADMINISTRATIVO");
            categorias.add("EXTERNO");

            return categorias;
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
