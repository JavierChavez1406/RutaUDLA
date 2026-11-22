package negocio;

import java.util.ArrayList;
import modelo.Movimiento;
import modelo.Servicio;

public class GestorEstimaciones {
    private GestorReportes reportes;

    public GestorEstimaciones(GestorReportes reportes) {
        this.reportes = reportes;
    }

    public double calcularIntervalo(Servicio servicio) {
        ArrayList<Long> salidas = new ArrayList<Long>();

        for (Movimiento m : reportes.getMovimientos()) {
            if (m.getServicio() == servicio &&
                    m.getTipo().equals("SALIDA")) {
                salidas.add(m.getFechaHora());
            }
        }

        // Ordenar las horas de salida.
        for (int i = 0; i < salidas.size(); i++) {
            for (int j = i + 1; j < salidas.size(); j++) {
                if (salidas.get(i) > salidas.get(j)) {
                    long aux = salidas.get(i);
                    salidas.set(i, salidas.get(j));
                    salidas.set(j, aux);
                }
            }
        }

        int cantidad = 0;
        double suma = 0;

        for (int i = 1; i < salidas.size(); i++) {
            long minutos =
                    (salidas.get(i) - salidas.get(i - 1)) / 60000;

            if (minutos >= 5 && minutos <= 240) {
                suma += minutos;
                cantidad++;
            }
        }

        if (cantidad < 2) return -1;

        return suma / cantidad;
    }

    public long proximaSalida(Servicio servicio) {
        double intervalo = calcularIntervalo(servicio);

        if (intervalo < 0) return -1;

        long ultima = -1;

        for (Movimiento m : reportes.getMovimientos()) {
            if (m.getServicio() == servicio &&
                    m.getTipo().equals("SALIDA") &&
                    m.getFechaHora() > ultima) {
                ultima = m.getFechaHora();
            }
        }

        long estimada = ultima + (long)(intervalo * 60000);

        if (estimada <= System.currentTimeMillis()) {
            return -1;
        }

        return estimada;
    }

    public long esperaMinutos(Servicio servicio) {
        long salida = proximaSalida(servicio);

        if (salida < 0) return -1;

        return (salida - System.currentTimeMillis() + 59999) / 60000;
    }

    public double calcularDuracion(Servicio servicio) {
        double total = 0;
        int cantidad = 0;

        for (Movimiento llegada : reportes.getMovimientos()) {
            if (llegada.getServicio() == servicio &&
                    llegada.getTipo().equals("LLEGADA")) {

                for (Movimiento salida : reportes.getMovimientos()) {
                    if (salida.getServicio() == servicio &&
                            salida.getTipo().equals("SALIDA") &&
                            salida.getRecorrido().equalsIgnoreCase(
                                    llegada.getRecorrido())) {

                        long mins =
                                (llegada.getFechaHora() -
                                        salida.getFechaHora()) / 60000;

                        if (mins >= 5 && mins <= 240) {
                            total += mins;
                            cantidad++;
                        }
                    }
                }
            }
        }

        if (cantidad < 2) return -1;

        return total / cantidad;
    }

    public long proximaLlegada(Servicio servicio) {
        long salida = proximaSalida(servicio);
        double duracion = calcularDuracion(servicio);

        if (salida < 0 || duracion < 0) return -1;

        return salida + (long)(duracion * 60000);
    }
}
