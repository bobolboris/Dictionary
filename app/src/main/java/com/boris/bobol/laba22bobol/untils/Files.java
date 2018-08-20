package com.boris.bobol.laba22bobol.untils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class Files {
    public static String readAllText(String path) throws IOException {
        return readAllText(new File(path));
    }

    public static String readAllText(File file) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();
        return text.toString();
    }


    public static byte[] readBytesFromFile(String path) {
        return readBytesFromFile(new File(path));
    }

    public static byte[] readBytesFromFile(File file) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bytesArray;
    }

    public static void delete(String path){
        delete(new File(path));
    }

    public static void delete(File file){
        file.delete();
    }
}