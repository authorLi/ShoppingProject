package portalservice;

import portalpojo.ItemInfo;

public interface ItemService {

    ItemInfo getItemById(Long itemId);
    String getItemDescById(Long itemId);
    String getItemParam(Long itemId);
}
