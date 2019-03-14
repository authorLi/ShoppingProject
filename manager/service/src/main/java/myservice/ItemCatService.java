package myservice;

import mypojo.TbItemCat;
import pojo.TreeNode;

import java.util.List;

public interface ItemCatService {

    public List<TbItemCat> getItemCatList(long parentId);
}
