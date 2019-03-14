package portalservice;

import mypojo.TbUser;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    TbUser getUserByToken(String token);

    TaotaoResult logout(HttpServletRequest request);
}
