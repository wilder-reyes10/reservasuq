package co.edu.uniquindio.reservasuq.controladores;
import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Instalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class GestionarInstalacionesControlador {

    @FXML
    private ComboBox<TipoInstalacion> tipoInstalacionBox;

    @FXML
    private TextField buscarField;

    @FXML
    private TextField capacidadMField;

    @FXML
    private TableColumn<Instalacion, String> colCapMaxima;

    @FXML
    private TableColumn<Instalacion, String> colCostoExterno;

    @FXML
    private TableColumn<Instalacion, String> colHorarios;

    @FXML
    private TableColumn<Instalacion, String> colId;

    @FXML
    private TableColumn<Instalacion, String> colTipoInstalacion;

    @FXML
    private TextField costoExternoField;

    @FXML
    private ComboBox<?> filtroBox;

    @FXML
    private ComboBox<?> horariosBox;

    @FXML
    private TextField idField;

    @FXML
    private TableView<?> tablaContactos;

    @FXML
    private TextField diaSemana;

    @FXML
    private TextField horaInicio;

    @FXML
    private TextField horaFin;

    private List<Horario> horarios;

    private ControladorPrincipal controladorPrincipal;

    public GestionarInstalacionesControlador(){
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
        this.horarios = new ArrayList<>();
    }

    @FXML
    void anadirInstalacion(ActionEvent event) {

        controladorPrincipal.crearInstalacion(
                tipoInstalacionBox.getValue(),
                Integer.parseInt(capacidadMField.getText()),
                Double.parseDouble(costoExternoField.getText()),
                horarios
        );
    }

    @FXML
    void buscarInstalaciones(ActionEvent event) {

    }

    @FXML
    void editarInstalacion(ActionEvent event) {

    }

    @FXML
    void eliminarIntalacion(ActionEvent event) {

    }

    @FXML
    void mostrarInstalaciones(ActionEvent event) {

    }

    public void agregarHorario(ActionEvent actionEvent) {

        String dia = diaSemana.getText();
        String horaI = horaInicio.getText();
        String horaF = horaFin.getText();

        Horario horario = new Horario(
                dia,
                LocalTime.parse(horaI),
                LocalTime.parse(horaF)
        );

        horarios.add(horario);

    }

}
