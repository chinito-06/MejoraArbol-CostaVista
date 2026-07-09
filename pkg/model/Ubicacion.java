package model;

public class Ubicacion {
    private String id;
    private String pasillo;
    private int estanteria;
    private int posicion;

    public Ubicacion(String id, String pasillo, int estanteria, int posicion) {
        this.id = id;
        this.pasillo = pasillo;
        this.estanteria = estanteria;
        this.posicion = posicion;
    }

    public String obtenerId() {
        return id;
    }

    public String obtenerPasillo() {
        return pasillo;
    }

    public int obtenerEstanteria() {
        return estanteria;
    }

    public int obtenerPosicion() {
        return posicion;
    }

    public String toString() {
        return "Ubicacion[" + id + ": pasillo " + pasillo + ", estanteria " + estanteria + ", posicion " + posicion + "]";
    }
}
