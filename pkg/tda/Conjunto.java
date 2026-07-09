package tda;

public class Conjunto<T> implements IConjunto<T> {
    private T[] datos;
    private int cantidad;
    private int max;

    public Conjunto(int max) {
        this.max = max;
        crear();
    }

    @Override
    public void crear() {
        this.datos = (T[]) new Object[max];
        this.cantidad = 0;
    }

    @Override
    public boolean esVacio() {
        return cantidad == 0;
    }

    @Override
    public void insertar(T elemento) {
        if (pertenece(elemento)) {
            System.out.println("Error: el elemento ya existe en el conjunto --> no se agrego");
            return;
        }
        if (cantidad == max) {
            System.out.println("Error: conjunto lleno --> no se agrego el elemento");
            return;
        }
        datos[cantidad] = elemento;
        cantidad++;
    }

    @Override
    public void eliminar(T elemento) {
        if (esVacio()) {
            System.out.println("Error: conjunto vacio --> no hay elementos para eliminar");
            return;
        }
        if (!pertenece(elemento)) {
            System.out.println("Error: el elemento no existe en el conjunto --> no se elimino");
            return;
        }
        int pos = -1;
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].equals(elemento)) {
                pos = i;
            }
        }
        // se reemplaza el eliminado con el ultimo y se achica el conjunto
        datos[pos] = datos[cantidad - 1];
        datos[cantidad - 1] = null;
        cantidad--;
    }

    @Override
    public boolean pertenece(T elemento) {
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void mostrar() {
        System.out.print("Datos del Conjunto: { ");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(datos[i]);
            if (i < cantidad - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" }");
    }
}
