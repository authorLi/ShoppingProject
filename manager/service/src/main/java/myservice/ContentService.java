package myservice;

import mypojo.TbContent;
import pojo.EasyUIDataGridResult;
import utils.TaotaoResult;

public interface ContentService {

    EasyUIDataGridResult getContentList(int page,int rows,long categoryId);

    TaotaoResult insertContent(TbContent content);
}
