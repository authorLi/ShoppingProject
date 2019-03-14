package searchdao.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import searchdao.SearchDao;
import searchpojo.Item;
import searchpojo.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {

    @Autowired
    private SolrServer solrServer;

    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        //返回值对象
        SearchResult searchResult = new SearchResult();
        //根据查询条件查询索引库
        QueryResponse queryResponse = solrServer.query(query);
        //取查询结果
        SolrDocumentList list = queryResponse.getResults();
        //取查询结果总数量
        searchResult.setRecordCount(list.getNumFound());
        //商品列表
        List<Item> itemList = new ArrayList<>();
        //取高亮显示
        Map<String, Map<String,List<String>>> highlighting = queryResponse.getHighlighting();
        //取商业列表
        for(SolrDocument solrDocument: list){
            //创建商品对象
            Item item = new Item();
            item.setId((String) solrDocument.get("id"));
            //取高亮显示结果
            List<String> stringList = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if(stringList != null&&stringList.size() > 0){
                title = stringList.get(0);
            }else{
                title = (String) solrDocument.get("item_title");
            }

            item.setTitle(title);
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));

            //添加的商品列表
            itemList.add(item);
        }

        searchResult.setItemList(itemList);
        return searchResult;
    }
}
