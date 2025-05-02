/*
* @file PriorityQueue.java
* 
* @author Sarah Estrada 24347
* 
* @date 1/05/2024
* 
* @description
* Programa que realiza el proceso de gestionar los nodos del arbol de huffman
* segun la frecuencia con la que se usan ciertas letras o simbolos.s
*
* @reference https://www.geeksforgeeks.org/min-heap-in-java/
*/

public class PriorityQueue {

    private HuffmanNode[] heap;
    private int size;
    private int capacity;

    /*
     * Inicializa la cola de prioridad
     */

    public PriorityQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new HuffmanNode[capacity];
    }

    /*
     * Si la cola esta vacía
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * Agrega un nuevo nodo al HuffmanTreea a la cola
     * segun su frecuencia
     */
    public void add(HuffmanNode node) {
        if (size >= capacity) {
            throw new IllegalStateException("La cola esta llena");
        }
        heap[size] = node;
        siftUp(size);
        size++;
    }

    /*
     * Agarra y devuelve el nodo con menor frecuencia
     * el cual es la raíz y se reorganiza el heap.
     */
    public HuffmanNode poll() {
        if (isEmpty()) {
            return null;
        }
        HuffmanNode result = heap[0];
        heap[0] = heap[size - 1];
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return result;
    }

    /*
     * Reorganiza un nodo hasta arriba del heap
     * comparando la frecuencia del nodo con la de su padre.
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    /*
     * Reorganiza el nodo hacia abajo en el heap
     * cmparando su frecuencia con la de sus hijos.
     */
    private void siftDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap[left].compareTo(heap[smallest]) < 0) {
                smallest = left;
            }

            if (right < size && heap[right].compareTo(heap[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);
            index = smallest;
        }
    }

    /*
     * Intercambia dos nodos en el heap
     */
    private void swap(int i, int j) {
        HuffmanNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /*
     * Devuelve la cantidad de nodos en el heap.
     */
    public int size() {
        return size;
    }

}
