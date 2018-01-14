package app.client;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ophir on 14/01/18.
 */
public class Pojo {

    public static class A {
        String fn;
        String ln;
    }

    public static void main(String[] args) {
        List<A> as = new ArrayList<>();
        A a = new A();
        a.fn = "fn";
        a.ln = "ln";
        as.add(a);
        System.out.println(new Gson().toJson(as));
    }
}
