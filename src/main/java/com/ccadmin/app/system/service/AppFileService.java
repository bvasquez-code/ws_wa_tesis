package com.ccadmin.app.system.service;

import com.ccadmin.app.shared.model.dto.ResponseWsDto;
import com.ccadmin.app.shared.model.entity.BusinessConfigEntity;
import com.ccadmin.app.shared.model.entity.id.BusinessConfigEntityID;
import com.ccadmin.app.shared.service.BusinessConfigService;
import com.ccadmin.app.shared.service.SessionService;
import com.ccadmin.app.system.model.dto.AppFileDto;
import com.ccadmin.app.system.model.entity.AppFileEntity;
import com.ccadmin.app.system.repository.AppFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.UUID;

@Service
public class AppFileService extends SessionService {

    @Autowired
    private AppFileRepository appFileRepository;
    @Autowired
    BusinessConfigService businessConfigService;

    public AppFileEntity findById(String FileCod)
    {
        return this.appFileRepository.findById(FileCod).get();
    }

    public ResponseWsDto save(AppFileDto appFileDto)
    {
        try{
            AppFileEntity appFile = new AppFileEntity();

            BusinessConfigEntity physicalRoute = this.businessConfigService.findById(
                    new BusinessConfigEntityID("ConfigurationFiles",1)
            );
            BusinessConfigEntity hostRoute = this.businessConfigService.findById(
                    new BusinessConfigEntityID("ConfigurationFiles",2)
            );

            appFile.FileType = getTypeFile(appFileDto.extension);
            appFile.FileCod = generateCodFile(appFile.FileType);
            appFile.Name = appFile.FileCod + "." + appFileDto.extension;
            appFile.Route = physicalRoute.ConfigVal + appFile.Name;
            appFile.Description = "no Description";
            appFile.addSession(getUserCod(),true);

            byte[] imageBytes = Base64.getDecoder().decode(appFileDto.base64.split(",")[1]);

            Path path = Paths.get(appFile.Route);

            Files.write(path, imageBytes);

            appFile.Route = hostRoute.ConfigVal + "image/" + appFile.Name;
            return new ResponseWsDto(
                    this.appFileRepository.save(appFile)
            );
        }catch (Exception ex){
            return new ResponseWsDto(ex);
        }
    }

    public String getTypeFile(String extension){

        String[] imageExtensions = {
                "jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp", "ico", "heif", "svg"
        };

        if( Arrays.stream(imageExtensions).toList().stream().filter(
                e -> e.toUpperCase().equals(extension.toUpperCase()) ).toList().size() > 0
        )
        {
            return "IMAGE";
        }

        return "OTHER";
    }

    private String generateCodFile(String typeFile){

        Map<String, String> typeMapping = new HashMap<>();
        typeMapping.put("IMAGE", "IMG");
        typeMapping.put("OTHER", "OTR");
        typeMapping.put("DOCUMENT", "DOC");
        typeMapping.put("VIDEO", "VID");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = today.format(formatter);

        String suffix = typeMapping.get(typeFile);
        String baseCode = formattedDate + UUID.randomUUID().toString().replace("-","");

        if(baseCode.length()>17){
            baseCode = baseCode.substring(0,17);
        }
        while (baseCode.length() < 17) {
            baseCode = "0" + baseCode;
        }
        return suffix + baseCode;
    }


}
