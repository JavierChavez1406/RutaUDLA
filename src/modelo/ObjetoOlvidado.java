package modelo;

public class ObjetoOlvidado {
    private int id;
    private Ruta ruta;
    private String tipo;
    private String descripcion;
    private String estado;
    private long fechaHora;

    public ObjetoOlvidado(int id, Ruta ruta, String tipo,
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

    public boolean cambiarEstado(String nuevo) {
        if (estado.equals("Pendiente") &&
                nuevo.equals("Localizado")) {
            estado = nuevo;
            return true;
        }

        if (estado.equals("Localizado") &&
                nuevo.equals("Recuperado")) {
            estado = nuevo;
            return true;
        }

        return false;
    }
}
