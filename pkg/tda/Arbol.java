package tda;

// Arbol Binario de Busqueda generico, adaptado del Arbol de la catedra.
// Se ordena por 'clave' (un int). Las claves menores van a la izquierda y las
// mayores O IGUALES van a la derecha, asi que se admiten claves repetidas.
// Para distinguir dos nodos con la misma clave se usa el 'id'.
//
// Uso en el sistema: Arbol<Producto>, con clave = stock e id = codigo.
public class Arbol<V> implements IArbol<V> {
    private Nodo<V> raiz;
    private int cant;

    public Arbol() {
        raiz = null;
        cant = 0;
    }

    // Inserta un elemento. Las claves iguales caen a la derecha (rama "else").
    @Override
    public void insertar(int clave, String id, V valor) {
        if (valor == null) {
            System.out.println("Error: no se puede insertar un valor nulo");
            return;
        }
        if (raiz == null) {
            raiz = new Nodo<V>(clave, id, valor);
            cant++;
            return;
        }
        Nodo<V> aux = raiz;
        boolean seguir = true;

        while (seguir) {
            if (clave < aux.clave) {
                if (aux.izq != null)
                    aux = aux.izq;
                else {
                    aux.izq = new Nodo<V>(clave, id, valor);
                    seguir = false;
                    cant++;
                }
            } else {
                if (aux.der != null)
                    aux = aux.der;
                else {
                    aux.der = new Nodo<V>(clave, id, valor);
                    seguir = false;
                    cant++;
                }
            }
        }
    }

    // Busca por clave. Si hay varias con la misma clave, sigue por la derecha
    // hasta encontrar la que tiene el id pedido.
    @Override
    public V buscar(int clave, String id) {
        Nodo<V> aux = raiz;
        while (aux != null) {
            if (clave == aux.clave) {
                if (aux.id.equals(id)) {
                    return aux.valor;
                }
                aux = aux.der;
            } else if (clave < aux.clave) {
                aux = aux.izq;
            } else {
                aux = aux.der;
            }
        }
        System.out.println("Error: no existe un elemento con clave " + clave + " e id " + id);
        return null;
    }

    @Override
    public void eliminar(int clave, String id) {
        if (raiz == null) {
            System.out.println("Error: el arbol esta vacio --> no hay nada para eliminar");
            return;
        }
        int cantAntes = cant;
        raiz = eliminarRecursivo(raiz, clave, id);
        if (cant == cantAntes) {
            System.out.println("Error: no existe un elemento con clave " + clave + " e id " + id);
        }
    }

    private Nodo<V> eliminarRecursivo(Nodo<V> actual, int clave, String id) {
        if (actual == null) {
            return null;
        }

        // Navegar por el arbol
        if (clave < actual.clave) {
            actual.izq = eliminarRecursivo(actual.izq, clave, id);
            return actual;
        }
        if (clave > actual.clave) {
            actual.der = eliminarRecursivo(actual.der, clave, id);
            return actual;
        }

        // Misma clave, pero puede no ser el elemento buscado. Como las claves
        // repetidas se insertaron a la derecha, se sigue buscando por ahi.
        if (!actual.id.equals(id)) {
            actual.der = eliminarRecursivo(actual.der, clave, id);
            return actual;
        }

        // Encontramos el nodo a eliminar

        // CASO 1 y 2: el nodo tiene un hijo o ninguno
        if (actual.izq == null) {
            cant--;
            return actual.der;
        }
        if (actual.der == null) {
            cant--;
            return actual.izq;
        }

        // CASO 3: el nodo tiene dos hijos.
        // Buscamos el sucesor inorden (el minimo del subarbol derecho). Ojo:
        // hay que copiar los TRES campos, no solo la clave, o el nodo quedaria
        // con el stock de un producto y el objeto de otro.
        Nodo<V> sucesor = encontrarMinimo(actual.der);
        actual.clave = sucesor.clave;
        actual.id = sucesor.id;
        actual.valor = sucesor.valor;
        // Eliminamos el sucesor inorden (ese borrado es el que descuenta cant)
        actual.der = eliminarRecursivo(actual.der, sucesor.clave, sucesor.id);
        return actual;
    }

    // Devuelve el NODO minimo (no solo la clave), para poder copiar clave, id y valor.
    private Nodo<V> encontrarMinimo(Nodo<V> nodo) {
        while (nodo.izq != null) {
            nodo = nodo.izq;
        }
        return nodo;
    }

