package tda;

public interface IPila<T> {
    public void apilar(T elemento);
    public T desapilar();
    public T tope();
    public boolean estaVacia();
    public boolean estaLlena();
    public int tamanio();
    public void mostrar();
}
