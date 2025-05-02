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
     * Lee el archivo y lo vuelve una cadena
     */
    public String readFile(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /*
     * Escribe la cadena de texto ya listo en un
     * archivo
     */
    public void writeFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        }
    }

}
