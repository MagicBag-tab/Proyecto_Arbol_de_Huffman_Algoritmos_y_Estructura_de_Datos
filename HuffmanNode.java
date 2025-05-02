/*
* @file HuffamnNode.java
* 
* @author Sarah Estrada 24347
* 
* @date 1/05/2024
* 
* @description
* Representa los nodos del arbol de huffman
*
* @reference
* https://www.geeksforgeeks.org/huffman-coding-java/?ref=ml_lbp
*
*/

public class HuffmanNode implements Comparable<HuffmanNode> {
    private char data;
    private int frequency;
    private HuffmanNode left, right;

    // constructor que inicializa el árbol
    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        left = right = null;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public int getFrecuency() {
        return frequency;
    }

    public void setFrecuency(int frequency) {
        this.frequency = frequency;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    // Implementación de Comparable para PriorityQueue
    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }

}
