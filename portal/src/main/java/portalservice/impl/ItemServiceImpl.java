package portalservice.impl;

import mypojo.TbItemDesc;
import mypojo.TbItemParamItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalpojo.ItemInfo;
import portalservice.ItemService;
import utils.HttpClientUtil;
import utils.JsonUtils;
import utils.TaotaoResult;

import java.util.List;
import java.util.Map;
@Service
public class ItemServiceImpl implements ItemService {

    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ITEM_DESC_URL}")
    private String ITEM_DESC_URL;
    @Value("${ITEM_PARAM_URL}")
    private String ITEM_PARAM_URL;

    @Override
    public ItemInfo getItemById(Long itemId) {

        try {
            //调用rest的服务
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
            if(!StringUtils.isBlank(json)){
                TaotaoResult result = TaotaoResult.formatToPojo(json, ItemInfo.class);
                if(result.getStatus() == 200){
                    ItemInfo item = (ItemInfo) result.getData();
                    return item;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getItemDescById(Long itemId) {
        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_DESC_URL + itemId);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);
            if(taotaoResult.getStatus() == 200){
                TbItemDesc itemDesc = (TbItemDesc) taotaoResult.getData();
                String result = itemDesc.getItemDesc();
                return result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getItemParam(Long itemId) {
        try {
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_PARAM_URL + itemId);
            //把json转换成java
            TaotaoResult result = TaotaoResult.formatToPojo(json,TbItemParamItem.class);
            if(result.getStatus() == 200){
                TbItemParamItem itemParamItem = (TbItemParamItem) result.getData();
                String paramData = itemParamItem.getParamData();
                //生成html
                //把规格参数json数据转换成java对象
                List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
                StringBuffer sb = new StringBuffer();
                sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
                sb.append("    <tbody>\n");
                for(Map m1:jsonList) {
                    sb.append("        <tr>\n");
                    sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
                    sb.append("        </tr>\n");
                    List<Map> list1 = (List<Map>) m1.get("params");
                    for(Map m2:list1) {
                        sb.append("        <tr>\n");
                        sb.append("            <td class=\"tdTitle\">" + m2.get("k") + "</th>\n");
                        sb.append("            <td>" + m2.get("v") + "</td>\n");
                        sb.append("        </tr>\n");
                    }
                }
                sb.append("    </tbody>\n");
                sb.append("</table>");
                //返回HTML片段
                return sb.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
