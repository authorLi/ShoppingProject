package mycontroller;

import mypojo.TbItemCat;
import myservice.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.TreeNode;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("list")
    @ResponseBody
    public List<TreeNode> getItemCatList(@RequestParam(value = "id",
            defaultValue =  "0")Long parentId){
        List<TreeNode> list = new ArrayList<>();

        List<TbItemCat> result = itemCatService.getItemCatList(parentId);

        for(TbItemCat lists:result){
            TreeNode node = new TreeNode(lists.getId(),lists.getName(),lists.getIsParent()?"close":"open");
            list.add(node);
        }
        return list;
    }
}
