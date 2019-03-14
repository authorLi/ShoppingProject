package orderservice.impl;

import mymapper.TbOrderItemMapper;
import mymapper.TbOrderMapper;
import mymapper.TbOrderShippingMapper;
import mypojo.TbOrder;
import mypojo.TbOrderItem;
import mypojo.TbOrderShipping;
import orderdao.JedisClient;
import orderservice.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utils.TaotaoResult;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_INIT_ID}")
    private String ORDER_INIT_ID;
    @Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;

    @Override
    public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping orderShipping) {

        //向订单表中插入订单明细
        //获得订单号
        String str = jedisClient.get(ORDER_GEN_KEY);
        if (StringUtils.isBlank(str)) {
            jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
        }
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
        //补全pojo属性
        order.setOrderId(orderId + "");
        //状态：1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        //状态： 1.评价 0.未评价
        order.setBuyerRate(0);
        orderMapper.insert(order);
        //插入订单明细
        for (TbOrderItem orderItem : list) {
            //补全订单明细
            //取订单明细
            long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
            orderItem.setId(orderDetailId + "");
            orderItem.setOrderId(orderId + "");
            //向订单明细插入记录
            orderItemMapper.insert(orderItem);
        }
        //插入物流表
        //补全物流表的属性
        orderShipping.setOrderId(orderId + "");
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);

        return TaotaoResult.ok(orderId);
    }
}
