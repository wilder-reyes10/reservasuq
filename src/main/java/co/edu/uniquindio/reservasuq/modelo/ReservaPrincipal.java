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

    //Método para registrar un Usuario en la plataforma
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
    //Método para que la persona pueda iniciar sesión

    @Override
    public Persona iniciarSesion(String correoInstitucional, String contrasena) throws Exception {
        // Verificar si el correo y la contraseña coinciden con las del administrador
        if (correoInstitucional.equals(ADMIN_EMAIL) && contrasena.equals(ADMIN_PASSWORD)) {
            return new Persona("12345", "Administrador", ADMIN_EMAIL, ADMIN_PASSWORD, TipoUsuario.ADMINISTRADOR);
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
    //Método para obtener un usuario con su cédula en la lista de personas

    @Override
    public Persona obtenerUsuario(String cedula) throws Exception {
        for (Persona persona : personas) {
            if (persona.getCedula().equals(cedula)) {
                return persona;
            }
        }
        return null;
    }

    //Método para listar las categorías en el comboBox
    @Override
    public ArrayList<String> listarOpciones() {
        ArrayList<String> categorias = new ArrayList<>();
        categorias.add("ESTUDIANTE");
        categorias.add("PROFESOR");
        categorias.add("PERSONAL ADMINISTRATIVO");
        categorias.add("EXTERNO");

        return categorias;
    }
    //Método para listar las instalaciones en el comboBox
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

    //Método para listar las reservas de cada persona con su cédula
    @Override
    public List<Reserva> listarReservasPorPersona(String cedulaPersona) {

        List<Reserva> historial = new ArrayList<>();

        for (Reserva reserva : reservas){
            if(reserva.getPersona().getCedula().equals(cedulaPersona)){
                historial.add(reserva);
            }
        }

        return historial;
    }
    //Método para listar todas las instalaciones creadas por el administrador

    @Override
    public List<Instalacion> listarInstalaciones() {
        return instalaciones;
    }

    //Método para buscar las instalaciones en la lista de instalaciones y retornar las instalaciones que se encontraron según el tipo de instalación
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
    //Método para crear instalaciones

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


    //Método para editar las instalaciones

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
    //Método para eliminar instalaciones
    @Override
    public void eliminarInstalacion(String id) throws Exception{
        int posNota = obtenerInstalacion(id);

        if(posNota == -1){
            throw new Exception("No existe el id proporcionado");
        }

        instalaciones.remove( instalaciones.get(posNota) );
    }

    //Método para obtener las instalaciones por su id en la lista de instalaciones
    private int obtenerInstalacion(String id){

        for (int i = 0; i < instalaciones.size(); i++) {
            if( instalaciones.get(i).getId().equals(id) ){
                return i;
            }
        }

        return -1;
    }

    // Método para verificar si una instalación está disponible en una fecha y hora específicas
    public boolean estaDisponible(Instalacion instalacion, LocalDate fecha, LocalTime hora) {

        if ( contarReservas(instalacion.getTipoInstalacion(), fecha, hora) >= instalacion.getCapacidadMaxima() ) {
            return false;
        }

        return true;
    }
    //Método para contar las reservas en la lista de reservas

    private int contarReservas(TipoInstalacion tipoInstalacion, LocalDate fecha, LocalTime hora){
        int contador = 0;
        for (Reserva reserva : reservas) {
            if (reserva.getInstalacion().getTipoInstalacion().equals(tipoInstalacion) &&
                    reserva.getFechaReserva().equals(fecha) &&
                    reserva.getHora().equals(hora)) {
                contador++;
            }
        }

        return contador;
    }

    //Método para realizar la reserva

    @Override
    public void realizarReserva(Persona persona, LocalDate fecha, LocalTime hora, Instalacion instalacion) throws Exception {

        // Validar que la fecha de reserva sea con al menos dos días de anticipación
        if (!esFechaValida(fecha)) {
            throw new Exception("La reserva debe realizarse con al menos dos días de anticipación.");
        }

        // Validar disponibilidad de la instalación
        if (!estaDisponible(instalacion, fecha, hora)) {
            throw new Exception("La instalación no está disponible en la fecha y hora seleccionadas.");
        }

        // Calcular costo según el tipo de usuario
        double costo = persona.getTipoUsuario() == TipoUsuario.EXTERNO ? instalacion.getCostoExterno() : 0;

        // Crear y guardar la reserva
        Reserva nuevaReserva = new Reserva(persona, instalacion, fecha, hora, costo);
        reservas.add(nuevaReserva);

        // Generar mensaje de confirmación
        String mensaje = String.format(
                        "Su reserva ha sido creada con éxito con los siguientes detalles:\n\n" +
                        "Tipo de Instalación: %s\n" +
                        "Fecha: %s\n" +
                        "Hora: %s\n" +
                        "Tipo de Usuario: %s\n" +
                        "Costo: $%.2f\n\n" +
                        "Gracias por utilizar nuestro sistema de reservas.\n" +
                        "Atentamente,\nEl equipo de Reservas",
                instalacion.getTipoInstalacion(),
                fecha.toString(),
                hora.toString(),
                persona.getTipoUsuario(),
                costo
        );

        // Enviar correo electrónico con los detalles de la reserva
        enviarEmail(persona, mensaje, "Confirmación de Reserva");
    }
    //Método para que el usuario pueda cancelar la reserva

    @Override
    public void cancelarReserva(Reserva reserva) throws Exception{
        if (!reservas.contains(reserva)) {
            throw new Exception("La reserva seleccionada no existe o ya ha sido cancelada.");
        }

        reservas.remove(reserva);
        String mensaje = String.format(
                "Su reserva ha sido cancelada con éxito.\n\n" +
                        "Tipo de Instalación: %s\n" +
                        "Fecha: %s\n" +
                        "Hora: %s\n\n" +
                        "Gracias por utilizar nuestro sistema de reservas.\n" +
                        "Atentamente,\nEl equipo de Reservas",
                reserva.getInstalacion().getTipoInstalacion(),
                reserva.getFechaReserva().toString(),
                reserva.getHora().toString()
        );

        // Enviar correo electrónico de confirmación de cancelación
        enviarEmail(reserva.getPersona(), mensaje, "Cancelación de Reserva");
    }

    //Método para verificar que la fecha en la que se haga la reserva sea válida
    private boolean esFechaValida(LocalDate fecha) {
        return fecha.isAfter(LocalDate.now().plusDays(1));
    }
    //Método para enviar el email con la información

    public void enviarEmail(Persona persona, String mensaje, String asunto) {
        String email = persona.getCorreoInstitucional();
        try {
            new EnvioEmail(email, asunto, mensaje).enviarNotificacion();
        } catch (Exception e) {
            System.out.println("Error al enviar correo electrónico a: " + email);
            e.printStackTrace();
        }
    }
    //Método para generar las horas
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
