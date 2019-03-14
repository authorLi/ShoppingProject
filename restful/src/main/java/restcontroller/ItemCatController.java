package restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import restpojo.CatResult;
import restservice.ItemCatService;
import utils.JsonUtils;

@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/itemcat/all",produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
    @ResponseBody
    public String getItemCatList(String callback){
        CatResult catResult = itemCatService.getItemCatList();
        //把pojo转换成字符串
        String json = JsonUtils.objectToJson(catResult);
        String result = callback + "(" + json + ");";
        return result;
    }

//    @RequestMapping("itemcat/list")
//    @ResponseBody
//    public Object getItemCatList(String callback){
//        CatResult catResult = itemCatService.getItemCatList();
//        MappingJacksonValue value = new MappingJacksonValue(catResult);
//        value.setJsonpFunction(callback);
//        return value;
//    }

}
