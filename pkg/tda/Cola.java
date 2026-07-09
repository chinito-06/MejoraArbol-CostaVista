package tda;

public class Cola<T> implements ICola<T> {
    private int frente;
    private int fin;
    private int capacidad;
    private T[] datos;

    public Cola(int capacidad) {
        this.capacidad = capacidad;
        this.datos = (T[]) new Object[capacidad];
        this.frente = 0;
        this.fin = -1;
    }

    @Override
    public void encolar(T elemento) {
        if (estaLlena()) {
            System.out.println("Error: la cola esta llena --> no se pudo encolar");
            return;
        }
        fin++;
        datos[fin] = elemento;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) {
            System.out.println("Error: cola vacia --> no hay elementos para desencolar");
            return null;
        }
        T elemento = datos[frente];
        datos[frente] = null;
        frente++;
        return elemento;
    }

    @Override
    public boolean estaVacia() {
        return frente > fin;
    }

    @Override
    public boolean estaLlena() {
        return fin == capacidad - 1;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            System.out.println("Error: cola vacia --> no hay frente");
            return null;
        }
        return datos[frente];
    }

    @Override
    public T fin() {
        if (estaVacia()) {
            System.out.println("Error: cola vacia --> no hay fin");
            return null;
        }
        return datos[fin];
    }

    @Override
    public int tamano() {
        if (estaVacia()) {
            return 0;
        }
        return fin - frente + 1;
    }

    @Override
    public void mostrar() {
        if (estaVacia()) {
            System.out.println("Cola vacia");
            return;
        }
        System.out.print("Datos de la cola: ");
        for (int i = frente; i <= fin; i++) {
            System.out.print(datos[i] + ", ");
        }
        System.out.println();
    }
}
