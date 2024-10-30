package co.edu.uniquindio.reservasuq.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PanelAdministradorControlador {

    private final ControladorPrincipal controladorPrincipal;
    @FXML
    private Button buttonCerrarSesion;
    public PanelAdministradorControlador(){
        controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    public void gestionarInstalaciones(){
        controladorPrincipal.navegarVentana("/gestionarInstalaciones.fxml", "Gestion de instalaciones");
    }

    public  void  cerrarSesion(){
        controladorPrincipal.cerrarVentana(buttonCerrarSesion);
        controladorPrincipal.navegarVentana("/inicio.fxml", "Pantalla de inicio");

    }

}
