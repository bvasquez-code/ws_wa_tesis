package com.ccadmin.app.schooldata.service;

import com.ccadmin.app.schooldata.model.dto.ImageRegisterMassiveDto;
import com.ccadmin.app.schooldata.model.entity.ExerciseImageEntity;
import com.ccadmin.app.schooldata.repository.ExerciseImageRepository;
import com.ccadmin.app.shared.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class ImageStorageService extends SessionService {

    private static final String IMAGE_FOLDER = "data/tesis";
    private static final Boolean flgSavePhysicalPath = false;

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
        String filename = UUID.randomUUID().toString() + ".png";
        if(flgSavePhysicalPath){
            this.savePhysicalPath(filename,base64);
        }
        ExerciseImageEntity exerciseImage = new ExerciseImageEntity(filename,base64);
        exerciseImage.addSession(getUserCod());
        this.exerciseImageRepository.save(exerciseImage);
        return filename;
    }

    public List<String> saveImage(ImageRegisterMassiveDto request) throws IOException {
        List<String> rptImg = new ArrayList<>();
        for(var item : request.imageList){
            String nameImg = saveImage(item.base64Image,item.filename);
            rptImg.add(nameImg);
        }
        return rptImg;
    }

    public String saveImage(String base64DataUrl,String filename) throws IOException {
        // Separa el encabezado "data:image/xxx;base64,"
        String base64 = base64DataUrl.substring(base64DataUrl.indexOf(',') + 1);
        if(flgSavePhysicalPath){
            this.savePhysicalPath(filename,base64);
        }
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

        if(flgSavePhysicalPath){
            base64 = this.findPhysicalPath(filename);
            return "data:image/png;base64," + base64;
        }

        try{
            base64 = this.exerciseImageRepository.findById(filename).get().ImageBase64;
        }catch (Exception ex){
            base64 = this.exerciseImageRepository.findById("b3d0b104-7492-42d1-87a5-16d625de5433.png").get().ImageBase64;
        }
        return "data:image/png;base64," + base64;
    }

    private void savePhysicalPath(String filename,String base64) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        Path target = Paths.get(IMAGE_FOLDER, filename);
        Files.createDirectories(target.getParent());
        Files.write(target, bytes);
    }

    private String findPhysicalPath(String filename) throws IOException {
         Path file = Paths.get(IMAGE_FOLDER, filename);
        byte[] bytes = Files.readAllBytes(file);
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/png;base64," + base64;
    }
}
