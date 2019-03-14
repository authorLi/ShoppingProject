import org.junit.Test;
import utils.JsonUtils;
import utils.TaotaoResult;

import java.util.LinkedList;
import java.util.List;

public class TestSolr {

    @Test
    public void test(){
        PPP p = new PPP();
        p.setUsername("zhangsan");
        p.setPassword("123456");
        String json = JsonUtils.objectToJson(p);
        TaotaoResult result = TaotaoResult.formatToPojo(json,PPP.class);
        System.out.println(result == null);
    }

    @Test
    public void test1(){
        int z = 3;
        int x = 1;
        int c = 2;

        Node first = new Node("第1位玩家");//头指针
        Node n = null;//当前指针
        Node foot = new Node("第" + z + "位玩家");//尾指针
        foot.next = first;
        Node ft = first;//从头开始
        for(int i = 0;i < z - 2;i++){
            n = new Node("第" + (z - 1) + "位玩家");
            ft.next = n;
            if(i == (z - 3)){
                n.next = foot;
            }
            n = n.next;
        }

        System.out.println(first);
        while(first.next != foot){
            System.out.println(first.next);
            first = first.next;
        }
        System.out.println(foot);

    }
}

class Node{
    public Node next;
    public String data;
    public Node(String data){
        this.data = data;
    }
    public String toString(){
        return this.data;
    }
}
