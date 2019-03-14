package myservice.Impl;

import mymapper.TbContentCategoryMapper;
import mypojo.TbContentCategory;
import mypojo.TbContentCategoryExample;
import myservice.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.TreeNode;
import utils.TaotaoResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;


    @Override
    public List<TreeNode> getCategoryList(long parentId) {

        //根据parentId查询节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();

        TbContentCategoryExample.Criteria criteria = example.createCriteria();

        criteria.andParentIdEqualTo(parentId);

        //执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

        List<TreeNode> result = new ArrayList<>();

        for (TbContentCategory tbContentCategory : list){
            //创建一个节点
            TreeNode node = new TreeNode(tbContentCategory.getId(),
                    tbContentCategory.getName(),
                    tbContentCategory.getIsParent()?"closed":"open");

            result.add(node);
        }

        return result;
    }

    @Override
    public TaotaoResult insertContentCategory(long parentId, String name) {

        //创建一个pojo
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setIsParent(false);
        //状态可选：1是正常，2是删除
        contentCategory.setStatus(1);
        contentCategory.setParentId(parentId);
        contentCategory.setSortOrder(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //添加纪录
        contentCategoryMapper.insert(contentCategory);
        //查看父节点的isParent是否为true，如果不是true则改为true
        TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parentCat.getIsParent()){
            parentCat.setIsParent(true);
            //更新父节点
            contentCategoryMapper.updateByPrimaryKey(parentCat);
        }

        //返回结果
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult deleteContentCategory(long parentId, long id) {

        contentCategoryMapper.deleteByPrimaryKey(id);
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List list = contentCategoryMapper.selectByExample(example);

        TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
        if(list == null){
            parentCat.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parentCat);
        }

        return TaotaoResult.ok(parentCat);
    }

    @Override
    public long getParentId(long id){
       TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
       return contentCategory.getParentId();
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setId(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return TaotaoResult.ok();
    }
}
