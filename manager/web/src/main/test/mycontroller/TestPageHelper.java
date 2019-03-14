package mycontroller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mymapper.TbItemMapper;
import mypojo.TbItem;
import mypojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {

    @Test
    public void testPageHelper(){
        //创建一个Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        //从Spring容器中获得Mapper的代理对象
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //执行查询，并分页
        TbItemExample example = new TbItemExample();
        //分页处理
        PageHelper.startPage(1,10);

        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);

        //取商品列表
        for(TbItem lists:list) {
            System.out.println(lists.getTitle());
        }

        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        Long total = pageInfo.getTotal();
        System.out.println("共有商品信息" + total);
    }
}
