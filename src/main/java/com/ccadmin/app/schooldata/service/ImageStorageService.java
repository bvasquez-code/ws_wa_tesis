package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.entity.ExerciseImageEntity;
import com.ccadmin.app.schooldata.repository.ExerciseImageRepository;
import com.ccadmin.app.shared.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageStorageService extends SessionService {

    private static final String IMAGE_FOLDER = "data/tesis";

    @Autowired
    private ExerciseImageRepository exerciseImageRepository;

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
        /*Path target = Paths.get(IMAGE_FOLDER, filename);
        Files.createDirectories(target.getParent());
        Files.write(target, bytes);*/
        ExerciseImageEntity exerciseImage = new ExerciseImageEntity(filename,base64);
        exerciseImage.addSession(getUserCod());
        this.exerciseImageRepository.save(exerciseImage);
        return filename;
    }

    /**
     * Lee el archivo por nombre, lo convierte a Base64 Data URL
     * (con prefijo "data:image/png;base64,") y lo retorna.
     */
    public String loadImageAsBase64(String filename) throws IOException {
        String base64 = "";
        /*Path file = Paths.get(IMAGE_FOLDER, filename);
        byte[] bytes = Files.readAllBytes(file);
        String base64 = Base64.getEncoder().encodeToString(bytes);*/
        try{
            base64 = this.exerciseImageRepository.findById(filename).get().ImageBase64;
        }catch (Exception ex){
            base64 = this.exerciseImageRepository.findById("b3d0b104-7492-42d1-87a5-16d625de5433.png").get().ImageBase64;
        }

        return "data:image/png;base64," + base64;
    }
}
