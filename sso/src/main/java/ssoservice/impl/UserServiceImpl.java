package ssoservice.impl;

import mymapper.TbUserMapper;
import mypojo.TbUser;
import mypojo.TbUserExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ssodao.JedisClient;
import ssoservice.UserService;
import utils.CookieUtils;
import utils.JsonUtils;
import utils.TaotaoResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String content, Integer type) {
        //创建查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //对数据进行校验,1\2\3分别代表username\phone\email
        //用户名校验
        if(type == 1) {
            criteria.andUsernameEqualTo(content);
        }else if(type == 2){
            criteria.andPhoneEqualTo(content);
        }else if(type == 3){
            criteria.andEmailEqualTo(content);
        }

        List<TbUser> list = userMapper.selectByExample(example);
        if(list == null || list.size() == 0){
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult createUser(TbUser user) {

        user.setCreated(new Date());
        user.setUpdated(new Date());

        //Spring框架中的md5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        userMapper.insert(user);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult userLogin(String name, String password, HttpServletRequest request, HttpServletResponse response) {

        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<TbUser> list = userMapper.selectByExample(userExample);
        //如果没有此用户名
        if(null == list || list.size() == 0){
            return TaotaoResult.build(400,"用户名或密码错误");
        }

        TbUser user = list.get(0);

        //比对密码
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return TaotaoResult.build(400,"用户名或密码错误");
        }

        //生成Token
        String token = UUID.randomUUID().toString();
        //保存用户之前，把用户对象的密码置空
        user.setPassword(null);
        //把用户信息写入Redis
        jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));

        //设置session的过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,SSO_SESSION_EXPIRE);

        //添加写cookie的逻辑,cookie有效期是关闭浏览器就失效
        CookieUtils.setCookie(request,response,"TT_TOKEN",token);

        //返回token
        return TaotaoResult.ok(token);
    }

    @Override
    public TaotaoResult getUserByToken(String token) {

        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"此session已过期，请重新登录");
        }

        //更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,SSO_SESSION_EXPIRE);
        //返回用户信息
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json,TbUser.class));
    }

    @Override
    public TaotaoResult userLogout(String token) {

        String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
        if(StringUtils.isBlank(json)){
            return TaotaoResult.build(400,"已退出登录");
        }

        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token,0);

        return TaotaoResult.ok();
    }
}
