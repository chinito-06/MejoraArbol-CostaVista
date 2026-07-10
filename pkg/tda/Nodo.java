package tda;

// Nodo del Arbol Binario de Busqueda.
// Guarda tres cosas:
//   clave -> es el valor por el que se ORDENA el arbol. En el sistema es el
//            stock del producto.
//   id    -> IDENTIFICA al elemento cuando dos nodos comparten la misma clave
//            (dos productos pueden tener el mismo stock). En el sistema es el
//            codigo del producto. Para el arbol es un texto cualquiera: no
//            sabe ni le importa que representa.
//   valor -> el dato guardado. En el sistema es el Producto completo.
public class Nodo<V> {
    int clave;
    String id;
    V valor;
    Nodo<V> izq, der;

    public Nodo(int clave, String id, V valor) {
        this.clave = clave;
        this.id = id;
        this.valor = valor;
        this.izq = null;
        this.der = null;
    }
}
