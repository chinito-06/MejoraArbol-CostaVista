package tda;

public interface IColaPrioridad<T> {
    public void crear();
    public boolean estaVacia();
    public boolean estaLlena();
    public void insertar(T dato, int prioridad);
    public T eliminar();
    public void eliminarElemento(T dato);
    public T frente();
    public void mostrar();
}