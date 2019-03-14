package searchcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import searchservice.ItemService;
import utils.TaotaoResult;

@Controller
@RequestMapping("/manager")
public class ItemController {

    @Autowired
    private ItemService itemService;

    //导入商品数据到索引
    @RequestMapping("/importAll")
    @ResponseBody
    public TaotaoResult importAllItem(){
        TaotaoResult result = itemService.importAllItems();
        return result;
    }
}
