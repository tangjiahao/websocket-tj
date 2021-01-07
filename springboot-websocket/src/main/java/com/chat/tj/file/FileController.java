package com.chat.tj.file;

import com.chat.tj.model.vo.ResponseVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author tangjing
 * @date 2021/01/07 09:48
 */
@RestController
@RequestMapping("file/")
@Api("图片/文件接口")
@Slf4j
public class FileController {
    @Value("${file.path}")
    private String dirPath;

    @PostMapping(value = "upload")
    public ResponseVo<String> upload(MultipartFile file) {

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.failed("上传文件大小超过20MB");
        }
        //文件后缀
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + prefix;
        File folder = new File(dirPath);
        // 文件夹不存在就自动创建
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            // 将文件上传到指定路径dirpath
            Files.copy(inputStream, new File(dirPath + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            //拼接上传文件路径
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(fileName)
                    .toUriString();
            return ResponseVo.content(fileDownloadUri);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseVo.failed("上传失败，系统异常");
        }

    }
}
