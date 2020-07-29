package com.obarong.testtextureview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// java byte【】数组与文件读写（增加新功能) - 平常心，平常心 - 博客园
//https://www.cnblogs.com/shuqingstudy/p/5135851.html

public class FileUtils {

    //第一种获取文件内容方式
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "+ file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * 方法二：
     * 根据byte数组，生成文件
     */
    public static void writeFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;

        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+"//"+fileName);
            /* 使用以下2行代码时，不追加方式*/
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bfile);

            /* 使用以下3行代码时，追加方式*/
//            bos = new BufferedOutputStream(new FileOutputStream(file, true));
//            bos.write(bfile);
//            bos.write("\r\n".getBytes());


            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

}
