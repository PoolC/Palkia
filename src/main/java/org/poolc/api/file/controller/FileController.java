package org.poolc.api.file.controller;

import io.restassured.internal.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    @Value("${file.file-dir}")
    private String fileDir;

    @GetMapping(value = "/{fileName}")
    public ResponseEntity sendFile(@PathVariable(name = "fileName") String fileName) {
        String filePath = fileDir + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                InputStream inputStream = new FileInputStream(filePath);
                byte[] out = IOUtils.toByteArray(inputStream);
                return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                        .body(out);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            return ResponseEntity.badRequest().body("존재하지 않는 파일입니다");
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> uploadFile(@ModelAttribute MultipartFile file) {
        try {

            int pos = file.getOriginalFilename().lastIndexOf(".");
            if(pos ==-1){
                pos = file.getOriginalFilename().length();
            }
            String fileNameWithOutSpace = file.getOriginalFilename().replace(' ', '-').substring(0,pos)+RandomStringGenerator()+file.getOriginalFilename().replace(' ', '-').substring(pos);
//            String fileNameWithOutSpace =file.getOriginalFilename().replace(' ', '-');
            Path path = Paths.get(fileDir + fileNameWithOutSpace);
            if (Files.exists(path)) {
                return ResponseEntity.badRequest().body("이미 존재하는 파일명입니다. 파일 명을 수정해주세요");
            }
            file.transferTo(path);
            return ResponseEntity.ok().body(URI.create("/files/" + URLEncoder.encode(fileNameWithOutSpace, StandardCharsets.UTF_8)).toString());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String RandomStringGenerator(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        for(int i=0;i<7;i++){
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}
