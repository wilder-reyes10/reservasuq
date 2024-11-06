package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@AllArgsConstructor
@Setter
public class Reserva {
    private Persona persona;
    private Instalacion instalacion;
    private LocalDate fechaReserva;
    private LocalTime hora;
    private double costo;
}
