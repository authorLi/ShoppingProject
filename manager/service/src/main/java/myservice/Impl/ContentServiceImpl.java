package myservice.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mymapper.TbContentMapper;
import mypojo.TbContent;
import mypojo.TbContentExample;
import myservice.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pojo.EasyUIDataGridResult;
import utils.HttpClientUtil;
import utils.TaotaoResult;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;


    @Override
    public EasyUIDataGridResult getContentList(int page, int rows, long categoryId) {

        TbContentExample example = new TbContentExample();

        PageHelper.startPage(page,rows);

        if(categoryId != 0){
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> list = contentMapper.selectByExample(example);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        PageInfo<TbContent> pageInfo = new PageInfo<>();
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    @Override
    public TaotaoResult insertContent(TbContent content) {

        //补全pojo内容
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);

        try {
            //添加缓存同步逻辑
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        }catch (Exception e){
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }
}
