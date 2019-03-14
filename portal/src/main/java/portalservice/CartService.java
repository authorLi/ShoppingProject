package portalservice;

import portalpojo.CartItem;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {

    TaotaoResult addCartItem(HttpServletRequest request, HttpServletResponse response, Long itemId, int num);

    List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response);

    TaotaoResult deleteCartItem(HttpServletRequest request, HttpServletResponse response, long itemId);
}
