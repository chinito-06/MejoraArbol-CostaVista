package tda;

public interface IDiccionario<K, V> {
    public boolean insertar(K clave, V valor);
    public V recuperarValor(K clave);
    public boolean modificar(K clave, V valor);
    public boolean eliminar(K clave);
    public int existe(K clave);
    public boolean estaVacio();
    public int tamanio();
    public void listarClaves();
    public void listarValores();
    public void mostrar();
}
