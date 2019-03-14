package mycontroller;

import mypojo.TbContent;
import myservice.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pojo.EasyUIDataGridResult;
import utils.TaotaoResult;

@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(Long categoryId,Integer page, Integer rows){
        EasyUIDataGridResult result = contentService.getContentList(page,rows,categoryId);
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult saveContent(TbContent content){
        TaotaoResult result = contentService.insertContent(content);
        return result;
    }
}
