package com.bu.zheng.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenxiaoxiong on 16/3/27.
 */
public class SkinUtils {

    public static Resources getResFromSkinApk(Context context, String skinApkPath) {

        Resources defaultRes = context.getResources();
        Resources skinApkRes = null;

        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", new Class<?>[]{String.class});
            if (method != null) {
                method.setAccessible(true);
                method.invoke(assetManager, new Object[]{skinApkPath});
            }
            skinApkRes = new Resources(assetManager, defaultRes.getDisplayMetrics(), defaultRes.getConfiguration());

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return skinApkRes;
    }

    public static void tintColor(ImageView imageView, Drawable drawable, int tint) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(wrappedDrawable, tint);
        imageView.setImageDrawable(wrappedDrawable);
    }

    public static void tintColorStateList(ImageView imageView, Drawable drawable, ColorStateList tint) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(wrappedDrawable, tint);
        imageView.setImageDrawable(wrappedDrawable);
    }

    public static String getSkinFilePath(Skin skin) {
        StringBuilder sb = new StringBuilder(SkinConfig.SKIN_RES_ROOT_PATH);
        sb.append(File.separator).append(skin.id).append(".apk");
        return sb.toString();
    }

    public static boolean isSkinDownloaded(Skin skin) {
        String filePath = getSkinFilePath(skin);
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isSkinDownloaded(String id) {
        return isSkinDownloaded(new Skin(id));
    }

    public static boolean skinUsable(int skinVersionCode) {
        return skinVersionCode == SkinConfig.SKIN_VERSION_CODE;
    }
}
