/*
* @file HuffmanTree.java
* 
* @author Sarah Estrada 24347
* 
* @date 1/05/2024
* 
* @description
* Programa que contiene toda la lógica del funcionamiento del arbol de huffman.
*
* @reference
* https://www.geeksforgeeks.org/huffman-coding-java/?ref=ml_lbp
*
*/

import java.util.HashMap;
import java.util.Map;

public class HuffmanTree {
    private HuffmanNode root;
    private Map<Character, String> codes;
    private Map<String, Character> reverseCodes;

    /*
     * Constructor que inicializa las estrucutras
     * para el árbol de huffman.
     */
    public HuffmanTree() {
        this.codes = new HashMap<>();
        this.reverseCodes = new HashMap<>();
        this.root = null;
    }

    /*
     * Construye el árbol de Huffman, calculando las
     * frecuencias de los caracteres y las ordena utilizando el
     * PriorityQueue.
     */
    public void buildTree(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        PriorityQueue queue = new PriorityQueue(freq.size());
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            queue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();
            HuffmanNode parent = new HuffmanNode('\0', left.getFrequency() + right.getFrequency());
            parent.setLeft(left);
            parent.setRight(right);
            queue.add(parent);
        }

        root = queue.poll();
        generateCodes(root, "");
    }

    /*
     * Genera los códigos de Huffman recorriendo
     * el árbol y asigna "0" para el hijo izquierdo y "1" para el derecho.
     */
    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }

        if (node.getLeft() == null && node.getRight() == null) {
            codes.put(node.getData(), code);
            reverseCodes.put(code, node.getData());
        }

        generateCodes(node.getLeft(), code + "0");
        generateCodes(node.getRight(), code + "1");
    }

    /*
     * Codifica un texto utilizando los codigos ya generados.
     */
    public String encode(String text) {
        if (root == null) {
            throw new IllegalStateException("El arbol no pudo ser constrido");
        }

        StringBuilder bits = new StringBuilder();
        for (char c : text.toCharArray()) {
            String code = codes.get(c);
            if (code == null) {
                throw new IllegalArgumentException("No se encontro el caracter en los codigos" + c);
            }
            bits.append(code);
        }
        return bits.toString();
    }

    /*
     * Decodifica una cadena de bits.
     */
    public String decode(String bits) {
        if (root == null) {
            throw new IllegalStateException("El arbol no pudo ser constrido");
        }
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;
        for (char bit : bits.toCharArray()) {
            current = (bit == '0') ? current.getLeft() : current.getRight();
            if (current == null) {
                throw new IllegalArgumentException("La secuencia de bits es inválida");
            }
            if (current.getLeft() == null && current.getRight() == null) {
                decoded.append(current.getData());
                current = root;
            }
        }
        return decoded.toString();
    }

    /*
     * Devuelve el Map de los codigos de Huffman
     */
    public Map<Character, String> getCodes() {
        return codes;
    }

    /*
     * Reconstruye el árbol de huffman a partir de los códigos
     */
    public void bildTreeFromCodes(Map<Character, String> codeTable) {
        root = new HuffmanNode('\0', 0);
        codes = new HashMap<>(codeTable);
        reverseCodes = new HashMap<>();

        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            char c = entry.getKey();
            String code = entry.getValue();
            reverseCodes.put(code, c);

            HuffmanNode current = root;
            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.getLeft() == null) {
                        current.setLeft(new HuffmanNode('\0', 0));
                    }
                    current = current.getLeft();
                } else {
                    if (current.getRight() == null) {
                        current.setRight(new HuffmanNode('\0', 0));
                    }
                    current = current.getRight();
                }
            }
            current.setData(c);
        }
    }
}
