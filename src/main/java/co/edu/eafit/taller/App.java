package co.edu.eafit.taller;

/** Punto de entrada. Con --salud responde como lo haria una prueba de humo. */
public class App {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--salud")) {
            System.out.println("solicitudes-api: OK");
            return;
        }
        System.out.println("solicitudes-api lista. Use --salud para la prueba de humo.");
    }
}
