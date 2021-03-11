package com.chat.tj.Test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Slf4j
public class FileSystem extends RuntimeException {
    public static void createFolder(String folderPath) {

        File folder = new File(folderPath);
        System.out.println("开始创建文件夹：" + folderPath);
        if (folder.mkdirs()) {
            System.out.println("文件夹创建成功");
            return;
        }
        System.out.println("文件夹创建失败，请检查路径是否正确:" + folderPath);
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
            System.out.println("文件创建失败" + fileName);
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
        System.out.println(folderPath + "删除完成");
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

    public static String getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.isFile() || file.isDirectory()) {
            long fileSize = FileUtils.sizeOf(file);
            StringBuilder result = new StringBuilder();
            result.append("共").append(fileSize).append("b(字节)");
            if (fileSize >= 1024) {
                result.append("," + fileSize / 1024).append("kb");
            }
            return result.toString();
        }
        System.out.println("路径不正确，获取大小失败");
        return null;
    }

    public static void menu() {
        System.out.println("1:创建文件夹");
        System.out.println("2:创建文件");
        System.out.println("3:删除文件夹或者文件");
        System.out.println("4:获取文件夹或者文件的大小");
        System.out.println("5:结束");
    }

    public static void mainMenu() {
        System.out.println("请选择功能：");
        Scanner input1 = new Scanner(System.in);
        int choose = input1.nextInt();
        switch (choose) {
            case 1: {
                System.out.println("请输入创建文件夹：如E:\\FileTest(或者相对路径)");
                Scanner input2 = new Scanner(System.in);
                String path = input2.nextLine();
                createFolder(path);
            }
            break;
            case 2: {
                System.out.println("请输入文件所在文件夹：如E:\\FileTest(或者相对路径)");
                Scanner input2 = new Scanner(System.in);
                String path = input2.nextLine();
                System.out.println("请输入创建的文件名：如hello.txt");
                Scanner input3 = new Scanner(System.in);
                String fileName = input3.nextLine();
                createFile(path, fileName);
            }
            break;
            case 3: {
                System.out.println("请输入删除的文件夹或者文件：如E:\\FileTest\\hello.txt(或者相对路径)");
                Scanner input2 = new Scanner(System.in);
                String path = input2.nextLine();
                deleteFolder(path);
            }
            break;
            case 4: {
                System.out.println("请输入获取文件夹或者文件的路径：如E:\\FileTest");
                Scanner input2 = new Scanner(System.in);
                String path = input2.nextLine();
                System.out.println(getFileSize(path));
            }
            break;
            case 5:
                throw new FileSystem();//抛出异常，终止递归
            default:
                System.out.println("错误的功能选择");
        }
        mainMenu();
    }

    public static void main(String[] args) {
        //相对路径在项目目录创建文件夹
//        createFolder("codeCreate");
        //绝对路径指定项目目录创建文件夹
        //createFolder("E:\\FileTest\\codefile");
        //createFolder("E:\\FileTest\\acefile");
        //相对路径在项目目录创建文件
        //createFile("static\\excel\\","ace.xlsx");
        //绝对路径指定项目目录创建文件
        //createFile("E:\\FileTest\\codefile","hello.txt");

        // 绝对路径删除文件夹和包含的文件
//        deleteFolder("E:\\FileTest\\codefile");
//        deleteFolder("codeCreate");
        //deleteFolder("E:\\FileTest\\hello.txt");
        //deleteFolder("static");
        // 获得文件或者文件夹大小
        //System.out.println(getFileSize("E:\\FileTest"));
        //System.out.println(getFileSize("E:\\FileTest\\codefile\\hello.txt"));
        try {
            menu();
            mainMenu();
        } catch (FileSystem e) {
            System.out.println("程序退出");
        }


    }
}
