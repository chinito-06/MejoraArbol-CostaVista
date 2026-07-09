package tda;

public interface ICola<T> {
    public void encolar(T elemento);
    public T desencolar();
    public boolean estaVacia();
    public boolean estaLlena();
    public T frente();
    public T fin();
    public int tamano();
    public void mostrar();
}
