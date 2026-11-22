package negocio;

import java.util.ArrayList;
import modelo.Movimiento;
import modelo.Disponibilidad;
import modelo.Servicio;

public class GestorReportes {
    private ArrayList<Movimiento> movimientos = new ArrayList<Movimiento>();
    private ArrayList<Disponibilidad> disponibilidades = new ArrayList<Disponibilidad>();
    private String error = "";

    public String getError() {
        return error;
    }

    public ArrayList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public ArrayList<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public Movimiento registrarMovimiento(
            Servicio servicio, String parada, String tipo,
            String recorrido, String usuario, int minutosAtras) {

        error = "";

        if (servicio == null ||
                !servicio.getRuta().getParadas().contains(parada)) {
            error = "Servicio o parada incorrectos";
            return null;
        }

        if (!tipo.equals("SALIDA") && !tipo.equals("LLEGADA")) {
            error = "Movimiento debe ser SALIDA o LLEGADA";
            return null;
        }

        if (recorrido.trim().isEmpty() ||
                usuario.trim().isEmpty() ||
                minutosAtras < 0 || minutosAtras > 10080) {
            error = "Datos vacios o tiempo fuera del rango de 0 a 10080 minutos";
            return null;
        }

        long instante = System.currentTimeMillis() - minutosAtras * 60000L;
        boolean salidaEncontrada = false;

        for (Movimiento anterior : movimientos) {
            if (anterior.getServicio() == servicio &&
                    anterior.getTipo().equals(tipo) &&
                    anterior.getParada().equals(parada) &&
                    Math.abs(anterior.getFechaHora() - instante) <= 120000L) {
                error = "Posible reporte duplicado en 2 minutos";
                return null;
            }

            if (anterior.getServicio() == servicio &&
                    anterior.getRecorrido().equalsIgnoreCase(recorrido)) {

                if (anterior.getTipo().equals(tipo)) {
                    error = "Ya existe ese movimiento para el mismo recorrido";
                    return null;
                }

                if (anterior.getTipo().equals("SALIDA") &&
                        anterior.getFechaHora() < instante) {
                    salidaEncontrada = true;
                }
            }
        }

        if (tipo.equals("LLEGADA") && !salidaEncontrada) {
            error = "Para registrar una llegada primero registre la salida del mismo recorrido";
            return null;
        }

        Movimiento nuevo = new Movimiento(
                movimientos.size() + 1, servicio, parada, tipo,
                recorrido, usuario, instante
        );

        movimientos.add(nuevo);
        return nuevo;
    }

    public boolean registrarDisponibilidad(
            Servicio servicio, String parada, String estado,
            int minutosAtras) {

        error = "";

        if (servicio == null ||
                !servicio.getRuta().getParadas().contains(parada)) {
            error = "Servicio o parada incorrectos";
            return false;
        }

        if ((!estado.equals("DISPONIBLE") &&
                !estado.equals("NO DISPONIBLE")) ||
                minutosAtras < 0 || minutosAtras > 10080) {
            error = "Estado o tiempo incorrectos";
            return false;
        }

        long instante = System.currentTimeMillis() - minutosAtras * 60000L;

        disponibilidades.add(
                new Disponibilidad(servicio, parada, estado, instante)
        );

        return true;
    }

    public String consultarDisponibilidad(Servicio servicio, String parada) {
        Disponibilidad ultima = null;

        for (Disponibilidad d : disponibilidades) {
            if (d.getServicio() == servicio &&
                    d.getParada().equals(parada)) {

                if (ultima == null ||
                        d.getFechaHora() > ultima.getFechaHora()) {
                    ultima = d;
                }
            }
        }

        if (ultima == null) {
            return "Disponibilidad desconocida: no hay reportes";
        }

        long antiguedad =
                (System.currentTimeMillis() - ultima.getFechaHora()) / 60000;

        if (antiguedad > 30) {
            return "Disponibilidad desconocida: ultimo reporte desactualizado";
        }

        return ultima.getEstado() +
                " (reportado hace " + antiguedad + " min, no confirmado)";
    }
}
