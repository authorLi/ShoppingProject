package portalservice.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalpojo.Order;
import portalservice.OrderService;
import utils.CookieUtils;
import utils.HttpClientUtil;
import utils.JsonUtils;
import utils.TaotaoResult;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${ORDER_BASE_URL}")
    private String ORDER_BASE_URL;
    @Value("${ORDER_CREATE_URL}")
    private String ORDER_CREATE_URL;

    @Override
    public String createOrder(Order order) {

        //创建订单之前应该补全用户信息
        //从cookie中获取TT_TOKEN的内容，根据token调用sso服务再根据token换取用户信息
        //调用order的服务提交订单
        String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
        //把json转换成TaotaoResult
        TaotaoResult result = TaotaoResult.format(json);
        if(result.getStatus() == 200){
            Object orderId = result.getData();
            return orderId.toString();
        }
        return "";
    }
}
