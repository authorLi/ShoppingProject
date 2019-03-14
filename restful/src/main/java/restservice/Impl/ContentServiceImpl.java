package restservice.Impl;

import mymapper.TbContentMapper;
import mypojo.TbContent;
import mypojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restdao.JedisClient;
import restservice.ContentService;
import utils.JsonUtils;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;


    @Override
    public List<TbContent> getContentList(long contentCid) {

        //从缓存中获取内容
        try{
            String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY,contentCid + "");
            if(!StringUtils.isBlank(result)){
                //把字符串转换成List
                List<TbContent> list = JsonUtils.jsonToList(result,TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //根据内容分类id查询内容列表
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(contentCid);

        //执行查询
        List<TbContent> result = contentMapper.selectByExample(example);

        //向缓存中添加内容
        try {
            //把result转换成字符串
            String cacheString = JsonUtils.objectToJson(result);
            jedisClient.hset(INDEX_CONTENT_REDIS_KEY,contentCid + "",cacheString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }
}
