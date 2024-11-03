package co.edu.uniquindio.reservasuq.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class Horario {
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    @Override
    public String toString() {
        return diaSemana + " " + horaInicio + " - " + horaFin;
    }
}
