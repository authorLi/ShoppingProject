package restservice.Impl;

import mymapper.TbItemDescMapper;
import mymapper.TbItemMapper;
import mymapper.TbItemParamItemMapper;
import mypojo.TbItem;
import mypojo.TbItemDesc;
import mypojo.TbItemParamItem;
import mypojo.TbItemParamItemExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restdao.JedisClient;
import restservice.ItemService;
import utils.JsonUtils;
import utils.TaotaoResult;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    @Override
    public TaotaoResult getItemBaseInfo(long itemId) {

        //添加缓存信息
        //从缓存中取商品信息
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
            //判断是否有值
            if(!StringUtils.isBlank(json)){
                //把json转换成java对象
                TbItem item = JsonUtils.jsonToPojo(json,TbItem.class);
                return TaotaoResult.ok(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //根据商品id查询商品信息
        TbItem item = itemMapper.selectByPrimaryKey(itemId);

        try {
            //把商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":base", JsonUtils.objectToJson(item));
            //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":base",REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }

        //使用TaotaoResult包装
        return TaotaoResult.ok(item);
    }

    @Override
    public TaotaoResult getItemDesc(long itemId) {

        //添加缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
            if(!StringUtils.isBlank(json)){
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json,TbItemDesc.class);
                return TaotaoResult.ok(itemDesc);
            }
        }catch (Exception e){

        }
        //创建查询条件
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try{
            jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":desc",JsonUtils.objectToJson(itemDesc));
            jedisClient.expire(REDIS_ITEM_KEY + ":" + "desc",REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok(itemDesc);
    }

    @Override
    public TaotaoResult getItemParam(long itemId) {

        //添加缓存
        try{
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
            if(!StringUtils.isBlank(json)){
                TbItemParamItem paramItem = JsonUtils.jsonToPojo(json,TbItemParamItem.class);
                return TaotaoResult.ok(paramItem);
            }
        }catch (Exception e){

        }

        //根据商品id查询规格参数
        //设置查询条件
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        //执行查询
        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
        if(list != null && list.size() > 0){
            TbItemParamItem paramItem = list.get(0);
            try {
                //把商品信息写入缓存
                jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(paramItem));
                //设置key的有效期
                jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":param",REDIS_ITEM_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
            return TaotaoResult.ok(paramItem);
        }

        return TaotaoResult.build(400,"无此商品规格");
    }
}
