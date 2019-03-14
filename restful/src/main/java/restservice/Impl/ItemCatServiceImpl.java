package restservice.Impl;

import mymapper.TbItemCatMapper;
import mypojo.TbItemCat;
import mypojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restpojo.CatNode;
import restpojo.CatResult;
import restservice.ItemCatService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public CatResult getItemCatList() {

        CatResult result = new CatResult();
        //查询分类列表
        result.setData(getCatList(0));
        return result;
    }

    //查询分类列表的方法
    private List<?> getCatList(long parentId){

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);

        //返回值List
        List resultList = new ArrayList<>();

        int count = 0;
        for (TbItemCat itemCat:list){
            //判断是否为父节点
            if(itemCat.getIsParent()) {
                CatNode node = new CatNode();
                if (parentId == 0) {
                    node.setName("<a href='products/" + itemCat.getId() + ".html'>" + itemCat.getName() + "</a>");
                } else {
                    node.setName(itemCat.getName());
                }
                node.setUrl("/products/" + itemCat.getId() + ".html");
                node.setItem(getCatList(itemCat.getId()));
                resultList.add(node);
                //第一级第一层只取14条记录
                count ++;
                if(parentId == 0&&count >= 14) break;
            }else{
                //如果是叶子节点
                resultList.add("/products/" + itemCat.getId() + ".html|" + itemCat.getName());
            }
        }

        return resultList;
    }
}
