import net.sf.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 46597 on 2018/3/10.
 */
public class JsonObjectTest {


    public static void main(String[] args) throws  Exception{

        JSONObject result = new JSONObject();
        //idea 控制台乱码 怎么解决？
        result.put("1","草师生");
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        result.put("2",list);
        System.out.println(result);


        String str = new String("草师生".getBytes(), "utf-8");

        System.out.println(str);


    }



}
