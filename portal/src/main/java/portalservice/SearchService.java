package portalservice;

import portalpojo.SearchResult;

public interface SearchService {

    SearchResult search(String queryString,int page);
}
