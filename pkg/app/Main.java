package app;

import model.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== CostaVista Logistics =====");
        CentroLogistico centro = new CentroLogistico(10);

        // Ubicaciones del deposito
        Ubicacion u1 = new Ubicacion("UB-01", "A", 1, 3);
        Ubicacion u2 = new Ubicacion("UB-02", "B", 2, 5);
        Ubicacion u3 = new Ubicacion("UB-03", "C", 1, 1);

        // Productos. Ojo: P002 y P004 tienen el MISMO stock (8), para probar
        // que el arbol maneja bien las claves repetidas.
        Producto p1 = new Producto("P001", "Lavandina 1L", 50, 10, u1);
        Producto p2 = new Producto("P002", "Jabon en polvo", 8, 15, u2);
        Producto p3 = new Producto("P003", "Esponja x3", 120, 20, u3);
        Producto p4 = new Producto("P004", "Trapo de piso", 8, 12, u1);

        // ===== Objetivo 1: Localizacion de stock (Diccionario) y unicidad (Conjunto) =====
        System.out.println("\n----- Alta de productos -----");
        centro.agregarProducto("P001", p1);
        centro.agregarProducto("P002", p2);
        centro.agregarProducto("P003", p3);
        centro.agregarProducto("P004", p4);   // mismo stock que P002

        System.out.println("\n----- Alta con codigo duplicado (Conjunto debe rechazarlo) -----");
        Producto repetido = new Producto("P001", "Lavandina 2L", 30, 10, u1);
        centro.agregarProducto("P001", repetido);

        System.out.println("\n----- Codigos usados (Conjunto de unicidad) -----");
        centro.mostrarCodigosUsados();

        System.out.println("\n----- Catalogo actual -----");
        centro.mostrarCatalogo();

        System.out.println("\n----- Buscar producto por codigo -----");
        System.out.println("P002 -> " + centro.buscarProducto("P002"));

        System.out.println("\n----- Localizar ubicacion por codigo -----");
        System.out.println("Ubicacion de P003 -> " + centro.buscarUbicacion("P003"));

        System.out.println("\n----- Buscar codigo inexistente (debe avisar) -----");
        Ubicacion noExiste = centro.buscarUbicacion("P999");
        System.out.println("Ubicacion de P999 -> " + noExiste);

        // ===== Objetivo 3: Trazabilidad (Pila) =====
        System.out.println("\n----- Stock inicial de P002 -----");
        System.out.println(centro.buscarProducto("P002"));

        System.out.println("\n----- Actualizar stock: baja valida (-3) -----");
        centro.actualizarStock("P002", -3);

        System.out.println("\n----- Actualizar stock: alta (+10) -----");
        centro.actualizarStock("P002", 10);

        System.out.println("\n----- Intentar baja que deja stock negativo (debe rechazarse) -----");
        centro.actualizarStock("P002", -1000);

        System.out.println("\n----- Historial de movimientos (Pila) -----");
        centro.mostrarTrazabilidad();

        System.out.println("\n----- Deshacer ultimo movimiento (vuelve antes del +10) -----");
        centro.deshacerUltimoMovimiento();

        System.out.println("\n----- Deshacer otro movimiento (vuelve antes del -3) -----");
        centro.deshacerUltimoMovimiento();

        System.out.println("\n----- Deshacer con la pila vacia (debe avisar) -----");
        centro.deshacerUltimoMovimiento();

        // ===== Objetivo 5: Inventario critico (Cola de Prioridad) =====
        System.out.println("\n----- Inventario critico (mas urgente = menos stock) -----");
        centro.mostrarInventarioCritico();
        System.out.println("Producto mas critico: " + centro.productoMasCritico());

        System.out.println("\n----- Bajar mucho el stock de P003 (pasa a ser el mas critico) -----");
        centro.actualizarStock("P003", -118);   // P003: 120 -> 2
        System.out.println("Producto mas critico ahora: " + centro.productoMasCritico());

        System.out.println("\n----- Reponer P003 (+200): deja de ser critico -----");
        centro.actualizarStock("P003", 200);     // P003: 2 -> 202
        System.out.println("Producto mas critico ahora: " + centro.productoMasCritico());

        // ===== MEJORA: Reporte de reposicion (Arbol Binario de Busqueda) =====
        // Stocks en este punto: P001=50, P002=8, P003=202, P004=8

        System.out.println("\n----- Catalogo ordenado por stock (recorrido inorden del arbol) -----");
        centro.mostrarCatalogoPorStock();

        System.out.println("\n----- Reposicion: productos con stock <= 20 -----");
        System.out.println("(P002 y P004 comparten stock 8: el arbol debe listar los dos)");
        centro.listarProductosParaReponer(20);

        System.out.println("\n----- Reposicion con umbral 5: no hay ninguno tan bajo -----");
        centro.listarProductosParaReponer(5);

        System.out.println("\n----- Reposicion con umbral 300: entran todos -----");
        centro.listarProductosParaReponer(300);

        System.out.println("\n----- Umbral negativo (debe avisar) -----");
        centro.listarProductosParaReponer(-5);

        System.out.println("\n----- Reponer P004 (+100): el arbol se reindexa -----");
        centro.actualizarStock("P004", 100);     // P004: 8 -> 108
        centro.mostrarCatalogoPorStock();
        System.out.println("Reposicion con stock <= 20 (P004 ya no aparece, P002 sigue):");
        centro.listarProductosParaReponer(20);

        System.out.println("\n----- Deshacer ese movimiento: P004 vuelve a stock 8 -----");
        centro.deshacerUltimoMovimiento();
        centro.mostrarCatalogoPorStock();
        System.out.println("Reposicion con stock <= 20 (P004 vuelve a aparecer):");
        centro.listarProductosParaReponer(20);

        // ===== Objetivo 2: Linea de expedicion (Cola FIFO) =====
        System.out.println("\n----- Despachar con la cola vacia (debe avisar) -----");
        Pedido vacio = centro.despacharProximoPedido();
        System.out.println("Pedido despachado -> " + vacio);

        System.out.println("\n----- Armar pedidos: agregarItem(producto, cantidad) -----");
        Pedido ped1 = new Pedido("PED-1", 5);
        ped1.agregarItem(p1, 5);   // 5 de P001 (hay 50)
        ped1.agregarItem(p2, 2);   // 2 de P002 (hay 8)

        Pedido ped2 = new Pedido("PED-2", 5);
        ped2.agregarItem(p3, 10);  // 10 de P003 (hay 202)

        System.out.println("\n----- Agregar item con stock insuficiente (debe rechazarse) -----");
        ped2.agregarItem(p2, 100); // pide 100 de P002, hay 8

        centro.marcarPedidoListo(ped1);
        centro.marcarPedidoListo(ped2);

        System.out.println("\n----- Cola de expedicion -----");
        centro.mostrarLineaExpedicion();

        System.out.println("\n----- Despachar en orden de llegada (FIFO, descuenta lo pedido) -----");
        Pedido despachado1 = centro.despacharProximoPedido();   // PED-1: -5 de P001, -2 de P002
        System.out.println("Despachado 1ro -> " + despachado1);
        Pedido despachado2 = centro.despacharProximoPedido();   // PED-2: -10 de P003
        System.out.println("Despachado 2do -> " + despachado2);

        System.out.println("\n----- El arbol quedo sincronizado despues del despacho -----");
        centro.mostrarCatalogoPorStock();
        System.out.println("Reposicion con stock <= 20 (P002 bajo a 6 al despachar PED-1):");
        centro.listarProductosParaReponer(20);

        // ===== Objetivo 4: Conexion de ubicaciones (Grafo) =====
        System.out.println("\n----- Armar el mapa del deposito -----");
        Ubicacion u4 = new Ubicacion("UB-04", "D", 3, 2);
        Ubicacion u5 = new Ubicacion("UB-05", "E", 1, 4);
        centro.agregarUbicacion(u1);
        centro.agregarUbicacion(u2);
        centro.agregarUbicacion(u3);
        centro.agregarUbicacion(u4);
        centro.agregarUbicacion(u5);

        // Conexiones (pasillos entre ubicaciones):
        //   u1 - u2 - u3 - u5
        //   u1 - u4 - u5
        centro.conectarUbicaciones(u1, u2);
        centro.conectarUbicaciones(u2, u3);
        centro.conectarUbicaciones(u3, u5);
        centro.conectarUbicaciones(u1, u4);
        centro.conectarUbicaciones(u4, u5);

        System.out.println("\n----- Camino mas corto de UB-01 a UB-05 (debe ir por u4: 2 saltos) -----");
        centro.encontrarCamino(u1, u5);

        System.out.println("\n===== Fin de la demostracion =====");
    }
}
