package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import co.edu.uniquindio.reservasuq.servicio.ServiciosUq;
import co.edu.uniquindio.reservasuq.utils.EnvioEmail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        } catch (Exception e) {
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
    public ArrayList<String> listarInstalacionesCombo(){
        ArrayList<String> instalaciones = new ArrayList<>();
        instalaciones.add("PISCINA");
        instalaciones.add("GIMNASIO");
        instalaciones.add("CANCHA FUTBOL");
        instalaciones.add("CANCHA BALONCESTO");
        instalaciones.add("AULAS ESTUDIO");
        instalaciones.add("SALON EVENTOS");

        return instalaciones;
    }

        //HACE FALTA ESTE MÉTODO
    @Override
    public List<Reserva> listarReservasPorPersona(String cedulaPersona) {
        return null;
    }

    @Override
    public List<Instalacion> listarInstalaciones() {
        return instalaciones;
    }
    @Override
    public List<Instalacion> buscarInstalaciones(TipoInstalacion tipoInstalacion) throws Exception {
        List<Instalacion> instalacionesFiltradas = new ArrayList<>();

        if (tipoInstalacion == null) {
            throw new Exception("Debe seleccionar un tipo de instalación");
        }

        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getTipoInstalacion().equals(tipoInstalacion)) {
                instalacionesFiltradas.add(instalacion);
            }
        }

        return instalacionesFiltradas;
    }

    @Override
    public List<Reserva> buscarReservas(TipoInstalacion tipoInstalacion) throws Exception {
        List<Reserva> reservasFiltradas = new ArrayList<>();

        if (tipoInstalacion == null) {
            throw new Exception("Debe seleccionar un tipo de instalación");
        }

        for (Reserva reserva : reservas) {
            if (reserva.getInstalacion().equals(tipoInstalacion)) {
                reservasFiltradas.add(reserva);
            }
        }

        return reservasFiltradas;
    }


    @Override
    public void editarInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, List<Horario> horarios, String id) throws Exception {
        // Buscar la posición de la instalación en la lista
        int posInstalacion = obtenerInstalacion(id);
        if (posInstalacion == -1) {
            throw new Exception("No existe una instalación con el ID proporcionado");
        }
        if (capacidadMaxima <= 0) {
            throw new Exception("La capacidad máxima debe ser mayor a cero");
        }
        if (costoExterno < 0) {
            throw new Exception("El costo externo no puede ser negativo");
        }
        if (horarios == null || horarios.isEmpty()) {
            throw new Exception("La lista de horarios no puede estar vacía");
        }
        Instalacion instalacionGuardada = instalaciones.get(posInstalacion);
        instalacionGuardada.setTipoInstalacion(tipoInstalacion);
        instalacionGuardada.setCapacidadMaxima(capacidadMaxima);
        instalacionGuardada.setCostoExterno(costoExterno);
        instalacionGuardada.setHorarios(horarios);
        //Actualiza la nota en la lista de notas
        instalaciones.set(posInstalacion, instalacionGuardada);
    }
    // Método para verificar si una instalación está disponible en una fecha y hora específicas
    @Override
    public boolean estaDisponible(TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime hora) {
        for (Reserva reserva : reservas) {
            if (reserva.getInstalacion().equals(tipoInstalacion) &&
                    reserva.getFechaReserva().equals(fecha) &&
                    reserva.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void realizarReserva(TipoUsuario tipoUsuario, TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime hora, Instalacion instalacion, String correoInstitucional) throws Exception {
        Persona persona = obtenerUsuarioEmail(correoInstitucional);
        if (!estaDisponible(tipoInstalacion, fecha, hora)) {
            throw new Exception("La instalación no está disponible en la fecha y hora seleccionadas.");
        }

        double costo = tipoUsuario == tipoUsuario.EXTERNO ? instalacion.getCostoExterno() : 0;
        Reserva nuevaReserva = new Reserva(tipoUsuario, instalacion, fecha, hora, costo);
        reservas.add(nuevaReserva);
        String mensaje = String.format(
                "Estimado/a %s,\n\n" +
                        "Su reserva ha sido creada con éxito con los siguientes detalles:\n\n" +
                        "Tipo de Instalación: %s\n" +
                        "Fecha: %s\n" +
                        "Hora: %s\n" +
                        "Tipo de Usuario: %s\n" +
                        "Costo: $%.2f\n\n" +
                        "Gracias por utilizar nuestro sistema de reservas.\n" +
                        "Atentamente,\nEl equipo de Reservas",
                persona.getNombre(),
                tipoInstalacion,
                fecha.toString(),
                hora.toString(),
                tipoUsuario,
                costo
        );

        // Enviar correo electrónico con los detalles de la reserva
        enviarEmail(persona, mensaje, "Confirmación de Reserva");
    }

    public Persona obtenerUsuarioEmail(String correoInstitucional) throws Exception {
        for (Persona persona : personas) {
            if (persona.getCorreoInstitucional().equals(correoInstitucional)) {
                return persona;
            }
        }
        return null;
    }
    public void enviarEmail(Persona persona, String mensaje, String asunto) {
        String email = persona.getCorreoInstitucional();
        try {
            new EnvioEmail(email, asunto, mensaje).enviarNotificacion();
        } catch (Exception e) {
            System.out.println("Error al enviar correo electrónico a: " + email);
            e.printStackTrace();
        }
    }

    @Override
    public void cancelarReserva(String cedula, TipoInstalacion tipoInstalacion, LocalDate fecha) {

    }


    @Override
    public void crearInstalacion(TipoInstalacion tipoInstalacion, int capacidadMaxima, double costoExterno, List<Horario> horarios) throws Exception {
        // Validaciones
        if (tipoInstalacion == null) {
            throw new Exception("El tipo de instalación es obligatorio");
        }
        if (capacidadMaxima <= 0) {
            throw new Exception("La capacidad máxima debe ser mayor a cero");
        }
        if (costoExterno < 0) {
            throw new Exception("El costo externo no puede ser negativo");
        }
        if (horarios == null || horarios.isEmpty()) {
            throw new Exception("La lista de horarios no puede estar vacía");
        }

        // Creación de la instalación
        Instalacion instalacion = Instalacion.builder()
                .id(UUID.randomUUID().toString()) // Genera un id aleatorio
                .tipoInstalacion(tipoInstalacion)
                .capacidadMaxima(capacidadMaxima)
                .costoExterno(costoExterno)
                .horarios(horarios)
                .build();

        instalaciones.add(instalacion);
    }
@Override
    public void eliminarInstalacion(String id) throws Exception{
        int posNota = obtenerInstalacion(id);

        if(posNota == -1){
            throw new Exception("No existe el id proporcionado");
        }

        instalaciones.remove( instalaciones.get(posNota) );
    }
    private int obtenerInstalacion(String id){

        for (int i = 0; i < instalaciones.size(); i++) {
            if( instalaciones.get(i).getId().equals(id) ){
                return i;
            }
        }

        return -1;
    }
    @Override
    public List<String> generarHorarios() {
        List<String> horarios = new ArrayList<>();
        for (int i = 8; i < 18; i++) {
            if(i < 10){
                horarios.add("0" + i + ":00");
                horarios.add("0" + i + ":30");
            }else{
                horarios.add(i + ":00");
                horarios.add(i + ":30");
            }
        }
        return horarios;
    }


}
