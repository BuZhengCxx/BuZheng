package com.bu.zheng.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bu.zheng.skin.view.ICustomSkinView;
import com.bu.zheng.skin.view.ITintSkinView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by chenxiaoxiong on 2017/3/14.
 */

public class ViewManager {

    //View
    private static int[] mResArrayBackground;

    //ImageView
    private static int[] mResArraySrc;

    //EditText
    private static int[] mResArrayTextColorHit;

    //TextView
    private static int[] mResArrayTextColor;
    private static int[] mResArrayTextSize;
    private static int[] mResArrayDrawableLeft;
    private static int[] mResArrayDrawableRight;

    //CheckBox
    private static int[] mResArrayCBbg;

    //ListView
    private static int[] mResArrayListDivider;

    //ProgressBar
    private static int[] mResArrayProgressBar;
    private static int[] mResArrayThumb;

    static {
        mResArrayBackground = new int[]{android.R.attr.background};
        mResArraySrc = new int[]{android.R.attr.src};
        mResArrayTextColor = new int[]{android.R.attr.textColor};
        mResArrayTextSize = new int[]{android.R.attr.textSize};
        mResArrayTextColorHit = new int[]{android.R.attr.textColorHint};
        mResArrayDrawableLeft = new int[]{android.R.attr.drawableLeft};
        mResArrayDrawableRight = new int[]{android.R.attr.drawableRight};
        mResArrayCBbg = new int[]{android.R.attr.button};
        mResArrayListDivider = new int[]{android.R.attr.listDivider};
        mResArrayProgressBar = new int[]{android.R.attr.progressDrawable};
        mResArrayThumb = new int[]{android.R.attr.thumb};
    }

    private WeakHashMap<View, ViewSkinAttr> mViewAttrMap;
    private Context mContext;
    private IResources mSkinRes;
    private Resources mDefaultRes;

    public ViewManager(Context context, Resources defaultRes, IResources skinRes) {
        mContext = context;
        mSkinRes = skinRes;
        mDefaultRes = defaultRes;
        mViewAttrMap = new WeakHashMap<>();
    }

    public void changeSkin(Skin skin) {
        setSkinRes(skin);
        onSkinChange();
    }

    public IResources getSkinRes() {
        return mSkinRes;
    }

    public void setSkinRes(Skin skin) {
        if (skin == null || skin.getResources() == null) {
            mSkinRes = null;
            return;
        }
        mSkinRes = skin.getResources();
    }

    private void onSkinChange() {
        synchronized (mViewAttrMap) {
            Set<Map.Entry<View, ViewSkinAttr>> entrySet = mViewAttrMap.entrySet();
            Iterator<Map.Entry<View, ViewSkinAttr>> iterator = entrySet.iterator();

            Map.Entry<View, ViewSkinAttr> entry;
            View view;
            ViewSkinAttr value;

            while (iterator.hasNext()) {
                entry = iterator.next();
                view = entry.getKey();
                value = entry.getValue();
                if (value == null) {
                    continue;
                }
                setViewSkin(view, value);
            }
        }
    }

