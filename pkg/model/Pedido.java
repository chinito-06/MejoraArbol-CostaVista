package model;

public class Pedido {
    private String id;
    private Producto[] items;
    private int[] cantidades;
    private int cantidadItems;
    private String estado;

    public Pedido(String id, int capacidadItems) {
        this.id = id;
        this.items = new Producto[capacidadItems];
        this.cantidades = new int[capacidadItems];
        this.cantidadItems = 0;
        this.estado = "PENDIENTE";
    }

    // Agrega un producto al pedido con la cantidad pedida. Valida que la
    // cantidad sea valida y que haya stock suficiente; si no, no lo agrega.
    public void agregarItem(Producto p, int cantidad) {
        if (cantidadItems == items.length) {
            System.out.println("Error: el pedido " + id + " esta lleno, no se puede agregar el item.");
            return;
        }
        if (cantidad <= 0) {
            System.out.println("Error: la cantidad pedida debe ser mayor a 0.");
            return;
        }
        if (cantidad > p.obtenerStock()) {
            System.out.println("Error: stock insuficiente de " + p.obtenerCodigo()
                + " (se pidieron " + cantidad + ", hay " + p.obtenerStock() + ") --> no se agrego al pedido.");
            return;
        }
        items[cantidadItems] = p;
        cantidades[cantidadItems] = cantidad;
        cantidadItems++;
    }

    public String obtenerId() {
        return id;
    }

    public Producto[] obtenerItems() {
        return items;
    }

    public int[] obtenerCantidades() {
        return cantidades;
    }

    public int obtenerCantidadItems() {
        return cantidadItems;
    }

    public String obtenerEstado() {
        return estado;
    }

    public void establecerEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String toString() {
        return "Pedido[" + id + ": estado " + estado + ", items " + cantidadItems + "]";
    }
}