    // ----- MEJORA: busqueda por rango -----

    // Muestra todos los elementos en los que la clave esta entre min y max, ordenados de
    // menor a mayor.
    @Override
    public void listarEnRango(int min, int max) {
        if (min > max) {
            System.out.println("Error: rango invalido (min mayor que max)");
            return;
        }
        if (raiz == null) {
            System.out.println("El arbol esta vacio --> no hay elementos para listar");
            return;
        }
        int encontrados = listarEnRangoRec(raiz, min, max);
        if (encontrados == 0) {
            System.out.println("  No hay elementos con clave entre " + min + " y " + max);
        }
    }

    // Devuelve cuantos elementos mostro, para poder avisar si el rango salio vacio.
    private int listarEnRangoRec(Nodo<V> nodo, int min, int max) {
        if (nodo == null) {
            return 0;
        }
        int encontrados = 0;
        // Solo bajo a la izquierda si ahi puede haber claves >= min
        if (nodo.clave > min) {
            encontrados = encontrados + listarEnRangoRec(nodo.izq, min, max);
        }
        // El nodo actual entra en el rango
        if (nodo.clave >= min && nodo.clave <= max) {
            System.out.println("  " + nodo.valor);
            encontrados++;
        }
        // Solo bajo a la derecha si ahi puede haber claves <= max
        if (nodo.clave < max) {
            encontrados = encontrados + listarEnRangoRec(nodo.der, min, max);
        }
        return encontrados;
    }

    // ----- Recorridos (igual que la catedra: reciben el nodo por parametro) -----

    @Override
    public void mostrarInorden(Nodo<V> elNodo) {
        if (elNodo == null) return;

        // Paso 1: visitar subarbol izquierdo
        mostrarInorden(elNodo.izq);

        // Paso 2: visitar nodo actual (raiz)
        System.out.print(elNodo.valor + " ");

        // Paso 3: visitar subarbol derecho
        mostrarInorden(elNodo.der);
    }

    @Override
    public void mostrarPreorden(Nodo<V> elNodo) {
        if (elNodo == null)
            return;
        System.out.print(" " + elNodo.valor);
        mostrarPreorden(elNodo.izq);
        mostrarPreorden(elNodo.der);
    }

    @Override
    public void mostrarPostorden(Nodo<V> elNodo) {
        if (elNodo == null)
            return;
        mostrarPostorden(elNodo.izq);
        mostrarPostorden(elNodo.der);
        System.out.print(" " + elNodo.valor);
    }

    // ----- Metricas del arbol -----

    @Override
    public int contarNodos() {
        return cant;
    }

    private int alturaRec(Nodo<V> nodo) {
        if (nodo == null) return 0;
        int hIzq = alturaRec(nodo.izq);
        int hDer = alturaRec(nodo.der);
        if (hIzq > hDer) return 1 + hIzq;
        return 1 + hDer;
    }

    @Override
    public int altura() {
        return alturaRec(raiz);
    }

    @Override
    public boolean esHoja(Nodo<V> elNodo) {
        if (elNodo != null && elNodo.izq == null && elNodo.der == null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean balanceadoRec(Nodo<V> nodo) {
        if (nodo == null) return true;
        int hIzq = alturaRec(nodo.izq);
        int hDer = alturaRec(nodo.der);
        int diferencia = hIzq - hDer;
        if (diferencia < 0) diferencia = -diferencia;
        return diferencia <= 1 && balanceadoRec(nodo.izq) && balanceadoRec(nodo.der);
    }

    @Override
    public boolean estaBalanceado() {
        return balanceadoRec(raiz);
    }

    private int contarHojasRec(Nodo<V> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (esHoja(nodo)) {
            return 1;
        }
        return contarHojasRec(nodo.izq) + contarHojasRec(nodo.der);
    }

    @Override
    public int contarHojas() {
        return contarHojasRec(raiz);
    }

    private void mostrarHojasRec(Nodo<V> nodo) {
        if (nodo == null) return;
        if (esHoja(nodo)) System.out.print(nodo.valor + " ");
        mostrarHojasRec(nodo.izq);
        mostrarHojasRec(nodo.der);
    }

    @Override
    public void mostrarHojas() {
        mostrarHojasRec(raiz);
        System.out.println();
    }

    @Override
    public boolean estaVacio() {
        return raiz == null;
    }

    @Override
    public Nodo<V> devolverRaiz() {
        return raiz;
    }
}
