package cs.android.task.entity;


/*
 * @author sam
 * @date 19-11-25 下午3:51
 */

import android.graphics.Bitmap;


public class Friend {
    private String name;
    private String phoneNumber;
    private Bitmap image;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
