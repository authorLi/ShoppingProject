package myservice.Impl;

import mymapper.TbItemParamMapper;
import mypojo.TbItemParam;
import mypojo.TbItemParamExample;
import myservice.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.TaotaoResult;

import java.util.Date;
import java.util.List;

//商品的规格参数模板
@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Override
    public TaotaoResult getItemParamByCid(long cid) {

        TbItemParamExample example = new TbItemParamExample();

        TbItemParamExample.Criteria criteria = example.createCriteria();

        criteria.andItemCatIdEqualTo(cid);

        List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);

        if(list != null && list.size() > 0){
            return TaotaoResult.ok(list.get(0));
        }

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult insertItemParam(TbItemParam itemParam) {

        //补全pojo
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
        //插入到规格参数模板表中
        itemParamMapper.insert(itemParam);
        return TaotaoResult.ok();
    }
}