    public ViewSkinAttr addView(View view, AttributeSet attributeSet) {
        if (view == null || attributeSet == null) {
            return null;
        }

        ViewSkinAttr viewSkinAttr = new ViewSkinAttr();

        addCustomViewAttrs(view, viewSkinAttr, attributeSet);
        addTintAttrs(view, viewSkinAttr, attributeSet);

        int resId = getResourceId(attributeSet, mResArrayBackground);
        if (resId > 0) {
            viewSkinAttr.setBackgroundResId(resId);
        }

        if (view instanceof ImageView) {
            resId = getResourceId(attributeSet, mResArraySrc);
            if (resId > 0) {
                viewSkinAttr.setSrcResId(resId);
            }

        } else if (view instanceof EditText) {
            resId = getResourceId(attributeSet, mResArrayTextColorHit);
            if (resId > 0) {
                viewSkinAttr.setTextColorHitResId(resId);
            }

        } else if (view instanceof CheckBox) {
            resId = getResourceId(attributeSet, mResArrayCBbg);
            if (resId > 0) {
                viewSkinAttr.setCheckBoxButtonResId(resId);
            }

        } else if (view instanceof TextView) {
            resId = getResourceId(attributeSet, mResArrayTextColor);
            if (resId > 0) {
                viewSkinAttr.setTextColorResId(resId);
            }
            resId = getResourceId(attributeSet, mResArrayTextSize);
            if (resId > 0) {
                viewSkinAttr.setTextSizeResId(resId);
            }
            resId = getResourceId(attributeSet, mResArrayDrawableLeft);
            if (resId > 0) {
                viewSkinAttr.setTextDrawableLeftResId(resId);
            }
            resId = getResourceId(attributeSet, mResArrayDrawableRight);
            if (resId > 0) {
                viewSkinAttr.setTextDrawableRightResId(resId);
            }

        } else if (view instanceof ListView) {
            resId = getResourceId(attributeSet, mResArrayListDivider);
            if (resId > 0) {
                viewSkinAttr.setListDividerResId(resId);
            }

        } else if (view instanceof ProgressBar) {
            resId = getResourceId(attributeSet, mResArrayProgressBar);
            if (resId > 0) {
                viewSkinAttr.setProgressBarDrawableId(resId);
            }
            if (view instanceof SeekBar) {
                resId = getResourceId(attributeSet, mResArrayThumb);
                if (resId > 0) {
                    viewSkinAttr.setSeekBarThumbId(resId);
                }
            }
        }

        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public void setViewSkin(View view, ViewSkinAttr viewSkinAttr) {

        setCustomViewAttrs(view, viewSkinAttr);
        setTintAttrs(view, viewSkinAttr);

        int resId;
        resId = viewSkinAttr.getBackgroundResId();
        if (resId > 0) {
            setBackgroundDrawable(view, getDrawable(resId));
        }

        if (view instanceof ImageView) {
            resId = viewSkinAttr.getSrcResId();
            if (resId > 0) {
                ((ImageView) view).setImageDrawable(getDrawable(resId));
            }

        } else if (view instanceof EditText) {
            resId = viewSkinAttr.getTextColorHitResId();
            if (resId > 0) {
                ((TextView) view).setHintTextColor(getColor(resId));
            }

        } else if (view instanceof CheckBox) {
            resId = viewSkinAttr.getCheckBoxButtonResId();
            if (resId > 0) {
                ((CheckBox) view).setButtonDrawable(getDrawable(resId));
            }

        } else if (view instanceof TextView) {
            resId = viewSkinAttr.getTextColorResId();
            if (resId > 0) {
                int color = getColor(resId);
                if (color > 0) {
                    ((TextView) view).setTextColor(color);
                } else {
                    ColorStateList colors = getColorStateList(resId);
                    if (colors != null) {
                        ((TextView) view).setTextColor(colors);
                    }
                }
            }
            resId = viewSkinAttr.getTextSizeResId();
            if (resId > 0) {
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimension(resId));
            }

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            resId = viewSkinAttr.getTextDrawableLeftResId();
            if (resId > 0) {
                drawableLeft = getDrawable(resId);
            }
            resId = viewSkinAttr.getTextDrawableRightResId();
            if (resId > 0) {
                drawableRight = getDrawable(resId);
            }
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null);

        } else if (view instanceof ListView) {
            resId = viewSkinAttr.getListDividerResId();
            if (resId > 0) {
                ((ListView) view).setDivider(getDrawable(resId));
            }
        } else if (view instanceof ProgressBar) {
            resId = viewSkinAttr.getProgressBarDrawableId();
            if (resId > 0) {
                ((ProgressBar) view).setProgressDrawable(getDrawable(resId));
            }
        }
    }

    private void addCustomViewAttrs(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet) {
        if (view instanceof ICustomSkinView) {
            ((ICustomSkinView) view).addStyledAttributes(viewSkinAttr, attributeSet);
        }
    }

    private void setCustomViewAttrs(View view, ViewSkinAttr viewSkinAttr) {
        if (view instanceof ICustomSkinView) {
            ((ICustomSkinView) view).setStyledAttributes(viewSkinAttr);
        }
    }

    private void addTintAttrs(View view, ViewSkinAttr viewSkinAttr, AttributeSet attributeSet) {
        if (view instanceof ITintSkinView) {
            ((ITintSkinView) view).addTintColor(view, viewSkinAttr, attributeSet);
            ((ITintSkinView) view).addTintColorStateList(view, viewSkinAttr, attributeSet);
        }
    }

    private void setTintAttrs(View view, ViewSkinAttr viewSkinAttr) {
        if (view instanceof ITintSkinView) {
            ((ITintSkinView) view).setTintColor(viewSkinAttr);
            ((ITintSkinView) view).setTintColorStateList(viewSkinAttr);
        }
    }

    private int getResourceId(AttributeSet attrs, int[] checkAttrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, checkAttrs);
        TypedValue value = a.peekValue(0);

        int resId = -1;
        if (value != null) {
            resId = value.resourceId;
        }
        a.recycle();
        return resId;
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (view == null || drawable == null) {
            return;
        }
        int pl = view.getPaddingLeft();
        int pt = view.getPaddingTop();
        int pr = view.getPaddingRight();
        int pb = view.getPaddingBottom();
        ViewCompat.setBackground(view, drawable);
        view.setPadding(pl, pt, pr, pb);
    }

    public Drawable getDrawable(int resId) throws Resources.NotFoundException {
        Drawable drawable;
        if (mSkinRes == null) {
            drawable = mDefaultRes.getDrawable(resId);
            return drawable;
        }
        try {
            drawable = mSkinRes.getDrawable(resId);
        } catch (Resources.NotFoundException e) {
            drawable = mDefaultRes.getDrawable(resId);
        }
        return drawable == null ? mDefaultRes.getDrawable(resId) : drawable;
    }

    public int getColor(int resId) throws Resources.NotFoundException {
        int color;
        if (mSkinRes == null) {
            color = mDefaultRes.getColor(resId);
            return color;
        }
        try {
            color = mSkinRes.getColor(resId);
        } catch (Resources.NotFoundException e) {
            color = mDefaultRes.getColor(resId);
        }
        return color;
    }

    public ColorStateList getColorStateList(int resId) {
        ColorStateList list;
        if (mSkinRes == null) {
            list = mDefaultRes.getColorStateList(resId);
            return list;
        }

        try {
            list = mSkinRes.getColorStateList(resId);
        } catch (Resources.NotFoundException e) {
            list = mDefaultRes.getColorStateList(resId);
        }
        return list;
    }

    public float getDimension(int resId) throws Resources.NotFoundException {
        float dimen;
        if (mSkinRes == null) {
            dimen = mDefaultRes.getDimension(resId);
            return dimen;
        }
        try {
            dimen = mSkinRes.getDimension(resId);
        } catch (Resources.NotFoundException e) {
            dimen = mDefaultRes.getDimension(resId);
        }
        return dimen;
    }

    //----------------------------------------------------------------------------------------------
    public ViewSkinAttr addImageSrc(View view, int resId) {
        if (view == null || resId < 0) {
            return null;
        }
        ViewSkinAttr viewSkinAttr = mViewAttrMap.get(view);
        if (viewSkinAttr == null) {
            viewSkinAttr = new ViewSkinAttr();
        }

        viewSkinAttr.setSrcResId(resId);
        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public ViewSkinAttr addTextColor(View view, int resId) {
        if (view == null || resId < 0) {
            return null;
        }
        ViewSkinAttr viewSkinAttr = mViewAttrMap.get(view);
        if (viewSkinAttr == null) {
            viewSkinAttr = new ViewSkinAttr();
        }
        viewSkinAttr.setTextColorResId(resId);
        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public ViewSkinAttr addTextDrawableLeft(View view, int resId) {
        if (view == null || resId < 0) {
            return null;
        }
        ViewSkinAttr viewSkinAttr = mViewAttrMap.get(view);
        if (viewSkinAttr == null) {
            viewSkinAttr = new ViewSkinAttr();
        }
        viewSkinAttr.setTextDrawableLeftResId(resId);
        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public ViewSkinAttr addTextDrawableRight(View view, int resId) {
        if (view == null || resId < 0) {
            return null;
        }
        ViewSkinAttr viewSkinAttr = mViewAttrMap.get(view);
        if (viewSkinAttr == null) {
            viewSkinAttr = new ViewSkinAttr();
        }
        viewSkinAttr.setTextDrawableRightResId(resId);
        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public ViewSkinAttr addBgColor(View view, int resId) {
        if (view == null || resId < 0) {
            return null;
        }
        ViewSkinAttr viewSkinAttr = mViewAttrMap.get(view);
        if (viewSkinAttr == null) {
            viewSkinAttr = new ViewSkinAttr();
        }
        viewSkinAttr.setBackgroundResId(resId);
        synchronized (mViewAttrMap) {
            mViewAttrMap.put(view, viewSkinAttr);
        }
        return viewSkinAttr;
    }

    public int getParsedId(int resId) {
        if (mSkinRes == null) {
            return resId;
        }
        return ((ApkSkinRes) mSkinRes).getParsedId(resId);
    }
}
