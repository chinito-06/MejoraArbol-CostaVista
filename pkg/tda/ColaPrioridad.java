package tda;

public class ColaPrioridad<T> implements IColaPrioridad<T> {
    private T[] datos;
    private int[] prioridades;
    private int cantidad;
    private int tamano;

    public ColaPrioridad(int tamano) {
        this.tamano = tamano;
        this.datos = (T[]) new Object[tamano];
        this.prioridades = new int[tamano];
        crear();
    }

    @Override
    public void crear() {
        this.cantidad = 0;
    }

    @Override
    public boolean estaVacia() {
        return cantidad == 0;
    }

    @Override
    public boolean estaLlena() {
        return cantidad == tamano;
    }

    // Inserta por desplazamiento entonces mayor prioridad queda al frente (indice 0)
    @Override
    public void insertar(T dato, int prioridad) {
        if (estaLlena()) {
            System.out.println("Error: cola de prioridad llena --> no se inserto");
            return;
        }
        int pos = cantidad;
        while (pos > 0 && prioridades[pos - 1] < prioridad) {
            datos[pos] = datos[pos - 1];
            prioridades[pos] = prioridades[pos - 1];
            pos--;
        }
        datos[pos] = dato;
        prioridades[pos] = prioridad;
        cantidad++;
    }

    // Saca y devuelve el elemento de mayor prioridad (el del frente).
    @Override
    public T eliminar() {
        if (estaVacia()) {
            System.out.println("Error: cola de prioridad vacia --> no hay elementos");
            return null;
        }
        T elemento = datos[0];
        for (int i = 0; i < cantidad - 1; i++) {
            datos[i] = datos[i + 1];
            prioridades[i] = prioridades[i + 1];
        }
        cantidad--;
        datos[cantidad] = null;
        return elemento;
    }

    // Saca un elemento puntual (no necesariamente el frente). Sirve para
    // reordenar: se elimina y luego se reinserta con la nueva prioridad.
    @Override
    public void eliminarElemento(T dato) {
        int pos = -1;
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].equals(dato)) {
                pos = i;
            }
        }
        if (pos == -1) {
            return;
        }
        for (int i = pos; i < cantidad - 1; i++) {
            datos[i] = datos[i + 1];
            prioridades[i] = prioridades[i + 1];
        }
        cantidad--;
        datos[cantidad] = null;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            System.out.println("Error: cola de prioridad vacia --> no hay frente");
            return null;
        }
        return datos[0];
    }

    @Override
    public void mostrar() {
        System.out.print("Inventario critico (mas urgente primero): ");
        for (int i = 0; i < cantidad; i++) {
            System.out.print("[" + datos[i] + ", prioridad " + prioridades[i] + "] - ");
        }
        System.out.println();
    }
}