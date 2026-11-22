package negocio;

import java.util.ArrayList;
import modelo.Ruta;
import modelo.Servicio;
import modelo.Movimiento;
import modelo.Favorito;

public class GestorConsultas {
    private GestorRutas rutas;
    private GestorReportes reportes;
    private ArrayList<Favorito> favoritos = new ArrayList<Favorito>();

    public GestorConsultas(GestorRutas rutas, GestorReportes reportes) {
        this.rutas = rutas;
        this.reportes = reportes;
    }

    public ArrayList<Servicio> buscarAlternativas(
            String origen, String destino) {

        ArrayList<Servicio> lista = new ArrayList<Servicio>();

        for (Servicio s : rutas.getServicios()) {
            if (s.getRuta().permiteViaje(origen, destino)) {
                lista.add(s);
            }
        }

        return lista;
    }

    public boolean guardarFavorito(String usuario, Ruta ruta) {
        if (ruta == null || usuario.trim().isEmpty()) {
            return false;
        }

        for (Favorito f : favoritos) {
            if (f.getUsuario().equalsIgnoreCase(usuario) &&
                    f.getRuta() == ruta) {
                return false;
            }
        }

        favoritos.add(new Favorito(usuario, ruta));
        return true;
    }

    public ArrayList<Favorito> buscarFavoritos(String usuario) {
        ArrayList<Favorito> lista = new ArrayList<Favorito>();

        for (Favorito f : favoritos) {
            if (f.getUsuario().equalsIgnoreCase(usuario)) {
                lista.add(f);
            }
        }

        return lista;
    }

    public int contarMovimientos(Ruta ruta, int minutos) {
        int total = 0;

        long desde =
                System.currentTimeMillis() - minutos * 60000L;

        for (Movimiento m : reportes.getMovimientos()) {
            if (m.getServicio().getRuta() == ruta &&
                    m.getFechaHora() >= desde) {
                total++;
            }
        }

        return total;
    }
}
