package orderservice;

import mypojo.TbOrder;
import mypojo.TbOrderItem;
import mypojo.TbOrderShipping;
import utils.TaotaoResult;

import java.util.List;

public interface OrderService {

    TaotaoResult createOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping orderShipping);
}
