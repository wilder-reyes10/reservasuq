package co.edu.uniquindio.reservasuq.modelo;

import co.edu.uniquindio.reservasuq.modelo.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor

public class Persona {

    private String cedula;
    private String nombre;
    private String correoInstitucional;
    private String contrasena;
    private TipoUsuario tipoUsuario;

}
