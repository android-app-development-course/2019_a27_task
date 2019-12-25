package cs.android.task.view;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

public class Util {
/**
 * 沉浸式状态栏
 */
public static void immerseStatusBar(Activity activity) {
    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    activity.getWindow()
            .getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
}
}
