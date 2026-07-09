package tda;

public interface IConjunto<T> {
    public void crear();
    public boolean esVacio();
    public void insertar(T elemento);
    public void eliminar(T elemento);
    public boolean pertenece(T elemento);
    public int tamanio();
    public void mostrar();
}
