package portalcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import portalpojo.SearchResult;
import portalservice.SearchService;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String search(@RequestParam(value = "q") String queryString, @RequestParam(defaultValue = "1") Integer page, Model model){
        if(queryString != null){
            try {
                queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        SearchResult search = searchService.search(queryString,page);
        //向页面传递参数
        model.addAttribute("query",queryString);
        model.addAttribute("totalPage",search.getPageCount());
        model.addAttribute("itemList",search.getItemList());
        model.addAttribute("page",page);

        return "search";
    }

}
