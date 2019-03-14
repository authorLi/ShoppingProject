package myservice;

import mypojo.TbItemParam;
import utils.TaotaoResult;

public interface ItemParamService {

    TaotaoResult getItemParamByCid(long cid);
    TaotaoResult insertItemParam(TbItemParam itemParam);
}
