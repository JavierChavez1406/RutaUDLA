package modelo;

public class Disponibilidad {
    private Servicio servicio;
    private String parada;
    private String estado;
    private long fechaHora;

    public Disponibilidad(Servicio servicio, String parada,
                          String estado, long fechaHora) {
        this.servicio = servicio;
        this.parada = parada;
        this.estado = estado;
        this.fechaHora = fechaHora;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public String getParada() {
        return parada;
    }

    public String getEstado() {
        return estado;
    }

    public long getFechaHora() {
        return fechaHora;
    }
}
