package mycontroller;

import myservice.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.TreeNode;
import utils.TaotaoResult;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<TreeNode> getContentCatList(@RequestParam(value = "id" , defaultValue = "0") Long parentId){
        List<TreeNode> result = contentCategoryService.getCategoryList(parentId);
        return result;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult createContentCategory(Long parentId, String name){
        TaotaoResult result = contentCategoryService.insertContentCategory(parentId,name);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long parentId,Long id){
        parentId = contentCategoryService.getParentId(id);
        TaotaoResult result = contentCategoryService.deleteContentCategory(parentId,id);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id,String name){
        TaotaoResult result = contentCategoryService.updateContentCategory(id,name);
        return result;
    }


}
