import java.util.HashSet;
import java.util.Set;

/**
 * Created by 46597 on 2018/2/24.
 */
public class GernericTest {


    //泛型的擦除？
    public static void main(String[] args) {

        Set<String> set = new HashSet<>();
        set.add("曺");
        set.add("世");
        set.add("sheng");

        //Object[] str = set.toArray();
        //String[] str2 = (String[])str;

        //String[] objects =(String[]) objects1;

        String[] str = set.toArray(new String[0]);


        System.out.println(str);




    }




}
