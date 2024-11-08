package co.edu.uniquindio.reservasuq.controladores;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.ToString;

import java.net.URL;
import java.util.ResourceBundle;
@ToString
public class RegistroControlador implements Initializable {
    private final ControladorPrincipal controladorPrincipal;
    public RegistroControlador(){
        controladorPrincipal = ControladorPrincipal.getInstancia();
    }

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCedula;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private ComboBox<String> tipoUsuarioCombo;

    public  void registrar(){
        try {
            controladorPrincipal.registrarUsuario(txtCedula.getText(), txtNombre.getText(), txtCorreo.getText(), txtContrasena.getText(),TipoUsuario.valueOf(tipoUsuarioCombo.getValue().replace(" ", "")));
            controladorPrincipal.mostrarAlerta("Usted se ha registrado con éxito a Reservas UQ.", Alert.AlertType.INFORMATION);
        }catch (Exception e){
            controladorPrincipal.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void volver(){
        controladorPrincipal.navegarVentana("/inicio.fxml", "Inicio");
        controladorPrincipal.cerrarVentana(txtCorreo);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tipoUsuarioCombo.setItems( FXCollections.observableList(controladorPrincipal.listarOpciones()) );

    }
}
