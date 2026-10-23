package co.edu.eafit.taller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Reglas de negocio de una solicitud (dominio puro: sin red, sin base de datos). */
public class ValidadorSolicitud {

    private final LocalDate hoy;

    /** Se inyecta la fecha de hoy para que las pruebas sean repetibles. */
    public ValidadorSolicitud(LocalDate hoy) {
        this.hoy = hoy;
    }

    public List<String> validar(String radicado, LocalDate fechaRadicacion) {
        List<String> errores = new ArrayList<>();
        if (radicado == null || !radicado.matches("RAD-[A-Z]+-\\d{4}")) {
            errores.add("RADICADO_INVALIDO");
        }
        if (fechaRadicacion == null) {
            errores.add("FECHA_REQUERIDA");
        } else if (fechaRadicacion.isAfter(hoy)) {
            errores.add("FECHA_FUTURA");
        }
        return errores;
    }
}
