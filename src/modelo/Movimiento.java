package modelo;

public class Movimiento {
    private int id;
    private Servicio servicio;
    private String parada;
    private String tipo;
    private String recorrido;
    private String usuario;
    private long fechaHora;

    public Movimiento(int id, Servicio servicio, String parada,
                      String tipo, String recorrido, String usuario,
                      long fechaHora) {
        this.id = id;
        this.servicio = servicio;
        this.parada = parada;
        this.tipo = tipo;
        this.recorrido = recorrido;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
    }

    public int getId() {
        return id;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public String getParada() {
        return parada;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRecorrido() {
        return recorrido;
    }

    public String getUsuario() {
        return usuario;
    }

    public long getFechaHora() {
        return fechaHora;
    }
}
