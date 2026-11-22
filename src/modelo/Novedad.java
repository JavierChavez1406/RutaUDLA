package modelo;

public class Novedad {
    private int id;
    private Ruta ruta;
    private String tipo;
    private String descripcion;
    private String estado;
    private long fechaHora;

    public Novedad(int id, Ruta ruta, String tipo,
                   String descripcion, long fechaHora) {
        this.id = id;
        this.ruta = ruta;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.estado = "Pendiente";
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

    public long getFechaHora() {
        return fechaHora;
    }

    public void resolver() {
        estado = "Resuelta";
    }
}
