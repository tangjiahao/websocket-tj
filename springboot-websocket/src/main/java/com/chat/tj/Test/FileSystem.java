package com.chat.tj.Test;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileSystem {
    public static void createFolder(String folderPath) {

        File folder = new File(folderPath);
        System.out.println("开始创建文件夹：" + folderPath);
        if (folder.mkdirs()) {
            System.out.println("文件夹创建成功:");
            return;
        }
        System.out.println("文件夹创建失败，请检查路径是否正确:");
    }

    public static void createFile(String folderPath, String fileName) {
        File file = new File(folderPath, fileName);
        System.out.println("开始在路径：" + folderPath + "创建文件：" + fileName);
        File folder = new File(folderPath);
        // 文件夹路径不存在就创建
        if (!folder.exists()) {
            folder.mkdirs();
        }
        try {
            if (file.createNewFile()) {
                System.out.println("文件创建成功");
            }
        } catch (IOException e) {
            System.out.println("文件创建失败");
            e.printStackTrace();
        }
    }

    public static void createFile2(String filePath) {
        File file = new File(filePath);
        System.out.println("开始创建文件：" + filePath);
        try {
            if (file.createNewFile()) {
                System.out.println("文件创建成功");
            }
        } catch (IOException e) {
            System.out.println("文件创建失败");
            e.printStackTrace();
        }
    }

    public static void deleteFolder(String folderPath) {
        System.out.println("开始删除文件夹/文件；" + folderPath);
        File file = new File(folderPath);
        if (file.isFile()) {
            file.delete();
            System.out.println("删除完成");
            return;
        }
        deleteDirectory(file);
        System.out.println("删除完成");
    }

    public static void deleteDirectory(File file) {
        File[] files = file.listFiles();
        //如果包含文件进行删除操作
        if (files != null) {
            for (File value : files) {
                // 是文件夹递归删除
                if (value.isDirectory()) {
                    deleteDirectory(value);
                }
                if (value.isFile()) {
                    //删除子文件
                    value.delete();
                }
            }
        }
        file.delete();
    }

    public static void main(String[] args) {
        //相对路径在项目目录创建文件夹
//        createFolder("codeCreate");
        //绝对路径指定项目目录创建文件夹
        //createFolder("E:\\FileTest\\codefile");
        //相对路径在项目目录创建文件
//        createFile("static\\excel\\","ace.xlsx");
        //绝对路径指定项目目录创建文件
        //createFile("E:\\FileTest\\codefile","hello.txt");
        // 绝对路径删除文件夹和包含的文件
//        deleteFolder("E:\\FileTest\\codefile");
//        deleteFolder("codeCreate");
        deleteFolder("E:\\FileTest\\hello.txt");


    }
}
