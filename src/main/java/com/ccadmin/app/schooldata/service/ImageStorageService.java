package com.ccadmin.app.schooldata.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageStorageService {

    private static final String IMAGE_FOLDER = "data/tesis";

    /**
     * Recibe un Data URL (p.ej. "data:image/png;base64,AAA..."),
     * decodifica el Base64 y lo guarda como archivo PNG,
     * devolviendo el nombre generado.
     */
    public String saveImage(String base64DataUrl) throws IOException {
        // Separa el encabezado "data:image/xxx;base64,"
        String base64 = base64DataUrl.substring(base64DataUrl.indexOf(',') + 1);
        byte[] bytes = Base64.getDecoder().decode(base64);
        String filename = UUID.randomUUID().toString() + ".png";
        Path target = Paths.get(IMAGE_FOLDER, filename);
        Files.createDirectories(target.getParent());
        Files.write(target, bytes);
        return filename;
    }

    /**
     * Lee el archivo por nombre, lo convierte a Base64 Data URL
     * (con prefijo "data:image/png;base64,") y lo retorna.
     */
    public String loadImageAsBase64(String filename) throws IOException {
        Path file = Paths.get(IMAGE_FOLDER, filename);
        byte[] bytes = Files.readAllBytes(file);
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/png;base64," + base64;
    }
}
