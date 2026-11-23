package modelo;

public class Novedad {
    private int id;
    private Ruta ruta;
    private String tipo;
    private String descripcion;
    private String estado;
    private String gravedad;
    private long fechaHora;

    public Novedad(int id, Ruta ruta, String tipo,
                   String descripcion, long fechaHora) {
        this.id = id;
        this.ruta = ruta;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.estado = "Pendiente";
        this.gravedad = calcularGravedad(tipo);
    }

    private String calcularGravedad(String tipoNovedad) {
        String t = tipoNovedad.trim().toUpperCase();
        if (t.equals("INTERRUPCION") || t.equals("ACCIDENTE") || t.equals("SUSPENSION")) {
            return "CRITICA";
        }
        if (t.equals("RETRASO") || t.equals("DESVIO") || t.equals("TRAFICO")) {
            return "MODERADA";
        }
        return "LEVE";
    }

    public int getId() {
        return id;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getGravedad() {
        return gravedad;
    }

    public long getFechaHora() {
        return fechaHora;
    }

    public void resolver() {
        estado = "Resuelta";
    }
}
