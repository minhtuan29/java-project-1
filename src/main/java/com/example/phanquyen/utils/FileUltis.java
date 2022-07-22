package com.example.phanquyen.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;

public class FileUltis {


    static String STATIC_PATH = Paths.get("").toAbsolutePath() + "/src/main/resources/static/";

    public static void saveFile(MultipartFile file, String staticDir, String namedFile) throws Exception{
        try{
            file.transferTo(new File(STATIC_PATH + staticDir, namedFile));
        }catch (Exception e){
            throw new Exception("Server lưu file không được, có thể bộ nhớ server đã hết hoặc một lý do gì đó");
        }

    }


}
