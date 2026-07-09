package service;

import tda.*;
import model.*;

// Clase de gestion del centro de distribucion.
// Guarda las estructuras de datos como atributos y reune las operaciones
// del sistema en metodos. Por ahora cubre tres objetivos:
//   1) Localizacion de stock  -> usa el Diccionario.
//   2) Linea de expedicion    -> usa la Cola FIFO.
//   3) Trazabilidad           -> usa la Pila (registrar y deshacer movimientos).
//   4) Conexion de ubicaciones -> usa el Grafo (camino mas corto en el deposito).
//   5) Inventario critico      -> usa la Cola de Prioridad (producto con menos stock).
public class CentroLogistico { 
    // Se usa para invertir la prioridad: menos stock = mas urgente. Como la
    // cola pone la mayor prioridad al frente, la prioridad es TOPE - stock.
    private int TOPE_PRIORIDAD = 100000;

    private Diccionario<String, Producto> productos;
    private Cola<Pedido> lineaExpedicion;
    private Conjunto<String> codigosUsados;
    private Pila<Movimiento> trazabilidad;
    private GrafoMatrizAdyacencia<Ubicacion> mapaDeposito;
    private ColaPrioridad<Producto> inventarioCritico;

    public CentroLogistico(int capacidad) {
        this.productos = new Diccionario<String, Producto>(capacidad);
        this.lineaExpedicion = new Cola<Pedido>(capacidad);
        this.codigosUsados = new Conjunto<String>(capacidad);
        this.trazabilidad = new Pila<Movimiento>(capacidad);
        this.mapaDeposito = new GrafoMatrizAdyacencia<Ubicacion>(capacidad, false);
        this.inventarioCritico = new ColaPrioridad<Producto>(capacidad);
    }

    // ----- Objetivo 1: Localizacion de stock (Diccionario) -----

    // Da de alta un producto. El Conjunto de codigos usados garantiza la
    // unicidad: si el codigo ya esta registrado, no se agrega.
    public void agregarProducto(String codigo, Producto producto) {
        if (codigosUsados.pertenece(codigo)) {
            System.out.println("Error: codigo duplicado " + codigo + " --> no se agrego el producto");
            return;
        }
        codigosUsados.insertar(codigo);
        productos.insertar(codigo, producto);
        inventarioCritico.insertar(producto, TOPE_PRIORIDAD - producto.obtenerStock());
        System.out.println("Producto agregado: " + producto);
    }

    // Busca un producto por su codigo sin recorrer todo el catalogo.
    public Producto buscarProducto(String codigo) {
        return productos.recuperarValor(codigo);
    }

    // Devuelve la ubicacion de un producto a partir de su codigo.
    public Ubicacion buscarUbicacion(String codigo) {
        Producto p = productos.recuperarValor(codigo);
        if (p == null) {
            return null;
        }
        return p.obtenerUbicacion();
    }

    public void mostrarCatalogo() {
        productos.mostrar();
    }

    public void mostrarCodigosUsados() {
        codigosUsados.mostrar();
    }

    // ----- Objetivo 2: Linea de expedicion (Cola FIFO) -----

    // Marca un pedido como LISTO y lo encola para despachar.
    public void marcarPedidoListo(Pedido pedido) {
        if (pedido == null) {
            System.out.println("Error: pedido nulo --> no se encolo");
            return;
        }
        pedido.establecerEstado("LISTO");
        lineaExpedicion.encolar(pedido);
    }

    // Despacha el pedido mas antiguo de la cola (orden de llegada, FIFO).
    // Al despachar, los productos del pedido salen del deposito, asi que se
    // descuenta del stock la cantidad pedida de cada item (con actualizarStock,
    // que registra el movimiento y reordena el inventario critico).
    public Pedido despacharProximoPedido() {
        Pedido p = lineaExpedicion.desencolar();
        if (p == null) {
            return null;
        }
        p.establecerEstado("DESPACHADO");
        Producto[] items = p.obtenerItems();
        int[] cantidades = p.obtenerCantidades();
        for (int i = 0; i < p.obtenerCantidadItems(); i++) {
            actualizarStock(items[i].obtenerCodigo(), -cantidades[i]);
        }
        return p;
    }

    public void mostrarLineaExpedicion() {
        lineaExpedicion.mostrar();
    }

    // ----- Objetivo 3: Trazabilidad (Pila) -----

    // Cambia el stock de un producto. Antes de aplicar el cambio, registra un
    // Movimiento en la pila para poder deshacerlo. La cantidad puede ser
    // negativa (baja); no se permite que el stock quede por debajo de 0.
    // Al cambiar el stock cambia la urgencia, asi que se reordena el producto
    // en el inventario critico (Cola de Prioridad).
    public void actualizarStock(String codigo, int cantidad) {
        Producto p = productos.recuperarValor(codigo);
        if (p == null) {
            return;
        }
        if (p.obtenerStock() + cantidad < 0) {
            System.out.println("Error: el stock no puede quedar negativo --> no se aplico el cambio");
            return;
        }
        trazabilidad.apilar(new Movimiento("ACTUALIZAR_STOCK", codigo, p.obtenerStock()));
        p.establecerStock(p.obtenerStock() + cantidad);
        reordenarInventarioCritico(p);
        System.out.println("Stock actualizado: " + p);
    }

    // Deshace el ultimo movimiento registrado, restaurando el stock previo.
    public void deshacerUltimoMovimiento() {
        Movimiento m = trazabilidad.desapilar();
        if (m == null) {
            return;
        }
        Producto p = productos.recuperarValor(m.obtenerCodigoProducto());
        if (p == null) {
            return;
        }
        p.establecerStock(m.obtenerStockPrevio());
        reordenarInventarioCritico(p);
        System.out.println("Movimiento deshecho: " + p);
    }

    // Reubica un producto en el inventario critico segun su stock actual:
    // lo saca y lo vuelve a insertar con la prioridad actualizada.
    private void reordenarInventarioCritico(Producto p) {
        inventarioCritico.eliminarElemento(p);
        inventarioCritico.insertar(p, TOPE_PRIORIDAD - p.obtenerStock());
    }

    public void mostrarTrazabilidad() {
        trazabilidad.mostrar();
    }

    // ----- Objetivo 4: Conexion de ubicaciones (Grafo) -----

    // Agrega una ubicacion como vertice del mapa del deposito.
    public void agregarUbicacion(Ubicacion ubicacion) {
        mapaDeposito.insertarVertice(ubicacion);
    }

    // Conecta dos ubicaciones (arista no dirigida = se puede ir y volver).
    public void conectarUbicaciones(Ubicacion a, Ubicacion b) {
        mapaDeposito.insertarArista(a, b);
    }

    // Muestra el camino mas corto (menos saltos) entre dos ubicaciones.
    public void encontrarCamino(Ubicacion origen, Ubicacion destino) {
        mapaDeposito.caminoMasCorto(origen, destino);
    }

    public void mostrarMapa() {
        mapaDeposito.mostrarMatriz();
    }

    // ----- Objetivo 5: Inventario critico (Cola de Prioridad) -----

    // Devuelve el producto con menos stock (el mas urgente de reponer).
    public Producto productoMasCritico() {
        return inventarioCritico.frente();
    }

    public void mostrarInventarioCritico() {
        inventarioCritico.mostrar();
    }
}