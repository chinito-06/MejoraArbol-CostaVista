package tda;

public class Diccionario<K, V> implements IDiccionario<K, V> {
    private Dato<K, V>[] datosDiccionario;
    private int cantidad;
    private int dimension;

    public Diccionario(int dimension) {
        this.dimension = dimension;
        this.datosDiccionario = (Dato<K, V>[]) new Dato[dimension];
        this.cantidad = 0;
    }

    @Override
    public int existe(K clave) {
        for (int i = 0; i < cantidad; i++) {
            if (datosDiccionario[i].obtenerClave().equals(clave)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean insertar(K clave, V valor) {
        if (existe(clave) != -1) {
            System.out.println("Error: la clave ya existe --> no se agrego al diccionario");
            return false;
        }
        if (cantidad == dimension) {
            System.out.println("Error: diccionario lleno --> no se agrego la clave");
            return false;
        }
        datosDiccionario[cantidad] = new Dato<K, V>(clave, valor);
        cantidad++;
        return true;
    }

    @Override
    public V recuperarValor(K clave) {
        int posicion = existe(clave);
        if (posicion == -1) {
            System.out.println("Error: la clave no existe --> no hay valor asociado");
            return null;
        }
        return datosDiccionario[posicion].obtenerValor();
    }

    @Override
    public boolean modificar(K clave, V valor) {
        int posicion = existe(clave);
        if (posicion == -1) {
            System.out.println("Error: la clave no existe --> no se modifico");
            return false;
        }
        datosDiccionario[posicion].establecerValor(valor);
        return true;
    }

    @Override
    public boolean eliminar(K clave) {
        int posicion = existe(clave);
        if (posicion == -1) {
            System.out.println("Error: la clave no existe --> no se elimino");
            return false;
        }
        for (int i = posicion; i < cantidad - 1; i++) {
            datosDiccionario[i] = datosDiccionario[i + 1];
        }
        datosDiccionario[cantidad - 1] = null;
        cantidad--;
        return true;
    }

    @Override
    public boolean estaVacio() {
        return cantidad == 0;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void listarClaves() {
        System.out.println("Claves del diccionario:");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(datosDiccionario[i].obtenerClave() + " - ");
        }
        System.out.println();
    }

    @Override
    public void listarValores() {
        System.out.println("Valores del diccionario:");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(datosDiccionario[i].obtenerValor() + " - ");
        }
        System.out.println();
    }

    @Override
    public void mostrar() {
        if (estaVacio()) {
            System.out.println("No existen elementos en el diccionario");
            return;
        }
        System.out.println("Diccionario:");
        for (int i = 0; i < cantidad; i++) {
            System.out.println(datosDiccionario[i].obtenerClave() + ": " + datosDiccionario[i].obtenerValor());
        }
    }
}
