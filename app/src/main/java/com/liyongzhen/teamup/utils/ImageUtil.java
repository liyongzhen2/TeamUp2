package com.liyongzhen.teamup.utils;

import android.graphics.Bitmap;

import com.liyongzhen.teamup.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by alex2 on 7/26/2017.
 */

public class ImageUtil {
    public static DisplayImageOptions DISPLAYIMAGEOPTION_RETANGLE;
    public static DisplayImageOptions DISPLAYIMAGEOPTION_CIRCLE;

    public static DisplayImageOptions getDisplayImageOptionCircle(int size) {
        if (ImageUtil.DISPLAYIMAGEOPTION_CIRCLE == null)
            ImageUtil.DISPLAYIMAGEOPTION_CIRCLE = new DisplayImageOptions.Builder().showStubImage(R.drawable.profile_photo_derault_circle)
                    .showImageForEmptyUri(R.drawable.profile_photo_derault_circle).showImageOnFail(R.drawable.profile_photo_derault_circle).cacheInMemory().cacheOnDisc()
                    .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).displayer(new RoundedBitmapDisplayer(200)).build();
        return ImageUtil.DISPLAYIMAGEOPTION_CIRCLE;
    }
    public static DisplayImageOptions getDisplayImageOptionRectangle() {

        if (ImageUtil.DISPLAYIMAGEOPTION_RETANGLE == null)
            ImageUtil.DISPLAYIMAGEOPTION_RETANGLE = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_profile)
                    .showImageForEmptyUri(R.drawable.default_profile).showImageOnFail(R.drawable.default_profile).cacheInMemory().cacheOnDisc()
                    .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).displayer(new SimpleBitmapDisplayer()).build();

        return ImageUtil.DISPLAYIMAGEOPTION_RETANGLE;
    }

}
