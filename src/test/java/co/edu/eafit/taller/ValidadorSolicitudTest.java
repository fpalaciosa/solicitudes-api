package co.edu.eafit.taller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ValidadorSolicitudTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 17);
    private final ValidadorSolicitud validador = new ValidadorSolicitud(HOY);

    @Test
    void aceptaSolicitudConDatosCompletos() {
        assertTrue(validador.validar("RAD-PRUEBA-0001", HOY).isEmpty());
    }

    @Test
    void rechazaFechaDeRadicacionFutura() {
        assertEquals(List.of("FECHA_FUTURA"), validador.validar("RAD-PRUEBA-0001", HOY.plusDays(1)));
    }

    @Test
    void rechazaRadicadoConFormatoInvalido() {
        assertEquals(List.of("RADICADO_INVALIDO"), validador.validar("12345", HOY));
    }
}
