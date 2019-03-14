package ssoservice;

import mypojo.TbUser;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    TaotaoResult checkData(String content, Integer type);

    TaotaoResult createUser(TbUser user);

    TaotaoResult userLogin(String name, String password, HttpServletRequest request, HttpServletResponse response);

    TaotaoResult getUserByToken(String token);

    TaotaoResult userLogout(String token);
}
