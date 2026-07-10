package tda;

public interface IArbol<V> {
    public void insertar(int clave, String id, V valor);
    public V buscar(int clave, String id);
    public void eliminar(int clave, String id);
    public void listarEnRango(int min, int max);
    public void mostrarInorden(Nodo<V> elNodo);
    public void mostrarPreorden(Nodo<V> elNodo);
    public void mostrarPostorden(Nodo<V> elNodo);
    public int contarNodos();
    public int altura();
    public boolean esHoja(Nodo<V> elNodo);
    public boolean estaBalanceado();
    public int contarHojas();
    public void mostrarHojas();
    public boolean estaVacio();
    public Nodo<V> devolverRaiz();
}
