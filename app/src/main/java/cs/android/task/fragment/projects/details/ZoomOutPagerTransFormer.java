package cs.android.task.fragment.projects.details;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.viewpager2.widget.ViewPager2;

public final class ZoomOutPagerTransFormer implements ViewPager2.PageTransformer {
private static final float MIN_SCALE = 0.9F;
private static final float MIN_ALPHA = 0.5F;

public void transformPage(@NotNull View view, float position) {
  int pageWidth = view.getWidth();
  int pageHeight = view.getHeight();
  if (position < (float) -1) {
    view.setAlpha(0.0F);
  } else if (position <= (float) 1) {
    float vertMargin = 0.9F;
    float var9 = (float) 1;
    boolean var7 = false;
    float var10 = Math.abs(position);
    float horzMargin = var9 - var10;
    boolean var8 = false;
    float scaleFactor = Math.max(vertMargin, horzMargin);
    vertMargin = (float) pageHeight * ((float) 1 - scaleFactor) / (float) 2;
    horzMargin = (float) pageWidth * ((float) 1 - scaleFactor) / (float) 2;
    if (position < (float) 0) {
      view.setTranslationX(horzMargin - vertMargin / (float) 2);
    } else {
      view.setTranslationX(-horzMargin + vertMargin / (float) 2);
    }

    view.setScaleX(scaleFactor);
    view.setScaleY(scaleFactor);
    view.setAlpha(0.5F + (scaleFactor - 0.9F) / 0.100000024F * 0.5F);
  } else {
    view.setAlpha(0.0F);
  }
}
}
