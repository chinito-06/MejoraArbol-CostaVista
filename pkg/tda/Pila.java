package tda;

public class Pila<T> implements IPila<T> {
    private T[] datos;
    private int tope;
    private int max;

    public Pila(int max) {
        this.max = max;
        this.datos = (T[]) new Object[max];
        this.tope = -1;
    }

    @Override
    public void apilar(T elemento) {
        if (estaLlena()) {
            System.out.println("Error: la pila esta llena --> no se pudo apilar");
            return;
        }
        tope++;
        datos[tope] = elemento;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            System.out.println("Error: pila vacia --> no hay elementos para desapilar");
            return null;
        }
        T elemento = datos[tope];
        datos[tope] = null;
        tope--;
        return elemento;
    }

    @Override
    public T tope() {
        if (estaVacia()) {
            System.out.println("Error: pila vacia --> no hay tope");
            return null;
        }
        return datos[tope];
    }

    @Override
    public boolean estaVacia() {
        return tope == -1;
    }

    @Override
    public boolean estaLlena() {
        return tope == max - 1;
    }

    @Override
    public int tamanio() {
        return tope + 1;
    }

    @Override
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Pila vacia");
            return;
        }
        System.out.print("Datos de la Pila (del tope al fondo): ");
        for (int i = tope; i >= 0; i--) {
            System.out.print(datos[i] + " - ");
        }
        System.out.println();
    }
}
