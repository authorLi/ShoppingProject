package portalservice.impl;

import mypojo.TbItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalpojo.CartItem;
import portalpojo.Item;
import portalservice.CartService;
import utils.CookieUtils;
import utils.HttpClientUtil;
import utils.JsonUtils;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;

    @Override
    public TaotaoResult addCartItem(HttpServletRequest request, HttpServletResponse response, Long itemId, int num) {
        //取商品信息
        CartItem cartItem = null;
        //取购物车商品列表
        List<CartItem> list = getCartItemList(request);
        //判断购物商品列表中是否存在此商品
        for (CartItem item : list) {
            //如果存在此商品
            if (item.getId() == itemId) {
                item.setNum(item.getNum() + num);
                cartItem = item;
                break;
            }
        }
        if (cartItem == null) {
            cartItem = new CartItem();
            //根据商品Id查询商品信息
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
            //把json转换成java对象
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
            if (result.getStatus() == 200) {
                TbItem item = (TbItem) result.getData();
                cartItem.setId(item.getId());
                cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
                cartItem.setNum(num);
                cartItem.setPrice(item.getPrice());
                cartItem.setTitle(item.getTitle());
            }
            //添加购物车列表
            list.add(cartItem);
        }
        //把购物车列表直接写入cookie
        CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        return TaotaoResult.ok();
    }

    @Override
    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {

        List<CartItem> list = getCartItemList(request);
        return list;
    }

    @Override
    public TaotaoResult deleteCartItem(HttpServletRequest request, HttpServletResponse response, long itemId) {
        //从cookie中取出商品购物车列表
        List<CartItem> list = getCartItemList(request, response);
        //从列表中找到此商品
        for (CartItem cartItem : list) {
            if (cartItem.getId() == itemId) {
                list.remove(cartItem);
                break;
            }
        }

        //把购物车列表直接写入cookie
        CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        return TaotaoResult.ok();
    }


    public List<CartItem> getCartItemList(HttpServletRequest request) {
        //从Cookie中取商品列表
        String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
        if (cartJson == null) {
            return new ArrayList<>();
        }
        //把json转换成商品列表
        try {
            List<CartItem> list = JsonUtils.jsonToList(cartJson, CartItem.class);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
}
