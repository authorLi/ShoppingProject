package portalpojo;

import mypojo.TbItem;

public class ItemInfo extends TbItem {

    public String[] getImages(){
        if(getImage() != null){
            String[] images = getImage().split(",");
            return images;
        }
        return null;
    }
}
