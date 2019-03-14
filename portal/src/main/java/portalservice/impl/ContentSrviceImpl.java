package portalservice.impl;

import mypojo.TbContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalservice.ContentService;
import utils.HttpClientUtil;
import utils.JsonUtils;
import utils.TaotaoResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentSrviceImpl implements ContentService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_INDEX_AD_URL}")
    private String REST_INDEX_AD_URL;

    @Override
    public String getContentList() {

        //调用服务层服务
        String str = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL);
        try{
            //把字符串转换成TaoTaoResult
            TaotaoResult result = TaotaoResult.formatToList(str, TbContent.class);
            //取内容列表
            List<TbContent> list = (List<TbContent>) result.getData();
            List<Map> resultList = new ArrayList<>();
            //创建一个jsp页码要求的pojo列表
            for (TbContent tbContent:list) {
                Map map = new HashMap();
                map.put("src",tbContent.getPic());
                map.put("height",240);
                map.put("width",670);
                map.put("srcB",tbContent.getPic2());
                map.put("widthB",550);
                map.put("heightB",240);
                map.put("href",tbContent.getUrl());
                map.put("alt",tbContent.getSubTitle());
                resultList.add(map);
            }
            return JsonUtils.objectToJson(resultList);
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }
}
