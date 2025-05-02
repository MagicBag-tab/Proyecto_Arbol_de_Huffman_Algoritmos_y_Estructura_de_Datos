/*
 * @file FileHandler.java
 * 
 * @author Sarah Estrada 24347
 * 
 * @date 1/05/2024
 * 
 * @description
 * Programa que lee el archivo
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    /*
     * Lee el archivo y lo vuelve una cadena, conservando espacios y saltos de línea
     */
    public String readFile(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    content.append("\n");
                }
                content.append(line);
                firstLine = false;
            }
        }
        return content.toString();
    }

    /*
     * Escribe la cadena de texto en un archivo, conservando espacios y saltos de
     * línea
     */
    public void writeFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

}