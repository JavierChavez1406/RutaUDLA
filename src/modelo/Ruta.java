package modelo;

import java.util.ArrayList;

public class Ruta {
    private int id;
    private String nombre;
    private ArrayList<String> paradas;

    public Ruta(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        paradas = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<String> getParadas() {
        return paradas;
    }

    public void agregarParada(String parada) {
        paradas.add(parada);
    }

    public boolean permiteViaje(String origen, String destino) {
        int inicio = paradas.indexOf(origen);
        int fin = paradas.indexOf(destino);
        return inicio >= 0 && fin > inicio;
    }
}
