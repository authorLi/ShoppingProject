package interceptor;

import mypojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import portalservice.impl.UserServiceImpl;
import utils.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    //调用本包下的UserServiceImpl
    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //在Handler执行之前处理
        //判断用户是否登录
        //从cookie中查询token
        String token = CookieUtils.getCookieValue(httpServletRequest,"TT_TOKEN");
        //根据token换取用户信息，调用SSO系统的接口的服务
        TbUser user = userService.getUserByToken(token);
        //取不到用户信息，跳转到登录页面，把用户的url作为参数传递给登录页面
        if(user == null){
            httpServletResponse.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
        //返回false
        //返回值决定Handler是否执行
        //取到用户信息，放行
        httpServletRequest.setAttribute("user",user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        //在Handler执行之后，返回ModelAndView之前
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //返回ModelAndView之后
        //响应用户之后
    }
}
