package modelo;

public class Servicio {
    private int id;
    private String nombre;
    private Ruta ruta;
    private double tarifa;
    private boolean tarifaVerificada;

    public Servicio(int id, String nombre, Ruta ruta,
                    double tarifa, boolean tarifaVerificada) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.tarifa = tarifa;
        this.tarifaVerificada = tarifaVerificada;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public double getTarifa() {
        return tarifa;
    }

    public boolean isTarifaVerificada() {
        return tarifaVerificada;
    }

    public void verificarTarifa(double nuevaTarifa) {
        if (nuevaTarifa >= 0) {
            tarifa = nuevaTarifa;
            tarifaVerificada = true;
        }
    }
}
