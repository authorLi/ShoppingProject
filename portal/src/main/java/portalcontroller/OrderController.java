package portalcontroller;

import mypojo.TbUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import portalpojo.CartItem;
import portalpojo.Order;
import portalservice.CartService;
import portalservice.OrderService;
import utils.ExceptionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model) {
        //取购物车商品列表
        List<CartItem> list = cartService.getCartItemList(request, response);
        model.addAttribute("cartList", list);
        return "order-cart";
    }

    @RequestMapping("/create")
    public String createOrder(Order order, Model model, HttpServletRequest request) {

        try {
            //从request中获取用户信息
            TbUser user = (TbUser) request.getAttribute("user");
            //再order对象中补全用户信息
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            String orderId = orderService.createOrder(order);
            model.addAttribute("orderId", orderId);
            model.addAttribute("payment", order.getPayment());
            model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "生成订单出错，请稍后再试!");
            return "error/exception";
        }
    }


}
