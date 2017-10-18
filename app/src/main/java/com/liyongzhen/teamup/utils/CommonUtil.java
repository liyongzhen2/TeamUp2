package com.liyongzhen.teamup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 8/21/2017.
 */

public class CommonUtil {

    public static enum DayGroup {
        TODAY, DAY, WEEK, MONTH, THREEMONTH, HALFYEAR, YEAR
    }

    public static float formatFloat(String val) {
        float floatVal = Float.parseFloat(val);
        if (floatVal < 0.01 && floatVal > -0.01)
            return 0f;
        String res = new DecimalFormat("#.##").format(floatVal);
        return Float.parseFloat(res);
    }

    public static long getSecondsFrom(DayGroup type) {
        long sec = 0;
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case TODAY:
                break;
            case DAY:
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, -1);
                break;
            case THREEMONTH:
                calendar.add(Calendar.MONTH, -3);
                break;
            case HALFYEAR:
                calendar.add(Calendar.MONTH, -6);
                break;
            case YEAR:
                calendar.add(Calendar.YEAR, -1);
                break;
        }
        sec = calendar.getTimeInMillis() / 1000;
        return sec;
    }

    public static long getUTCSecond() {
        long t1 = System.currentTimeMillis();
        return t1 / 1000;
    }

    public static String getDifferenceTime(long timeStamp) {
        String res;
        long currentSec = System.currentTimeMillis();
        long dif = currentSec - timeStamp * 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(dif);

        int year = calendar.get(Calendar.YEAR) - 1970;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        if (year > 0)
            res = year + "Y";
        else if (month > 0)
            res = month + "M";
        else if (day > 0)
            res = day + "D";
        else if (hour > 0)
            res = hour + "h";
        else if (min > 0)
            res = min + "m";
        else if (sec > 0)
            res = sec + "s";
        else
            res = "Just now";

        return res;
    }

    public static File getFileRezie(URI uri) {
        try {
            File imgFileOrig = new File(uri);
            if (imgFileOrig == null)
                return null;

            if (!imgFileOrig.exists())
                return null;

            int oneKb = 1 * 1024;
            int limitSize = 100 * oneKb;

            if (imgFileOrig.length() <= limitSize)
                return imgFileOrig;

            ExifInterface exif = new ExifInterface(
                    imgFileOrig.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            File dirTemp = new File(Constants.DIR_LOCAL_IMAGE_TEMP);
            if (!dirTemp.exists())
                dirTemp.mkdirs();

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(imgFileOrig);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > Constants.IMAGE_MAX_SIZE || o.outWidth > Constants.IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(Constants.IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            // we save the file, at least until we have made use of it
            File f = new File(Constants.DIR_LOCAL_IMAGE_TEMP + File.separator + imgFileOrig.getName());
            f.createNewFile();

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(imgFileOrig);
            Bitmap b = BitmapFactory.decodeStream(fis, null, o2);
            if (orientation > 0)
                b = rotateBitmap(b, orientation);
            fis.close();

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            // write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(outStream.toByteArray());

            // remember close de FileOutput
            fo.close();

            b.recycle();

            return f;

        } catch (Exception e) {

        }

        return null;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public static int getDateOffest(long timestamp) {
        int offset = (int)(System.currentTimeMillis() / 1000 - timestamp) / 3600 / 24;
        Log.e("getDateOffset", "==================>" + String.valueOf(timestamp));
        Log.e("getDateOffset", "==================>" + String.valueOf(offset));
        return offset;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
