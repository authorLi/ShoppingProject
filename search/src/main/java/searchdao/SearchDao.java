package searchdao;

import org.apache.solr.client.solrj.SolrQuery;
import searchpojo.SearchResult;

public interface SearchDao {

    SearchResult search(SolrQuery query) throws Exception;
}
