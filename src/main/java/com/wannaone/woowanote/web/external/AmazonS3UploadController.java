package com.wannaone.woowanote.web.external;


import com.wannaone.woowanote.common.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/aws")
public class AmazonS3UploadController {
    @Autowired
    private S3Uploader s3Uploader;

    @PostMapping("/s3upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "image");
    }
}
