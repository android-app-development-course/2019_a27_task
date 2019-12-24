package cs.android.task;

import android.app.Application;



/*
 * @author sam
 * @date 19-12-23 下午2:38
 */

public class MyApplication extends Application {

    private static String host = "10.255.19.106";


    public  String getHost(){
        return host;
    }

}
