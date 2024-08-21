package com.dhan.pdf_generator.java.services;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;

@Service
public class ApiService {


    public  void downloadImage(String presignedUrl, String localFilePath) throws IOException {
        URL url = new URL(presignedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(localFilePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("Image downloaded successfully to " + localFilePath);
        } catch (IOException e) {
            System.err.println("Error downloading the image: " + e.getMessage());
            throw e;
        } finally {
            connection.disconnect();
        }
    }


    public  void uploadFile(String localFilePath, String presignedUrl) throws IOException {
        File file = new File(localFilePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + localFilePath);
        }

        URL url = new URL(presignedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("Content-Length", String.valueOf(file.length()));

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = connection.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("File uploaded successfully to " + presignedUrl);
            } else {
                System.err.println("Failed to upload the file. HTTP response code: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Error uploading the file: " + e.getMessage());
            throw e;
        } finally {
            connection.disconnect();
        }
    }





    public  void clearDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            throw new IOException("Directory does not exist: " + directory.getAbsolutePath());
        }

        if (!directory.isDirectory()) {
            throw new IOException("Provided path is not a directory: " + directory.getAbsolutePath());
        }

        File[] files = directory.listFiles();
        if (files != null) {  // Check to avoid NullPointerException
            for (File file : files) {
                if (file.isDirectory()) {
                    clearDirectory(file); // Recursively clear subdirectories
                }
                if (!file.delete()) {
                    throw new IOException("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }

        System.out.println("Directory cleared: " + directory.getAbsolutePath());
    }





}
