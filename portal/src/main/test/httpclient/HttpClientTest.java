package httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTest {

    @Test
    public void doGet() throws Exception{
        //创建一个HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个GET对象
        HttpGet get = new HttpGet("http://www.sogou.com");
        //执行请求
        CloseableHttpResponse response = httpClient.execute(get);
        //取相应结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity,"utf-8");
        System.out.println(str);
        //关闭HttpClient对象
        response.close();
        httpClient.close();
    }

    @Test
    public void doGetWithParam() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();

        URIBuilder builder = new URIBuilder("http://www.sogou.com/web");
        builder.addParameter("query","花千骨");
        HttpGet get = new HttpGet(builder.build());
        CloseableHttpResponse response = httpClient.execute(get);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        HttpEntity entity = response.getEntity();
        String str = EntityUtils.toString(entity,"utf-8");
        System.out.println(str);
        response.close();
        httpClient.close();
    }

    @Test
    public void dePost() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建一个Post对象
        HttpPost post = new HttpPost("http://localhost:8082/httpclient/post.html");
        //执行post请求
        CloseableHttpResponse response = httpClient.execute(post);
        String str = EntityUtils.toString(response.getEntity());
        System.out.println(str);
        response.close();
        httpClient.close();
    }

    @Test
    public void doPostWithParam() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个Post对象
        HttpPost post = new HttpPost("http://localhost:8082/httpclient/post.html");
        //创建一个Entity，模拟一个表单
        List<NameValuePair> kvList = new ArrayList<>();
        kvList.add(new BasicNameValuePair("username","张三"));
        kvList.add(new BasicNameValuePair("password","123456"));

        //包装成一个Entity对象
        StringEntity entity = new UrlEncodedFormEntity(kvList);
        //设置请求内容
        post.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(post);
        String str = EntityUtils.toString(response.getEntity() ,"UTF-8");
        System.out.println(str);

        response.close();
        httpClient.close();

    }
}
