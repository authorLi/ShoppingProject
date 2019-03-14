package restservice;

import mypojo.TbContent;

import java.util.List;

public interface ContentService {
    List<TbContent> getContentList(long contentCid);
}
