package co.edu.uniquindio.reservasuq.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InicioControlador {
    private final ControladorPrincipal controladorPrincipal;
    public InicioControlador() {
        controladorPrincipal = ControladorPrincipal.getInstancia();
    }
    @FXML
    private Button iniciarSesion;
    @FXML
    private Button registrarse;

    public void registrarse() {
        controladorPrincipal.navegarVentana("/registro.fxml", "Registro");
        controladorPrincipal.cerrarVentana(registrarse);
    }

    public void iniciarSesion() {
        controladorPrincipal.navegarVentana("/iniciarSesion.fxml", "Iniciar sesión");
        controladorPrincipal.cerrarVentana(registrarse);
    }
}
