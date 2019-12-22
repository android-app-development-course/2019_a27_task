package cs.android.task.entity;


/*
 * @author sam
 * @date 19-11-25 下午3:51
 */

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;



import cs.android.task.R;

public class Friend {
    private String name;
    private String introduction;
    private Bitmap image;

    public Friend(String name, String introduction, Bitmap image) {
        this.name = name;
        this.introduction = introduction;
        this.image = image;
    }

    public Friend(String name, String introduction){
        this.name = name;
        this.introduction = introduction;
        this.image = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
