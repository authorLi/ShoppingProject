package myservice;

import mypojo.TbItem;
import pojo.EasyUIDataGridResult;
import utils.TaotaoResult;

public interface ItemService {

    TbItem getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page,int rows);

    TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception;
}
