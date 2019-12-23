package cs.android.task;

import android.app.Application;



/*
 * @author sam
 * @date 19-12-23 下午2:38
 */

public class MyApplication extends Application {
    private String host = "10.242.93.99";


    public String getHost(){
        return host;
    }

}
