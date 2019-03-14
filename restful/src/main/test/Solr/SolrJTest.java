package Solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class SolrJTest {

    @Test
    public void addDocument() throws Exception {
        //创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/");
        //创建一个文档
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test001");
        document.addField("item_title","测试商品2");
        document.addField("item_price",54321    );
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/");
//        solrServer.deleteById("test001");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://localhost:8080/solr/");
        //创建一个查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.setQuery("*:*");
        solrQuery.setStart(20);
        solrQuery.setRows(50);
        //执行查询
        QueryResponse query = solrServer.query(solrQuery);
        //去查询结果
        SolrDocumentList solrDocuments = query.getResults();

        System.out.println("共查询到: " + solrDocuments.getNumFound());
        for(SolrDocument solrDocument : solrDocuments){
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
            System.out.println(solrDocument.get("item_image"));

        }
    }
}
