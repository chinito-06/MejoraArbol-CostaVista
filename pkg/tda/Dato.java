package tda;

public class Dato<K, V> {
    private K clave;
    private V valor;

    public Dato(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public K obtenerClave() {
        return clave;
    }

    public V obtenerValor() {
        return valor;
    }

    public void establecerValor(V valor) {
        this.valor = valor;
    }
}
