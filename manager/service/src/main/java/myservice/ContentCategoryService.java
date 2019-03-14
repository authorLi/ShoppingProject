package myservice;

import pojo.TreeNode;
import utils.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    List<TreeNode> getCategoryList(long parentId);

    TaotaoResult insertContentCategory(long parentId, String name);

    TaotaoResult deleteContentCategory(long parentId,long id);

    long getParentId(long id);

    TaotaoResult updateContentCategory(long id,String name);
}
