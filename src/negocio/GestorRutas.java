package negocio;

import java.util.ArrayList;
import modelo.Ruta;
import modelo.Servicio;

public class GestorRutas {
    private ArrayList<Ruta> rutas = new ArrayList<Ruta>();
    private ArrayList<Servicio> servicios = new ArrayList<Servicio>();

    public GestorRutas() {
        // Recorridos de demostracion, no oficiales.
        Ruta r1 = new Ruta(1, "Campus Granados - Metro Jipijapa (DEMO)");
        r1.agregarParada("Campus Granados");
        r1.agregarParada("Parada de prueba");
        r1.agregarParada("Metro Jipijapa");

        Ruta r2 = new Ruta(2, "UDLA Park - UDLA Arena (DEMO)");
        r2.agregarParada("UDLA Park");
        r2.agregarParada("UDLA Arena");

        rutas.add(r1);
        rutas.add(r2);

        servicios.add(new Servicio(1, "Bus universitario", r1, 0, false));
        servicios.add(new Servicio(2, "Transporte Park - Arena", r2, 0, false));
        servicios.add(new Servicio(3, "Furgoneta independiente", r1, 0.35, false));
    }

    public ArrayList<Ruta> getRutas() {
        return rutas;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public Ruta buscarRuta(int id) {
        for (Ruta r : rutas) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    public Servicio buscarServicio(int id) {
        for (Servicio s : servicios) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public boolean paradaValida(Servicio s, String parada) {
        return s != null && s.getRuta().getParadas().contains(parada);
    }

    public ArrayList<Ruta> buscarRecorridos(String origen, String destino) {
        ArrayList<Ruta> encontradas = new ArrayList<Ruta>();

        for (Ruta r : rutas) {
            if (r.permiteViaje(origen, destino)) {
                encontradas.add(r);
            }
        }

        return encontradas;
    }

    public double calcularCosto(Servicio servicio, int viajes) {
        if (servicio == null ||
                !servicio.isTarifaVerificada() ||
                viajes <= 0) {
            return -1;
        }

        return servicio.getTarifa() * viajes;
    }
}
