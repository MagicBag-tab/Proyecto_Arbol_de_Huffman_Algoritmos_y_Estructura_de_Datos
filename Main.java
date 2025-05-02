
/*
 * @file Main.java
 * 
 * @author Sarah Estrada 24347
 * 
 * @date 1/05/2024
 * 
 * @description
 * Programa principal, en donde el usuario elige si desea codificar o
 * decodificar el archivo
 */
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        FileHandler fileHandler = new FileHandler();
        BitHandler bitHandler = new BitHandler();
        Scanner scanner = new Scanner(System.in);

        String inputFile = "codingtext.txt";
        String compressedFile = "compressed.txt";
        String codesFile = "codes.txt";
        String outputFile = "output.txt";

        System.out.println("Bienvenido al Codificador/Decodificador de Huffman");
        System.out.println("\nMenú:");
        System.out.println("1. Codificar archivo (" + inputFile + ")");
        System.out.println("2. Decodificar archivo (" + compressedFile + ")");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción (1-3): ");

        int op = scanner.nextInt();

        while (op != 3) {

            switch (op) {
                case 1:
                    try {
                        String input = fileHandler.readFile(inputFile);
                        if (input.isEmpty()) {
                            System.out.println("El archivo de entrada está vacío.");
                            break;
                        }
                        System.out.println("Texto de entrada:");
                        System.out.println("---");
                        System.out.println(input);
                        System.out.println("---");

                        HuffmanTree huffman = new HuffmanTree();
                        huffman.buildTree(input);
                        String bits = huffman.encode(input);
                        int padding = huffman.getPaddingBits();
                        System.out.println("Bits codificados: " + bits);
                        System.out.println("Bits de relleno añadidos: " + padding);

                        bitHandler.writeBitsAsASCII(compressedFile, bits, padding);
                        bitHandler.writeCodeTable(codesFile, huffman.getCodes());
                        System.out.println("Codificación completada. Archivos generados:");
                        System.out.println("- " + compressedFile + " (caracteres ASCII)");
                        System.out.println("- " + codesFile + " (tabla de códigos)");

                        System.out.println("Tabla de códigos:");
                        for (Map.Entry<Character, String> entry : huffman.getCodes().entrySet()) {
                            char key = entry.getKey();
                            String displayKey = (key == '\n') ? "\\n" : String.valueOf(key);
                            System.out.println("'" + displayKey + "': " + entry.getValue());
                        }

                    } catch (IOException e) {
                        System.err.println("Error al codificar: " + e.getMessage());
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.err.println("Error en los datos o algoritmo: " + e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Leyendo tabla de códigos desde: " + codesFile);
                        Map<Character, String> codes = bitHandler.readCodeTable(codesFile);
                        if (codes.isEmpty()) {
                            System.out.println("La tabla de códigos está vacía.");
                            break;
                        }
                        System.out.println("Tabla de códigos leída (" + codes.size() + " entradas):");
                        for (Map.Entry<Character, String> entry : codes.entrySet()) {
                            char key = entry.getKey();
                            String displayKey = (key == '\n') ? "\\n" : String.valueOf(key);
                            System.out.println("'" + displayKey + "': " + entry.getValue());
                        }

                        HuffmanTree decoder = new HuffmanTree();
                        decoder.buildTreeFromCodes(codes);

                        int[] paddingHolder = new int[1];
                        String readBits = bitHandler.readBitsFromASCII(compressedFile, paddingHolder);
                        int padding = paddingHolder[0];
                        System.out.println("Bits leídos: " + readBits);
                        System.out.println("Bits de relleno: " + padding);

                        String decoded = decoder.decode(readBits, padding);
                        System.out.println("Texto decodificado:");
                        System.out.println("---");
                        System.out.println(decoded);
                        System.out.println("---");

                        fileHandler.writeFile(outputFile, decoded);
                        System.out.println("Decodificación completada. Archivo generado: " + outputFile);

                    } catch (IOException e) {
                        System.err.println("Error al decodificar: " + e.getMessage());
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.err.println("Error en los datos o algoritmo: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.println("Gracias por utilizar el programa uu");
                    op = 3;
                    break;

                default:
                    System.out.println("Selecciona una opción válida");
                    break;
            }

            System.out.println("\nMenú:");
            System.out.println("1. Codificar archivo (" + inputFile + ")");
            System.out.println("2. Decodificar archivo (" + compressedFile + ")");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción (1-3): ");

            op = scanner.nextInt();
        }
        scanner.close();
    }
}