package mycontroller;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import utils.FtpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestFTP {

    @Test
    public void testFTPClient() throws Exception{

        //创建一个FTPClient对象
        FTPClient ftpClient = new FTPClient();
        //创建一个FTP连接
        ftpClient.connect("127.0.0.1",21);
        //登录FTP服务器，使用用户名和密码
        ftpClient.login("anonymous","123456");
        //设置上传路径
        ftpClient.changeWorkingDirectory("D:/Ccleaner");
        //修改上传文件的格式
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        //上传文件，读取本地文件，第一个参数服务端文件名，第二个参数上传文件的InputStream
        ftpClient.storeFile("b.txt",new FileInputStream(new File("E:/a.txt")));
        System.out.println("成功");
        //关闭连接
        ftpClient.logout();
    }

    @Test
    public void testFTPUtils() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File("E:/a.txt"));
        FtpUtil.uploadFile("127.0.0.1",21,"anonymous","123456","D:/Ccleaner","","b.txt",inputStream);

    }
}
