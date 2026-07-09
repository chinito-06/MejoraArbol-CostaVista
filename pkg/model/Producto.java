package model;

public class Producto {
    private String codigo;
    private String nombre;
    private int stock;
    private int stockMinimo;
    private Ubicacion ubicacion;

    public Producto(String codigo, String nombre, int stock, int stockMinimo, Ubicacion ubicacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
    }

    public String obtenerCodigo() {
        return codigo;
    }

    public String obtenerNombre() {
        return nombre;
    }

    public int obtenerStock() {
        return stock;
    }

    public int obtenerStockMinimo() {
        return stockMinimo;
    }

    public Ubicacion obtenerUbicacion() {
        return ubicacion;
    }

    public void establecerStock(int nuevoStock) {
        this.stock = nuevoStock;
    }

    public void establecerUbicacion(Ubicacion nuevaUbicacion) {
        this.ubicacion = nuevaUbicacion;
    }

    public boolean esCritico() {
        return stock < stockMinimo;
    }

    public String toString() {
        return "Producto[" + codigo + ": " + nombre + ", stock " + stock + "]";
    }
}
