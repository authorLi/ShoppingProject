package myservice.Impl;

import myservice.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.FtpUtil;
import utils.IDUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class PictureServiceImpl implements PictureService {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;


    @Override
    public Map uploadPicture(MultipartFile uploadFile){

        Map map = new HashMap();

        //生成一个新的文件名，取原始文件名
        String oldName = uploadFile.getOriginalFilename();
        //生成新文件名
//        UUID.randomUUID();
        String newName = IDUtils.genImageName();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        String imagePath = "";
        //图片上传
        try {
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS,FTP_PORT,FTP_USERNAME,FTP_PASSWORD,FTP_BASE_PATH,
                    imagePath,newName,uploadFile.getInputStream());

            if(!result){
                map.put("error",1);
                map.put("message","上传失败");
                return map;
            }
        } catch (IOException e) {
            map.put("error",1);
            map.put("message","文件上传发生异常");
            e.printStackTrace();
        }
        map.put("error",0);
        map.put("url",IMAGE_BASE_URL + imagePath + "/" + newName);
        return map;
    }
}
