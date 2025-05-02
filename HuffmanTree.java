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
*/

import java.util.HashMap;
import java.util.Map;

public class HuffmanTree {
    private HuffmanNode root;
    private Map<Character, String> codes;
    private Map<String, Character> reverseCodes;
    private int paddingBits; // Almacenar los bits de relleno

    /*
     * Constructor que inicializa las estructuras
     * para el árbol de huffman.
     */
    public HuffmanTree() {
        this.codes = new HashMap<>();
        this.reverseCodes = new HashMap<>();
        this.root = null;
        this.paddingBits = 0;
    }

    /*
     * Construye el árbol de Huffman, calculando las
     * frecuencias de los caracteres y las ordena utilizando el
     * PriorityQueue.
     */
    public void buildTree(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            // Permitir caracteres imprimibles, espacios y saltos de línea
            if (Character.isDefined(c) && (!Character.isISOControl(c) || c == ' ' || c == '\n')) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
        }
        if (freq.isEmpty()) {
            throw new IllegalArgumentException("El texto de entrada no contiene caracteres válidos");
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
     * Codifica un texto utilizando los códigos ya generados.
     * Devuelve la cadena de bits y almacena la cantidad de bits de relleno.
     */
    public String encode(String text) {
        if (root == null) {
            throw new IllegalStateException("El árbol no pudo ser construido");
        }

        StringBuilder bits = new StringBuilder();
        for (char c : text.toCharArray()) {
            String code = codes.get(c);
            if (code == null) {
                throw new IllegalArgumentException("No se encontró el carácter en los códigos: " + c);
            }
            bits.append(code);
        }

        // Calcular el relleno necesario para que la longitud sea múltiplo de 8
        paddingBits = (8 - (bits.length() % 8)) % 8;
        for (int i = 0; i < paddingBits; i++) {
            bits.append("0");
        }

        return bits.toString();
    }

    /*
     * Devuelve la cantidad de bits de relleno
     */
    public int getPaddingBits() {
        return paddingBits;
    }

    /*
     * Decodifica una cadena de bits, ignorando los bits de relleno.
     */
    public String decode(String bits, int paddingBits) {
        if (root == null) {
            throw new IllegalStateException("El árbol no pudo ser construido");
        }
        // Quitar los bits de relleno del final
        if (paddingBits > 0) {
            bits = bits.substring(0, bits.length() - paddingBits);
        }
        StringBuilder decoded = new StringBuilder();
        HuffmanNode current = root;
        for (int i = 0; i < bits.length(); i++) {
            char bit = bits.charAt(i);
            current = (bit == '0') ? current.getLeft() : current.getRight();
            if (current == null) {
                System.out.println("Decodificación falló en la posición " + i + " con bit '" + bit + "'");
                throw new IllegalArgumentException("La secuencia de bits es inválida en la posición " + i);
            }
            if (current.getLeft() == null && current.getRight() == null) {
                decoded.append(current.getData());
                current = root;
            }
        }
        if (current != root) {
            throw new IllegalArgumentException(
                    "La secuencia de bits termina en un nodo intermedio, posiblemente incompleta");
        }
        return decoded.toString();
    }

    /*
     * Devuelve el Map de los códigos de Huffman
     */
    public Map<Character, String> getCodes() {
        return codes;
    }

    /*
     * Reconstruye el árbol de Huffman a partir de los códigos
     */
    public void buildTreeFromCodes(Map<Character, String> codeTable) {
        root = new HuffmanNode('\0', 0);
        codes = new HashMap<>(codeTable);
        reverseCodes = new HashMap<>();

        for (Map.Entry<Character, String> entry : codeTable.entrySet()) {
            char c = entry.getKey();
            String code = entry.getValue();
            reverseCodes.put(code, c);

            HuffmanNode current = root;
            for (int i = 0; i < code.length(); i++) {
                char bit = code.charAt(i);
                HuffmanNode newNode = (bit == '0') ? current.getLeft() : current.getRight();
                if (newNode == null) {
                    newNode = new HuffmanNode('\0', 0);
                    if (bit == '0') {
                        current.setLeft(newNode);
                    } else {
                        current.setRight(newNode);
                    }
                }
                current = newNode;
            }
            current.setData(c);
        }
    }
}