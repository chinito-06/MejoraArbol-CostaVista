# Mejora funcional — Reporte de reposición por rango de stock

**Materia:** Programación 2 — TPO (Centro Logístico de Distribución)
**Integrantes:** Juan Segundo Olazabal, Mateo Schezzler

Este documento describe **únicamente la mejora** incorporada al Trabajo Práctico. El resto del sistema (los TDA originales y la clase de gestión) ya estaba entregado en la versión original.

---

## 1. Descripción de la nueva funcionalidad

Se incorporó al sistema un **Árbol Binario de Búsqueda (ABB)** que indexa los productos **por su stock**, y se lo amplió con un método nuevo: **búsqueda por rango**.

Con eso el sistema pasa a poder responder:

```java
centro.listarProductosParaReponer(20);
```

> *"Dame todos los productos con stock entre 0 y 20, ordenados de menor a mayor, para armar la orden de reposición del día."*

Se agregó además un segundo método de consulta:

```java
centro.mostrarCatalogoPorStock();   // catalogo completo ordenado por stock
```

que aprovecha la propiedad del ABB: un **recorrido inorden** devuelve los elementos ya ordenados, sin ordenar nada.

---

## 2. Justificación: por qué es útil

Antes de la mejora, el sistema **no podía** responder esa consulta de forma razonable:

- La **Cola de Prioridad** (`inventarioCritico`) devuelve **un solo producto**: el más crítico, con `frente()`. Para obtener los diez más críticos habría que ir sacándolos y después reinsertarlos todos.
- El **Diccionario** es un arreglo con búsqueda lineal. Encuentra por **código exacto** en O(n), pero no sabe nada de orden ni de rangos: habría que recorrer el catálogo completo y después ordenar el resultado.

El ABB responde *"todos los que están entre X e Y, ordenados"* en **O(log n + k)** (siendo k la cantidad de resultados), sin recorrer todo el catálogo y sin ordenar. La consulta baja hasta la zona del rango y **poda** las ramas donde no puede haber claves válidas.

Es una mejora directa sobre la lógica central del sistema: la reposición de inventario.

---

## 3. TDA utilizados

| TDA | Rol en la mejora |
|-----|------------------|
| **Árbol Binario de Búsqueda** (`Arbol<V>`) | TDA **nuevo**, incorporado y **ampliado** con el método `listarEnRango(min, max)`. Indexa los productos por stock. |
| **Diccionario** (`Diccionario<String,Producto>`) | Sigue siendo el índice por código. Se mantiene **sincronizado** con el árbol: el diccionario busca por código, el árbol por rango de stock. |
| **Cola de Prioridad** (`ColaPrioridad<Producto>`) | **Convive** con el árbol, no se reemplaza. La cola da *el* producto más crítico; el árbol da *todos* los que están bajo el umbral. |
| **Conjunto** (`Conjunto<String>`) | Garantiza que el código de producto —que el árbol usa como `id`— sea único. |

---

## 4. Integración con el resto del programa

### 4.1 El TDA nuevo (`tda/`)

Se adaptó el `Arbol` de la cátedra, que guardaba un `int` pelado, para que guarde productos. El **`Nodo` tiene ahora tres campos**:

| Campo | Para qué | En el sistema |
|-------|----------|---------------|
| `clave` (int) | **ordena** el árbol | el stock del producto |
| `id` (String) | **identifica** el nodo cuando dos comparten clave | el código del producto |
| `valor` (V) | el dato guardado | el `Producto` completo |

El `id` es un texto **opaco** para el árbol: no sabe que es un "código de producto". Por eso el TDA sigue siendo genérico (`Arbol<V>`), y en el sistema se usa como `Arbol<Producto>`.

**Claves repetidas.** Dos productos pueden tener el mismo stock. El árbol las admite mandando las iguales **a la derecha** (rama `else` del `insertar`, igual que la cátedra). Para distinguirlas, `buscar` y `eliminar` reciben también el `id`: cuando la clave coincide pero el `id` no, siguen buscando a la derecha.

**Método nuevo — `listarEnRango(min, max)`.** Es un recorrido inorden **podado**:

```java
if (nodo.clave > min)                          listarEnRangoRec(nodo.izq, ...);  // poda por abajo
if (nodo.clave >= min && nodo.clave <= max)    System.out.println(nodo.valor);
if (nodo.clave < max)                          listarEnRangoRec(nodo.der, ...);  // poda por arriba
```

Las dos condiciones son la poda: si el nodo actual ya tiene stock mayor o igual a `max`, no se baja a la derecha, porque todo lo de ahí es aún mayor. Como es inorden, los resultados salen **ordenados** de forma natural.

### 4.2 La clase de gestión (`service/CentroLogistico.java`)

Se agregó el atributo `Arbol<Producto> arbolStock` y se lo mantiene **coherente** con las demás estructuras:

| Método | Qué se le agregó |
|--------|------------------|
| `agregarProducto(...)` | Inserta el producto en el árbol con `clave = stock`, `id = codigo`. |
| `actualizarStock(...)` | El stock **es la clave**: al cambiar, hay que reubicar el nodo. |
| `deshacerUltimoMovimiento()` | Ídem: se restaura el stock previo y se reindexa. |

Como el árbol no sabe modificar una clave in-situ, el reindexado se hace **eliminando el nodo con la clave vieja y reinsertándolo con la nueva**:

