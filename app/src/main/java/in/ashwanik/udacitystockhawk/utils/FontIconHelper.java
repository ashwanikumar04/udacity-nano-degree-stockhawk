package in.ashwanik.udacitystockhawk.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.MaterialIcons;

import in.ashwanik.udacitystockhawk.R;


public class FontIconHelper {

    public static Drawable getFontDrawable(Context activity, MaterialIcons icon) {
        return getFontDrawable(activity, icon, R.color.white);
    }


    public static Drawable getFontDrawable(Context activity, MaterialIcons icon, int color) {
        return new IconDrawable(activity, icon)
                .colorRes(color)
                .actionBarSize();
    }

    public static Drawable getFontDrawable(Context activity, MaterialIcons icon, int sizeDp, int color) {
        return new IconDrawable(activity, icon)
                .colorRes(color)
                .sizeDp(sizeDp);
    }
}
