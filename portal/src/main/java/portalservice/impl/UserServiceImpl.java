package portalservice.impl;

import mypojo.TbUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import portalservice.UserService;
import utils.CookieUtils;
import utils.HttpClientUtil;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    @Value("${SSO_BASE_URL}")
    public String SSO_BASE_URL;
    @Value("${SSO_USER_TOKEN}")
    private String SSO_USER_TOKEN;
    @Value("${SSO_PAGE_LOGIN}")
    public String SSO_PAGE_LOGIN;
    @Value("${SSO_USER_LOGOUT}")
    public String SSO_USER_LOGOUT;

    @Override
    public TbUser getUserByToken(String token) {

        try {
            //调用SSO的服务，根据token取用户信息
            String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
            //把json转换成TaotaoResult
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbUser.class);
            if (result.getStatus() == 200) {
                TbUser user = (TbUser) result.getData();
                return user;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TaotaoResult logout(HttpServletRequest request) {
        try {
            String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
            HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_LOGOUT + token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }


}
