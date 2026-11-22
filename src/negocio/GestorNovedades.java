package negocio;

import java.util.ArrayList;
import modelo.Ruta;
import modelo.Novedad;
import modelo.ObjetoOlvidado;

public class GestorNovedades {
    private ArrayList<Novedad> novedades = new ArrayList<Novedad>();
    private ArrayList<ObjetoOlvidado> objetos = new ArrayList<ObjetoOlvidado>();

    public ArrayList<Novedad> getNovedades() {
        return novedades;
    }

    public ArrayList<ObjetoOlvidado> getObjetos() {
        return objetos;
    }

    public Novedad registrarNovedad(
            Ruta ruta, String tipo, String descripcion,
            int minutosAtras) {

        if (ruta == null ||
                tipo.trim().isEmpty() ||
                descripcion.trim().isEmpty() ||
                minutosAtras < 0 || minutosAtras > 10080) {
            return null;
        }

        long fecha =
                System.currentTimeMillis() - minutosAtras * 60000L;

        Novedad n = new Novedad(
                novedades.size() + 1, ruta, tipo, descripcion, fecha
        );

        novedades.add(n);
        return n;
    }

    public boolean resolverNovedad(int id) {
        for (Novedad n : novedades) {
            if (n.getId() == id &&
                    n.getEstado().equals("Pendiente")) {
                n.resolver();
                return true;
            }
        }

        return false;
    }

    public ObjetoOlvidado registrarObjeto(
            Ruta ruta, String tipo, String descripcion,
            int minutosAtras) {

        if (ruta == null ||
                (!tipo.equals("OLVIDADO") &&
                        !tipo.equals("ENCONTRADO")) ||
                descripcion.trim().isEmpty() ||
                minutosAtras < 0 || minutosAtras > 10080) {
            return null;
        }

        long fecha =
                System.currentTimeMillis() - minutosAtras * 60000L;

        ObjetoOlvidado o = new ObjetoOlvidado(
                objetos.size() + 1, ruta, tipo, descripcion, fecha
        );

        objetos.add(o);
        return o;
    }

    public boolean cambiarEstadoObjeto(int id, String estado) {
        for (ObjetoOlvidado o : objetos) {
            if (o.getId() == id) {
                return o.cambiarEstado(estado);
            }
        }

        return false;
    }
}
