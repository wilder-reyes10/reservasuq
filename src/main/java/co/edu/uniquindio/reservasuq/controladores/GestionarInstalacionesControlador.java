package co.edu.uniquindio.reservasuq.controladores;
import co.edu.uniquindio.reservasuq.modelo.Horario;
import co.edu.uniquindio.reservasuq.modelo.Instalacion;
import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class GestionarInstalacionesControlador implements Initializable {

    @FXML
    private ComboBox<TipoInstalacion> tipoInstalacionBox;

    @FXML
    private ComboBox<Horario> horariosIntalacion;

    @FXML
    private TextField capacidadMField;

    @FXML
    private TableColumn<Instalacion, String> colCapMaxima;

    @FXML
    private TableColumn<Instalacion, String> colCostoExterno;

    @FXML
    private TableColumn<Instalacion, String> colHorarios;


    @FXML
    private TableColumn<Instalacion, String> colTipoInstalacion;

    @FXML
    private TextField costoExternoField;

    @FXML
    private ComboBox<String> filtroBox;

    @FXML
    private TextField idField;

    @FXML
    private TableView<Instalacion> tablaInstalaciones;

    @FXML
    private TextField diaSemana;

    @FXML
    private TextField horaInicio;

    @FXML
    private TextField horaFin;

    private List<Horario> horarios;
    private ControladorPrincipal controladorPrincipal;
    private Instalacion instalacionSeleccionada;
    private ObservableList<Instalacion> instalacionesObservable;

    public GestionarInstalacionesControlador(){
        this.controladorPrincipal = ControladorPrincipal.getInstancia();
        this.horarios = new ArrayList<>();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Asignar las propiedades de la nota a las columnas de la tabla
        colCapMaxima.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colCostoExterno.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCostoExterno())));
        colHorarios.setCellValueFactory(cellData -> {
            List<Horario> horarios = cellData.getValue().getHorarios();
            // Convierte la lista de horarios en un solo String
            StringBuilder horariosStr = new StringBuilder();
            for (Horario horario : horarios) {
                horariosStr.append(horario.toString()).append("\n");
            }
            return new SimpleStringProperty(horariosStr.toString().trim());
        });
        colTipoInstalacion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoInstalacion().name().replace(" ", "")));

        //Cargar categorias en el ComboBox
        filtroBox.setItems(FXCollections.observableList(controladorPrincipal.listarInstalacionesCombo()));
        tipoInstalacionBox.setItems(FXCollections.observableArrayList(TipoInstalacion.values()));

        //Inicializar lista observable y cargar las notas
        instalacionesObservable = FXCollections.observableArrayList();
        tablaInstalaciones.setItems(instalacionesObservable);
        actualizarInstalaciones();

        //Evento click en la tabla
        tablaInstalaciones.setOnMouseClicked(e -> {
            // Obtener la instalación seleccionada
            instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();

            if (instalacionSeleccionada != null) {
                // Llenar campos de texto básicos
                capacidadMField.setText(String.valueOf(instalacionSeleccionada.getCapacidadMaxima()));
                costoExternoField.setText(String.valueOf(instalacionSeleccionada.getCostoExterno()));
                tipoInstalacionBox.setValue(instalacionSeleccionada.getTipoInstalacion());
                horariosIntalacion.setItems(FXCollections.observableArrayList(instalacionSeleccionada.getHorarios()));

                // Verificar si hay al menos un horario en la lista
                List<Horario> horarios = instalacionSeleccionada.getHorarios();
                if (!horarios.isEmpty()) {
                    // Obtener el primer horario
                    Horario primerHorario = horarios.get(0);
                    // Asignar valores de día de la semana, hora de inicio y hora de fin
                    diaSemana.setText(primerHorario.getDiaSemana());
                    horaInicio.setText(String.valueOf(primerHorario.getHoraInicio()));
                    horaFin.setText(String.valueOf(primerHorario.getHoraFin()));
                    this.horarios = horarios;
                } else {
                    // Si no hay horarios, limpiar los campos correspondientes
                    diaSemana.clear();
                    horaInicio.clear();
                    horaFin.clear();
                }
            }
        });

        horariosIntalacion.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem != null){
                diaSemana.setText(newItem.getDiaSemana());
                horaInicio.setText(String.valueOf(newItem.getHoraInicio()));
                horaFin.setText(String.valueOf(newItem.getHoraFin()));
            }
        });

    }
    @FXML
    void anadirInstalacion(ActionEvent event) throws Exception {

        try{

        controladorPrincipal.crearInstalacion(
                tipoInstalacionBox.getValue(),
                Integer.parseInt(capacidadMField.getText()),
                Double.parseDouble(costoExternoField.getText()),
                new ArrayList<>(horarios)
        );
            horarios.clear();
            limpiarCampos();
            actualizarInstalaciones();
            controladorPrincipal.mostrarAlerta("Instalación creada con éxito", Alert.AlertType.INFORMATION);
            limpiarCampos();
            //tablaInstalaciones.setItems( FXCollections.observableArrayList(controladorPrincipal.listarInstalaciones()) );
        }catch (Exception ex){
            controladorPrincipal.mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void actualizarInstalaciones() {
        instalacionesObservable.setAll(controladorPrincipal.listarInstalaciones());
    }

    public void cargarInstalaciones(List<Instalacion> filtro) {
        instalacionesObservable.setAll(filtro);
    }

    @FXML
    void buscarInstalaciones(ActionEvent event) {
        String filtro = filtroBox.getSelectionModel().getSelectedItem();

        try {
            String filtroSinEspacios = filtro.replace(" ", "");
            // Convertir el String 'filtro' a TipoInstalacion
            TipoInstalacion tipoInstalacion = TipoInstalacion.valueOf(filtroSinEspacios);

            List<Instalacion> instalacionesFiltradas = controladorPrincipal.buscarInstalaciones(tipoInstalacion);
            cargarInstalaciones(instalacionesFiltradas);
            //tablaInstalaciones.setItems(FXCollections.observableArrayList(instalacionesFiltradas));
        } catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta("El filtro seleccionado no es válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("Error al buscar las instalaciones", Alert.AlertType.ERROR);
        }

        limpiarCampos();
    }


    @FXML
    void editarInstalacion(ActionEvent event) {
        if(instalacionSeleccionada != null) {
            try {
                controladorPrincipal.editarInstalacion(
                        tipoInstalacionBox.getValue(),
                        Integer.parseInt(capacidadMField.getText()),
                        Double.parseDouble(costoExternoField.getText()),
                        horarios,
                        instalacionSeleccionada.getId() // Pasar el ID de la instalación seleccionada
                );

                limpiarCampos();
                actualizarInstalaciones();
                controladorPrincipal.mostrarAlerta("Instalación actualizada con éxito", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                controladorPrincipal.mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            controladorPrincipal.mostrarAlerta("Debe seleccionar una instalacion de la lista de Instalaciones", Alert.AlertType.WARNING);
        }

    }
    public void volver(){
        controladorPrincipal.navegarVentana("/panelAdministrador.fxml", "Panel del administrador");
        controladorPrincipal.cerrarVentana(horaFin);
    }

    @FXML
    void eliminarInstalacion(ActionEvent event) {
        try {
            // Verificar que haya una instalación seleccionada en la tabla
            Instalacion instalacionSeleccionada = tablaInstalaciones.getSelectionModel().getSelectedItem();

            if (instalacionSeleccionada == null) {
                controladorPrincipal.mostrarAlerta("Debe seleccionar una instalación para eliminar", Alert.AlertType.WARNING);
                return;
            }

            // Confirmar la eliminación antes de proceder
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de eliminar esta instalación?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                controladorPrincipal.eliminarInstalacion(instalacionSeleccionada.getId());
                limpiarCampos();
                actualizarInstalaciones();
                controladorPrincipal.mostrarAlerta("Instalación eliminada con éxito", Alert.AlertType.INFORMATION);
                //tablaInstalaciones.setItems(FXCollections.observableArrayList(controladorPrincipal.listarInstalaciones()));
            }

        } catch (Exception ex) {
            controladorPrincipal.mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void mostrarInstalaciones(ActionEvent event) {
            // Obtiene todas las instalaciones disponibles en el modelo
            //List<Instalacion> todasLasInstalaciones = controladorPrincipal.listarInstalaciones();
        actualizarInstalaciones();
            // Actualiza la tabla con todos los contactos
            //tablaInstalaciones.setItems(FXCollections.observableArrayList(todasLasInstalaciones));
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
        horariosIntalacion.setItems(FXCollections.observableArrayList(horarios));

    }
    private void limpiarCampos(){
        capacidadMField.clear();
        costoExternoField.clear();
        diaSemana.clear();
        horaInicio.clear();
        horaFin.clear();
        tipoInstalacionBox.setValue(null);

    }

}
