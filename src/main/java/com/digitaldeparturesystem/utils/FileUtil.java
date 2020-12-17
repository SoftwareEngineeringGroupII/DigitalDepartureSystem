package com.digitaldeparturesystem.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUtil {

    public static int download(String filename, HttpServletResponse res){
        try {
            // 发送给客户端的数据
            OutputStream outputStream = res.getOutputStream();
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            // 读取filename
            bis = new BufferedInputStream(new FileInputStream(new File("./file/" + filename)));
            int i = bis.read(buff);
            while (i != -1) {
                outputStream.write(buff, 0, buff.length);
                outputStream.flush();
                i = bis.read(buff);
            }
            return 1;
        } catch (IOException e) {
            return -1;
        }
    }

}