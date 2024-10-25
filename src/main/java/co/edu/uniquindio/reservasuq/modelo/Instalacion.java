package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoInstalacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor

public class Instalacion {
    private String nombre;
    private int capacidadMaxima;
    private List<Reserva> reservas;
    private double costoExterno;
    private TipoInstalacion tipoInstalacion;
}
