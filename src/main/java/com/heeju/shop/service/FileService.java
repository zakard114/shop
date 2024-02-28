package com.heeju.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // UUID (Universally Unique Identifier) is used when giving names to distinguish
        // different objects. Since there is little chance of duplication in actual use, using it as the file name can
        // solve the file name duplication problem.
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // .substring() cuts the string (at the desired location). In this case, only "." + extension name is taken as ".".
        String savedFileName = uuid.toString() + extension; // Create the file name to be saved by combining the value
                                                            // received as UUID and the extension of the original file name.
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // The FileOutputStream class is a class that
                                                                        // emits byte-unit output. Create a file output
                                                                        // stream to write to the file by passing the location
                                                                        // where the file will be saved and the name of the file to the constructor.
        fos.write(fileData); // Insert file Data into the output stream as a file.
        fos.close();
        return savedFileName; // Returns the name of the uploaded file.
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath); // Create a file object using the path where the file is saved.

        if(deleteFile.exists()) {  // If the file exists, delete the file.
            deleteFile.delete();
            log.info("The file has been deleted.");
        } else {
            log.info("The file does not exist.");
        }
    }
}
