package modelo;

public class Favorito {
    private String usuario;
    private Ruta ruta;

    public Favorito(String usuario, Ruta ruta) {
        this.usuario = usuario;
        this.ruta = ruta;
    }

    public String getUsuario() {
        return usuario;
    }

    public Ruta getRuta() {
        return ruta;
    }
}
