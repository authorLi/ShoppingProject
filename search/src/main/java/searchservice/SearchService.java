package searchservice;

import searchpojo.SearchResult;

public interface SearchService {

    public SearchResult search(String queryString,int page,int rows) throws Exception;
}
