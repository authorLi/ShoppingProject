package portalservice.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalpojo.SearchResult;
import portalservice.SearchService;
import utils.HttpClientUtil;
import utils.TaotaoResult;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Value("${SEARCH_BASE_URL}")
    private String SEARCH_BASE_URL;

    @Override
    public SearchResult search(String queryString, int page) {
        //调用Search的服务
        //查询参数
        Map<String,String> map = new HashMap<>();
        map.put("q",queryString);
        map.put("page",page + "");
        try {
            //调用服务
            String json = HttpClientUtil.doGet(SEARCH_BASE_URL, map);
            //把字符串转换成java对象
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, SearchResult.class);
            if(taotaoResult.getStatus() == 200){
                SearchResult searchResult = (SearchResult) taotaoResult.getData();
                return searchResult;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
