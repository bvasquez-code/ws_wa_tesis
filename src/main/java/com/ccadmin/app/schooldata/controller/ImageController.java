package com.ccadmin.app.schooldata.controller;

import com.ccadmin.app.schooldata.service.ImageStorageService;
import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageStorageService storageService;


    @PostMapping("/upload")
    public ResponseEntity<ResponseWsDto> upload(@RequestBody String base64Image) {
        try {
            String filename = storageService.saveImage(base64Image);
            return new ResponseEntity<>(
                    new ResponseWsDto().okResponse(filename),
                    HttpStatus.OK
            );
        } catch (IOException ex) {
            return new ResponseEntity<>(
                    new ResponseWsDto(ex),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/view/{filename}")
    public ResponseEntity<ResponseWsDto> view(@PathVariable String filename) {
        try {
            String dataUrl = storageService.loadImageAsBase64(filename);
            return new ResponseEntity<>(
                    new ResponseWsDto().okResponse(dataUrl),
                    HttpStatus.OK
            );
        } catch (NoSuchFileException ex) {
            return new ResponseEntity<>(
                    new ResponseWsDto("Imagen no encontrada"),
                    HttpStatus.NOT_FOUND
            );
        } catch (IOException ex) {
            return new ResponseEntity<>(
                    new ResponseWsDto(ex),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
