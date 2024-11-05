package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.Persona;
import co.edu.uniquindio.reservasuq.modelo.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class IniciarSesionControlador {
    @FXML
    private TextField txtEmail; // Campo de texto para el email
    @FXML
    private PasswordField txtContrasena; // Campo de texto para la contraseña
    @FXML
    private CheckBox txtValidar; // CheckBox para validar si es administrador

    private final ControladorPrincipal controladorPrincipal;
    public IniciarSesionControlador(){
        controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    // Método para manejar el evento de inicio de sesión
    public void iniciarSesion(ActionEvent event) {
        String email = "admin@uq.edu.co";//txtEmail.getText();
        String contrasena = "12345admin";//txtContrasena.getText();
        boolean esAdmin = true; // txtValidar.isSelected(); // Verificar si la CheckBox está marcada (para admin)

        try {
            // Llamar al método iniciarSesion de ReservaPrincipal
            Persona persona = controladorPrincipal.iniciarSesion(email, contrasena);

            // Verificar si es un administrador
            if (esAdmin && persona.getNombre().equals("Administrador")) {
                controladorPrincipal.navegarVentana("/panelAdministrador.fxml", "Panel Administrador");
                controladorPrincipal.cerrarVentana(txtEmail);
            } else if (!esAdmin && persona != null) {
                Sesion.getInstanciaSesion().setPersona(persona);
                controladorPrincipal.navegarVentana("/panelUsuario.fxml", "Panel Usuario");
                controladorPrincipal.cerrarVentana(txtEmail);
            } else {
                controladorPrincipal.mostrarAlerta("Credenciales incorrectas", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void volver(){
        controladorPrincipal.navegarVentana("/inicio.fxml", "Inicio");
        controladorPrincipal.cerrarVentana(txtEmail);
    }
    }
