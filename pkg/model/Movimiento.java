package model;

public class Movimiento {
    private String tipo;
    private String codigoProducto;
    private int stockPrevio;

    public Movimiento(String tipo, String codigoProducto, int stockPrevio) {
        this.tipo = tipo;
        this.codigoProducto = codigoProducto;
        this.stockPrevio = stockPrevio;
    }

    public String obtenerTipo() {
        return tipo;
    }

    public String obtenerCodigoProducto() {
        return codigoProducto;
    }

    public int obtenerStockPrevio() {
        return stockPrevio;
    }

    public String toString() {
        return "Movimiento[" + tipo + ": producto " + codigoProducto + ", stockPrevio " + stockPrevio + "]";
    }
}
