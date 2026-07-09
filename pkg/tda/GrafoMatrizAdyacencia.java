package tda;

public class GrafoMatrizAdyacencia<T> implements IGrafo<T> {
    private T[] vertices;
    private int[][] matriz;
    private int cantidad;
    private int capacidad;
    private boolean dirigido;

    public GrafoMatrizAdyacencia(int capacidad, boolean dirigido) {
        this.capacidad = capacidad;
        this.dirigido = dirigido;
        this.cantidad = 0;
        this.vertices = (T[]) new Object[capacidad];
        this.matriz = new int[capacidad][capacidad];
    }

    private int obtenerIndice(T vertice) {
        for (int i = 0; i < cantidad; i++) {
            if (vertices[i].equals(vertice)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void insertarVertice(T vertice) {
        if (cantidad == capacidad) {
            System.out.println("Error: no se pueden insertar mas vertices");
            return;
        }
        if (existeVertice(vertice)) {
            System.out.println("Error: el vertice ya existe");
            return;
        }
        vertices[cantidad] = vertice;
        cantidad++;
    }

    @Override
    public void eliminarVertice(T vertice) {
        int pos = obtenerIndice(vertice);
        if (pos == -1) {
            System.out.println("Error: el vertice no existe");
            return;
        }
        for (int i = pos; i < cantidad - 1; i++) {
            vertices[i] = vertices[i + 1];
        }
        for (int i = pos; i < cantidad - 1; i++) {
            for (int j = 0; j < cantidad; j++) {
                matriz[i][j] = matriz[i + 1][j];
            }
        }
        for (int j = pos; j < cantidad - 1; j++) {
            for (int i = 0; i < cantidad; i++) {
                matriz[i][j] = matriz[i][j + 1];
            }
        }
        cantidad--;
        vertices[cantidad] = null;
        for (int i = 0; i < capacidad; i++) {
            matriz[cantidad][i] = 0;
            matriz[i][cantidad] = 0;
        }
    }

    @Override
    public void insertarArista(T origen, T destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);
        if (posOrigen == -1 || posDestino == -1) {
            System.out.println("Error: uno de los vertices no existe");
            return;
        }
        matriz[posOrigen][posDestino] = 1;
        if (!dirigido) {
            matriz[posDestino][posOrigen] = 1;
        }
    }

    @Override
    public void eliminarArista(T origen, T destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);
        if (posOrigen == -1 || posDestino == -1) {
            System.out.println("Error: uno de los vertices no existe");
            return;
        }
        matriz[posOrigen][posDestino] = 0;
        if (!dirigido) {
            matriz[posDestino][posOrigen] = 0;
        }
    }

    @Override
    public boolean existeVertice(T vertice) {
        return obtenerIndice(vertice) != -1;
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        int posOrigen = obtenerIndice(origen);
        int posDestino = obtenerIndice(destino);
        if (posOrigen == -1 || posDestino == -1) {
            return false;
        }
        return matriz[posOrigen][posDestino] == 1;
    }


// busca el camino mas corto (menos saltos) entre dos vertices con BFS.
// como todas las aristas valen 1, BFS visita por niveles de distancia
// asi que la primera vez que llega al destino ya es el camino mas corto.

    @Override
    public void caminoMasCorto(T origen, T destino) {
        int inicio = obtenerIndice(origen);
        int fin = obtenerIndice(destino);
        if (inicio == -1 || fin == -1) {
            System.out.println("Error: alguno de los vertices no existe en el grafo");
            return;
        }

        boolean[] visitado = new boolean[cantidad];
        int[] predecesor = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            predecesor[i] = -1;
        }

        Cola<Integer> cola = new Cola<Integer>(cantidad);
        visitado[inicio] = true;
        cola.encolar(inicio);

        while (!cola.estaVacia()) {
            int actual = cola.desencolar();
            if (actual == fin) {
                break;
            }
            for (int v = 0; v < cantidad; v++) {
                if (matriz[actual][v] == 1 && !visitado[v]) {
                    visitado[v] = true;
                    predecesor[v] = actual;
                    cola.encolar(v);
                }
            }
        }

        if (!visitado[fin]) {
            System.out.println("No hay camino entre " + origen + " y " + destino);
            return;
        }

        // Reconstruir el camino desde el destino hacia el origen.
        int[] caminoInvertido = new int[cantidad];
        int largo = 0;
        int actual = fin;
        while (actual != -1) {
            caminoInvertido[largo] = actual;
            largo++;
            actual = predecesor[actual];
        }

        // Mostrarlo en orden (del origen al destino).
        System.out.println("Camino mas corto (" + (largo - 1) + " saltos):");
        for (int i = largo - 1; i >= 0; i--) {
            System.out.print(vertices[caminoInvertido[i]]);
            if (i > 0) {
                System.out.print("  ->  ");
            }
        }
        System.out.println();
    }

    @Override
    public void mostrarVertices() {
        System.out.println("Vertices:");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(vertices[i] + " ");
        }
        System.out.println();
    }

    @Override
    public void mostrarMatriz() {
        System.out.println("Matriz de adyacencia:");
        for (int i = 0; i < cantidad; i++) {
            System.out.print(vertices[i] + "  ");
            for (int j = 0; j < cantidad; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
}