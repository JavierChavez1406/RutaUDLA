package interfaz;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import modelo.*;
import negocio.*;

public class MainConsola {

    private Scanner teclado = new Scanner(System.in);
    private GestorRutas rutas = new GestorRutas();
    private GestorReportes reportes = new GestorReportes();
    private GestorEstimaciones estimaciones =
            new GestorEstimaciones(reportes);
    private GestorNovedades novedades = new GestorNovedades();
    private GestorConsultas consultas =
            new GestorConsultas(rutas, reportes);
    private SimpleDateFormat formato =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean datosDemoCargados = false;

    private void titulo(String nombre) {
        System.out.println("\n==========================================");
        System.out.println("  RUTAUDLA | " + nombre);
        System.out.println("==========================================");
    }

    private String leer(String mensaje) {
        String valor = "";

        while (valor.trim().isEmpty()) {
            System.out.print(mensaje + ": ");
            valor = teclado.nextLine().trim();

            if (valor.isEmpty()) {
                System.out.println("Dato obligatorio. Intente otra vez.");
            }
        }

        return valor;
    }

    private int numero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje + " (" + minimo +
                    " a " + maximo + "): ");

            if (teclado.hasNextInt()) {
                int valor = teclado.nextInt();
                teclado.nextLine();

                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } else {
                teclado.nextLine();
            }

            System.out.println("Ingrese un numero entero valido.");
        }
    }

    private String fecha(long milisegundos) {
        return formato.format(new Date(milisegundos));
    }

    private void mostrarRutas() {
        for (Ruta r : rutas.getRutas()) {
            System.out.println("[" + r.getId() + "] " + r.getNombre());

            for (String parada : r.getParadas()) {
                System.out.println("    - " + parada);
            }
        }
    }

    private void mostrarServicios() {
        for (Servicio s : rutas.getServicios()) {
            String tarifa = "NO VERIFICADA";

            if (s.isTarifaVerificada()) {
                tarifa = "$" + String.format("%.2f", s.getTarifa());
            }

            System.out.println("[" + s.getId() + "] " +
                    s.getNombre() + " | " +
                    s.getRuta().getNombre() + " | " + tarifa);
        }
    }

    private Servicio pedirServicio() {
        mostrarServicios();

        int id = numero("Numero del servicio",
                1, rutas.getServicios().size());

        return rutas.buscarServicio(id);
    }

    private Ruta pedirRuta() {
        mostrarRutas();

        int id = numero("Numero de la ruta",
                1, rutas.getRutas().size());

        return rutas.buscarRuta(id);
    }

    private String pedirParada(Servicio servicio) {
        ArrayList<String> paradas = servicio.getRuta().getParadas();

        for (int i = 0; i < paradas.size(); i++) {
            System.out.println((i + 1) + ". " + paradas.get(i));
        }

        return paradas.get(
                numero("Parada", 1, paradas.size()) - 1);
    }

    private String pedirTipo(String tipo1, String tipo2) {
        System.out.println("1. " + tipo1 + "  2. " + tipo2);

        int opcion = numero("Elija tipo", 1, 2);

        if (opcion == 1) {
            return tipo1;
        }

        return tipo2;
    }

    // MODULO 1 - JUAN

    private void menuRutas() {
        titulo("RUTAS Y TARIFAS - JUAN SARABIA");

        System.out.println("1. Ver rutas");
        System.out.println("2. Buscar recorrido");
        System.out.println("3. Ver servicios");
        System.out.println("4. Calcular costo");
        System.out.println("5. Verificar tarifa");

        int opcion = numero("Opcion", 1, 5);

        if (opcion == 1) {
            mostrarRutas();
        }

        if (opcion == 2) {
            mostrarRutas();

            String origen = leer("Nombre exacto de parada origen");
            String destino = leer("Nombre exacto de parada destino");

            ArrayList<Ruta> lista =
                    rutas.buscarRecorridos(origen, destino);

            if (lista.isEmpty()) {
                System.out.println(
                        "No existe recorrido con ese origen y destino en ese sentido.");
            }

            for (Ruta r : lista) {
                System.out.println("Ruta encontrada: " + r.getNombre());
            }
        }

        if (opcion == 3) {
            mostrarServicios();
        }

        if (opcion == 4) {
            Servicio s = pedirServicio();
            int viajes = numero("Numero de viajes", 1, 999);

            double costo = rutas.calcularCosto(s, viajes);

            if (costo < 0) {
                System.out.println(
                        "No se puede calcular: tarifa NO verificada.");
            } else {
                System.out.printf("Costo estimado: $%.2f%n", costo);
            }
        }

        if (opcion == 5) {
            Servicio s = pedirServicio();

            int centavos = numero(
                    "Tarifa VERIFICADA en centavos (ej. 35 = $0.35)",
                    0, 100000);

            s.verificarTarifa(centavos / 100.0);

            System.out.println(
                    "Tarifa marcada como verificada por el OPERADOR de la demostracion. No es oficial.");
        }
    }

    // MODULO 2 - MARCOS

    private void menuReportes() {
        titulo("REPORTES COMUNITARIOS - MARCOS BETANCOURT");

        System.out.println("1. Reportar movimiento");
        System.out.println("2. Reportar disponibilidad");
        System.out.println("3. Consultar disponibilidad");
        System.out.println("4. Ver movimientos");

        int opcion = numero("Opcion", 1, 4);

        if (opcion == 1) {
            Servicio s = pedirServicio();
            String p = pedirParada(s);

            String tipo = pedirTipo("SALIDA", "LLEGADA");

            String recorrido = leer(
                    "ID de recorrido (ej. V1, mismo para salida y llegada)");

            String alias = leer("Alias del estudiante");

            int hace = numero(
                    "Hace cuantos minutos ocurrio", 0, 10080);

            Movimiento m = reportes.registrarMovimiento(
                    s, p, tipo, recorrido, alias, hace);

            if (m == null) {
                System.out.println("ERROR: " + reportes.getError());
            } else {
                System.out.println("Guardado movimiento #" +
                        m.getId() + " | " + fecha(m.getFechaHora()));
            }
        }

        if (opcion == 2) {
            Servicio s = pedirServicio();
            String p = pedirParada(s);

            String estado =
                    pedirTipo("DISPONIBLE", "NO DISPONIBLE");

            int hace = numero(
                    "Hace cuantos minutos se observo", 0, 10080);

            if (reportes.registrarDisponibilidad(
                    s, p, estado, hace)) {
                System.out.println(
                        "Estado comunitario registrado.");
            } else {
                System.out.println(
                        "ERROR: " + reportes.getError());
            }
        }

        if (opcion == 3) {
            Servicio s = pedirServicio();
            String p = pedirParada(s);

            System.out.println(
                    reportes.consultarDisponibilidad(s, p));
        }

        if (opcion == 4) {
            if (reportes.getMovimientos().isEmpty()) {
                System.out.println("No existen reportes.");
            }

            for (Movimiento m : reportes.getMovimientos()) {
                System.out.println(
                        "#" + m.getId() + " " +
                                m.getServicio().getNombre() + " " +
                                m.getTipo() + " | " +
                                m.getParada() + " | " +
                                fecha(m.getFechaHora()));
            }
        }
    }

    // MODULO 3 - JOSUE

    private void menuEstimaciones() {
        titulo("ESTIMACIONES DE TIEMPO - JOSUE TISILEMA");

        Servicio s = pedirServicio();

        double intervalo =
                estimaciones.calcularIntervalo(s);

        if (intervalo < 0) {
            System.out.println(
                    "Datos insuficientes: se requieren al menos 3 salidas comparables.");
            return;
        }

        System.out.printf("Intervalo promedio: %.1f minutos%n", intervalo);

        long salida = estimaciones.proximaSalida(s);

        if (salida < 0) {
            System.out.println(
                    "La proxima salida estimada ya vencio: no se mostrara como actual.");
            return;
        }

        System.out.println(
                "Proxima salida ESTIMADA: " + fecha(salida));

        System.out.println(
                "Espera ESTIMADA: " +
                        estimaciones.esperaMinutos(s) + " minutos");

        long llegada = estimaciones.proximaLlegada(s);

        if (llegada < 0) {
            System.out.println(
                    "Llegada: faltan al menos 2 recorridos completos validos.");
        } else {
            System.out.println(
                    "Llegada ESTIMADA: " + fecha(llegada));
        }

        System.out.println(
                "Aviso: NO es GPS ni un horario confirmado.");
    }

    // MODULO 4 - JAVIER

    private void menuNovedades() {
        titulo("NOVEDADES Y OBJETOS - JAVIER CHAVEZ");

        System.out.println("1. Reportar novedad");
        System.out.println("2. Resolver novedad");
        System.out.println("3. Ver novedades");
        System.out.println("4. Reportar objeto");
        System.out.println("5. Cambiar estado objeto");
        System.out.println("6. Ver objetos");

        int op = numero("Opcion", 1, 6);

        if (op == 1) {
            Ruta r = pedirRuta();

            String tipo = leer(
                    "Tipo (retraso, cambio, interrupcion...)");

            String descripcion =
                    leer("Descripcion de la novedad");

            int hace = numero(
                    "Hace cuantos minutos sucedio", 0, 10080);

            Novedad n = novedades.registrarNovedad(
                    r, tipo, descripcion, hace);

            if (n == null) {
                System.out.println("ERROR: datos invalidos");
            } else {
                System.out.println(
                        "Guardada novedad #" + n.getId() + " | Gravedad: " + n.getGravedad() + " | Pendiente");
                if (novedades.contarRetrasosActivos(r) >= 2) {
                    System.out.println("ALERTA: Esta ruta presenta retrasos reiterados.");
                }
            }
        }

        if (op == 2) {
            mostrarNovedades();

            int id = numero(
                    "Numero de novedad", 1, 999999);

            if (novedades.resolverNovedad(id)) {
                System.out.println("Novedad resuelta");
            } else {
                System.out.println(
                        "No se pudo cambiar: ID o estado incorrecto");
            }
        }

        if (op == 3) {
            mostrarNovedades();
        }

        if (op == 4) {
            Ruta r = pedirRuta();

            String tipo =
                    pedirTipo("OLVIDADO", "ENCONTRADO");

            String descripcion =
                    leer("Descripcion GENERAL (sin datos personales)");

            int hace = numero(
                    "Hace cuantos minutos sucedio", 0, 10080);

            ObjetoOlvidado o = novedades.registrarObjeto(
                    r, tipo, descripcion, hace);

            if (o == null) {
                System.out.println("ERROR: datos invalidos");
            } else {
                System.out.println(
                        "Guardado objeto #" + o.getId() + " | Pendiente");
                if (novedades.hayPosibleCoincidencia(r, tipo)) {
                    System.out.println("AVISO: Existe un reporte opuesto activo en esta misma ruta.");
                }
            }
        }

        if (op == 5) {
            mostrarObjetos();

            int id = numero(
                    "Numero de reporte", 1, 999999);

            String estado =
                    pedirTipo("Localizado", "Recuperado");

            if (novedades.cambiarEstadoObjeto(id, estado)) {
                System.out.println("Estado actualizado");
            } else {
                System.out.println(
                        "Cambio no permitido (primero Localizado y luego Recuperado)");
            }
        }

        if (op == 6) {
            mostrarObjetos();
        }
    }

    private void mostrarNovedades() {
        if (novedades.getNovedades().isEmpty()) {
            System.out.println("No hay novedades.");
        }

        for (Novedad n : novedades.getNovedades()) {
            System.out.println(
                    "#" + n.getId() + " | " +
                            n.getRuta().getNombre() + " | " +
                            n.getTipo() + " | " +
                            n.getEstado() + " | " +
                            n.getDescripcion());
        }
    }

    private void mostrarObjetos() {
        if (novedades.getObjetos().isEmpty()) {
            System.out.println("No hay objetos reportados.");
        }

        for (ObjetoOlvidado o : novedades.getObjetos()) {
            System.out.println(
                    "#" + o.getId() + " | " +
                            o.getRuta().getNombre() + " | " +
                            o.getTipo() + " | " +
                            o.getEstado() + " | " +
                            o.getDescripcion());
        }
    }

    // MODULO 5 - FELIPE

    private void menuConsultas() {
        titulo("CONSULTAS Y FAVORITOS - FELIPE COLLANTES");

        System.out.println("1. Ver panel de ruta");
        System.out.println("2. Comparar alternativas");
        System.out.println("3. Guardar favorito");
        System.out.println("4. Ver favoritos");
        System.out.println("5. Estadisticas");

        int op = numero("Opcion", 1, 5);

        if (op == 1) {
            Ruta r = pedirRuta();

            System.out.println("Ruta: " + r.getNombre());

            for (Servicio s : rutas.getServicios()) {
                if (s.getRuta() == r) {
                    System.out.println(
                            "Servicio: " + s.getNombre());

                    if (s.isTarifaVerificada()) {
                        System.out.println(
                                "Tarifa: $" + s.getTarifa());
                    } else {
                        System.out.println(
                                "Tarifa: sin verificar");
                    }

                    long salida =
                            estimaciones.proximaSalida(s);

                    if (salida < 0) {
                        System.out.println(
                                "Proxima salida: sin datos");
                    } else {
                        System.out.println(
                                "Proxima salida ESTIMADA: " +
                                        fecha(salida));
                    }
                }
            }

            for (Novedad n : novedades.getNovedades()) {
                if (n.getRuta() == r &&
                        n.getEstado().equals("Pendiente")) {
                    System.out.println(
                            "ALERTA: " + n.getDescripcion());
                }
            }
        }

        if (op == 2) {
            mostrarRutas();

            String origen =
                    leer("Parada origen exacta");

            String destino =
                    leer("Parada destino exacta");

            ArrayList<Servicio> alternativas =
                    consultas.buscarAlternativas(origen, destino);

            if (alternativas.isEmpty()) {
                System.out.println(
                        "No existen alternativas para ese trayecto.");
            }

            for (Servicio s : alternativas) {
                if (s.isTarifaVerificada()) {
                    System.out.println(
                            s.getNombre() + " | $" + s.getTarifa());
                } else {
                    System.out.println(
                            s.getNombre() + " | tarifa sin confirmar");
                }
            }
        }

        if (op == 3) {
            String alias = leer("Alias");
            Ruta r = pedirRuta();

            if (consultas.guardarFavorito(alias, r)) {
                System.out.println(
                        "Ruta favorita agregada");
            } else {
                System.out.println(
                        "Favorito repetido o dato incorrecto");
            }
        }

        if (op == 4) {
            ArrayList<Favorito> favoritos =
                    consultas.buscarFavoritos(leer("Alias"));

            if (favoritos.isEmpty()) {
                System.out.println(
                        "No hay favoritos guardados.");
            }

            for (Favorito f : favoritos) {
                System.out.println(
                        "Favorita: " + f.getRuta().getNombre());
            }
        }

        if (op == 5) {
            Ruta r = pedirRuta();

            int min = numero(
                    "Periodo: ultimos cuantos minutos", 1, 10080);

            System.out.println(
                    "Cantidad de reportes de movimiento: " +
                            consultas.contarMovimientos(r, min));

            for (Servicio s : rutas.getServicios()) {
                if (s.getRuta() == r) {
                    double intervalo =
                            estimaciones.calcularIntervalo(s);

                    if (intervalo < 0) {
                        System.out.println(
                                s.getNombre() +
                                        " | Intervalo: datos insuficientes");
                    } else {
                        System.out.println(
                                s.getNombre() +
                                        " | Intervalo promedio: " +
                                        intervalo + " min");
                    }
                }
            }
        }
    }

    // DATOS DE PRUEBA PARA LA EXPOSICION

    private void cargarDatosDemostracion() {
        if (datosDemoCargados) {
            System.out.println(
                    "Los datos de ejemplo ya fueron cargados. No se duplicaran.");
            return;
        }

        Servicio servicio = rutas.buscarServicio(3);
        Ruta ruta = rutas.buscarRuta(1);

        // Tarifa de PRUEBA, no oficial.
        servicio.verificarTarifa(0.35);

        reportes.registrarMovimiento(
                servicio, "Campus Granados", "SALIDA",
                "DEMO1", "Alumno", 130);

        reportes.registrarMovimiento(
                servicio, "Campus Granados", "SALIDA",
                "DEMO2", "Alumno", 70);

        reportes.registrarMovimiento(
                servicio, "Campus Granados", "SALIDA",
                "DEMO3", "Alumno", 10);

        reportes.registrarMovimiento(
                servicio, "Metro Jipijapa", "LLEGADA",
                "DEMO1", "Alumno", 110);

        reportes.registrarMovimiento(
                servicio, "Metro Jipijapa", "LLEGADA",
                "DEMO2", "Alumno", 50);

        reportes.registrarDisponibilidad(
                servicio, "Campus Granados", "DISPONIBLE", 5);

        novedades.registrarNovedad(
                ruta, "RETRASO", "Retraso de demostracion", 8);

        novedades.registrarObjeto(
                ruta, "OLVIDADO", "Mochila azul de prueba", 15);

        consultas.guardarFavorito("Alumno", ruta);

        datosDemoCargados = true;

        System.out.println(
                "Datos DEMOSTRATIVOS cargados: 3 salidas, 2 llegadas, disponibilidad, novedad, objeto y favorito.");

        System.out.println(
                "Los horarios generados son de ejemplo y NO oficiales.");
    }

    // MENU PRINCIPAL

    public void ejecutar() {
        titulo("SISTEMA COMUNITARIO (PROTOTIPO ACADEMICO)");

        System.out.println(
                "Datos demostrativos, sin GPS, sin datos oficiales; se guardan en memoria mientras se ejecuta.");

        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n1. Juan - Rutas y tarifas");
            System.out.println("2. Marcos - Reportes comunitarios");
            System.out.println("3. Josue - Estimacion de tiempos");
            System.out.println("4. Javier - Novedades y objetos");
            System.out.println("5. Felipe - Consultas y favoritos");
            System.out.println("6. Cargar DATOS DEMO para la defensa");
            System.out.println("0. Salir");

            opcion = numero("Seleccione modulo", 0, 6);

            switch (opcion) {
                case 1:
                    menuRutas();
                    break;

                case 2:
                    menuReportes();
                    break;

                case 3:
                    menuEstimaciones();
                    break;

                case 4:
                    menuNovedades();
                    break;

                case 5:
                    menuConsultas();
                    break;

                case 6:
                    cargarDatosDemostracion();
                    break;

                case 0:
                    System.out.println(
                            "Gracias por usar RutaUDLA.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new MainConsola().ejecutar();
    }
}
