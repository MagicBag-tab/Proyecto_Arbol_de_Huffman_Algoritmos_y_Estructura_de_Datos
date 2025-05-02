/*
* @file BitHandler.java
* 
* @author Sarah Estrada 24347
* 
* @date 1/05/2024
* 
* @description
* Programa que maneja un lector de bits y también escribe los bits.
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BitHandler {

    /*
     * Escribe los bits como caracteres ASCII en un archivo de texto.
     * Almacena la cantidad de bits de relleno al inicio del archivo.
     */
    public void writeBitsAsASCII(String path, String bits, int padding) throws IOException {
        validateBits(bits);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            // Escribir la cantidad de bits de relleno como el primer byte
            writer.write((char) padding);

            // Convertir los bits a caracteres ASCII (grupos de 8 bits)
            StringBuilder ascii = new StringBuilder();
            for (int i = 0; i < bits.length(); i += 8) {
                String byteStr = bits.substring(i, Math.min(i + 8, bits.length()));
                int byteValue = Integer.parseInt(byteStr, 2);
                ascii.append((char) byteValue);
            }
            writer.write(ascii.toString());
            writer.flush();
        }
    }

    /*
     * Lee un archivo que contiene caracteres ASCII y los convierte
     * a bits, devolviendo la cadena de bits y almacenando el relleno en
     * paddingHolder.
     */
    public String readBitsFromASCII(String path, int[] paddingHolder) throws IOException {
        StringBuilder asciiContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            // Leer el primer byte que contiene la cantidad de bits de relleno
            int padding = reader.read();
            if (padding < 0 || padding > 7) {
                throw new IllegalArgumentException("Cantidad de bits de relleno inválida: " + padding);
            }
            paddingHolder[0] = padding;

            // Leer el resto del archivo
            int charCode;
            while ((charCode = reader.read()) != -1) {
                asciiContent.append((char) charCode);
            }
        }

        // Convertir los caracteres ASCII de vuelta a bits
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < asciiContent.length(); i++) {
            int byteValue = asciiContent.charAt(i);
            String binary = Integer.toBinaryString(byteValue);
            // Asegurarse de que cada byte tenga 8 bits (rellenar con ceros a la izquierda
            // si es necesario)
            while (binary.length() < 8) {
                binary = "0" + binary;
            }
            bits.append(binary);
        }

        String result = bits.toString();
        if (result.isEmpty()) {
            throw new IllegalArgumentException("El archivo de bits está vacío o no contiene bits válidos");
        }
        validateBits(result);
        return result;
    }

    /*
     * Escribe la tabla de códigos en un archivo de texto
     * para tenerlo guardado.
     */
    public void writeCodeTable(String path, Map<Character, String> codes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Map.Entry<Character, String> entry : codes.entrySet()) {
                char c = entry.getKey();
                String code = entry.getValue();
                // Permitir caracteres imprimibles, espacios y saltos de línea
                if (Character.isDefined(c) && (!Character.isISOControl(c) || c == ' ' || c == '\n') && code != null
                        && !code.isEmpty()) {
                    validateBits(code);
                    // Representar el carácter de forma legible en el archivo
                    String charRepresentation = (c == '\n') ? "\\n" : String.valueOf(c);
                    writer.write(charRepresentation + ":" + code + "\n");
                } else {
                    System.out
                            .println("Advertencia: Entrada inválida omitida al escribir la tabla de códigos: Carácter='"
                                    + c + "', Código='" + code + "'");
                }
            }
        }
    }

    /*
     * Lee la tabla de códigos desde el archivo de texto
     */
    public Map<Character, String> readCodeTable(String path) throws IOException {
        Map<Character, String> codes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    System.out.println("Advertencia: Línea vacía en la tabla de códigos, línea " + lineNumber);
                    continue;
                }
                String[] parts = line.split(":", -1);
                if (parts.length != 2) {
                    System.out.println(
                            "Advertencia: Formato inválido en la tabla de códigos, línea " + lineNumber + ": " + line);
                    continue;
                }
                String charPart = parts[0];
                char c;
                if (charPart.equals("\\n")) {
                    c = '\n';
                } else if (charPart.length() != 1 || !Character.isDefined(charPart.charAt(0))) {
                    System.out.println(
                            "Advertencia: Carácter inválido en la tabla de códigos, línea " + lineNumber + ": " + line);
                    continue;
                } else {
                    c = charPart.charAt(0);
                }
                String code = parts[1];
                try {
                    validateBits(code);
                    codes.put(c, code);
                } catch (IllegalArgumentException e) {
                    System.out.println("Advertencia: Código de bits inválido en la línea " + lineNumber + ": " + line
                            + " (" + e.getMessage() + ")");
                }
            }
        }
        if (codes.isEmpty()) {
            throw new IllegalArgumentException("La tabla de códigos está vacía o no contiene entradas válidas");
        }
        return codes;
    }

    /*
     * Valida que la cadena de bits este bien hecha
     */
    private void validateBits(String bits) {
        for (char c : bits.toCharArray()) {
            if (c != '0' && c != '1') {
                throw new IllegalArgumentException("Cadena de bits inválida: contiene '" + c + "'");
            }
        }
    }
}