```java
private void reindexarArbolStock(int stockPrevio, Producto p) {
    arbolStock.eliminar(stockPrevio, p.obtenerCodigo());
    arbolStock.insertar(p.obtenerStock(), p.obtenerCodigo(), p);
}
```

Es **el mismo patrón** que el sistema ya usaba en `reordenarInventarioCritico(...)` para la Cola de Prioridad.

Gracias a esto, el árbol se sincroniza **solo**: `despacharProximoPedido()` llama a `actualizarStock` por cada ítem del pedido, así que el reporte de reposición refleja los despachos sin que nadie toque el árbol explícitamente.

Métodos públicos nuevos: `listarProductosParaReponer(umbral)` y `mostrarCatalogoPorStock()`.

---

## 5. Código fuente

Archivos de la mejora (dentro de `Mejorarbol/`):

- `tda/Nodo.java` — nodo con `clave`, `id` y `valor`. **(nuevo)**
- `tda/IArbol.java` — interfaz del TDA. **(nuevo)**
- `tda/Arbol.java` — el ABB genérico + `listarEnRango(...)`. **(nuevo)**
- `service/CentroLogistico.java` — atributo `arbolStock`, `reindexarArbolStock(...)`, `listarProductosParaReponer(...)`, `mostrarCatalogoPorStock()`.
- `app/Main.java` — casos de prueba de la mejora (incluye P004, con el mismo stock que P002, para ejercitar las claves repetidas).


## 6. Evidencia de ejecución

Productos cargados. **P002 y P004 comparten stock 8** a propósito:

| Código | Nombre | Stock |
|--------|--------|-------|
| P001 | Lavandina 1L | 50 |
| P002 | Jabon en polvo | 8 |
| P003 | Esponja x3 | 202 |
| P004 | Trapo de piso | 8 |

**Catálogo ordenado por stock (recorrido inorden):**

```
Catalogo ordenado por stock: Producto[P004: Trapo de piso, stock 8] Producto[P002: Jabon en polvo, stock 8]
                             Producto[P001: Lavandina 1L, stock 50] Producto[P003: Esponja x3, stock 202]
```

**Reporte de reposición (stock <= 20). Los dos productos con stock 8 aparecen:**

```
Productos con stock entre 0 y 20 (de menor a mayor):
  Producto[P004: Trapo de piso, stock 8]
  Producto[P002: Jabon en polvo, stock 8]
```

**Reindexado — se repone P004 (+100). El árbol elimina SOLO su nodo, P002 queda intacto:**

```
Stock actualizado: Producto[P004: Trapo de piso, stock 108]
Catalogo ordenado por stock: P002(8)  P001(50)  P004(108)  P003(202)

Reposicion con stock <= 20 (P004 ya no aparece, P002 sigue):
  Producto[P002: Jabon en polvo, stock 8]
```

**Deshacer el movimiento — P004 vuelve a stock 8 y reaparece en el reporte:**

```
Movimiento deshecho: Producto[P004: Trapo de piso, stock 8]
Reposicion con stock <= 20 (P004 vuelve a aparecer):
  Producto[P002: Jabon en polvo, stock 8]
  Producto[P004: Trapo de piso, stock 8]
```

**El árbol se sincroniza solo al despachar un pedido** (PED-1 se lleva 2 unidades de P002):

```
Reposicion con stock <= 20 (P002 bajo a 6 al despachar PED-1):
  Producto[P002: Jabon en polvo, stock 6]
  Producto[P004: Trapo de piso, stock 8]
```

### Casos de prueba cubiertos

| Caso | Entrada | Resultado esperado | Obtenido |
|------|---------|--------------------|----------|
| Recorrido inorden | catálogo completo | ordenado por stock ascendente | ✓ |
| Rango con claves repetidas | `umbral = 20` | lista P002 **y** P004 (ambos stock 8) | ✓ |
| Rango vacío | `umbral = 5` | avisa que no hay elementos | ✓ |
| Rango que abarca todo | `umbral = 300` | los 4 productos, ordenados | ✓ |
| Umbral negativo | `umbral = -5` | mensaje de error, no rompe | ✓ |
| Reindexado por cambio de stock | `actualizarStock("P004", +100)` | P004 sale del reporte, P002 queda | ✓ |
| Reindexado al deshacer | `deshacerUltimoMovimiento()` | P004 vuelve al reporte | ✓ |
| Sincronización tras despacho | `despacharProximoPedido()` | P002 aparece con stock 6 | ✓ |

---

## 7. Limitaciones conocidas

Se dejan explícitas porque son decisiones de diseño, no errores:

- **El ABB no se balancea.** En el peor caso —claves insertadas en orden, o muchos productos con el mismo stock encadenados a la derecha— degenera hacia una lista y las operaciones pasan a O(n). Un **AVL** lo evitaría rebalanceando con rotaciones. No se usó porque las rotaciones **no preservan la invariante "claves iguales a la derecha"** de la que depende nuestro `eliminar(clave, id)`: una rotación a izquierda puede dejar una clave igual en el subárbol izquierdo. Para usar AVL habría que volver la clave única, ordenando por stock y desempatando por código.
- **Entre claves iguales el orden no es estable.** Tras reinsertar un producto, su posición relativa a otro con el mismo stock puede cambiar. El ABB no promete orden entre claves iguales.
- **`listarEnRango` imprime en vez de devolver los resultados**, en línea con el resto del proyecto. Para encadenar el reporte con otra operación (por ejemplo, generar la orden de compra automáticamente) habría que devolver los productos en lugar de mostrarlos.
