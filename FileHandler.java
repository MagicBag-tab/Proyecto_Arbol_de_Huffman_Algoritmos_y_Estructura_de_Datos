/*
 * @file FileHandler.java
 * 
 * @author Sarah Estrada 24347
 * 
 * @date 1/05/2024
 * 
 * @description
 * Programa que lee el archivo y realiza las conversiones de bits a ASCCI y de ASCII a bits
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
     * Escribe la cadena de bits a ASCII en un archivo de texto
     */
    public void writeBitsAsASCII(String path, String bits) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(bits);
        }
    }

    /*
     * Lee un archivo que se encuentra en ACSSI y los convierte
     * a bits devolviendo la cadena de bits
     */
    public String readBitsFromASCII(String path) throws IOException {
        StringBuilder bits = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bits.append(line);
            }
        }
        return bits.toString();
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
