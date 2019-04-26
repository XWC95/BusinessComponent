package me.businesscomponent.utils;

import android.app.Activity;
import android.content.Context;
import android.icu.util.VersionInfo;
import android.support.v4.app.FragmentManager;

import me.businesscomponent.view.TipDialog;

/**
 * 弹窗管理类
 *
 * @author : ym.li
 * @version : v2.6.0
 * @since : 2018/9/3/003
 */
public class PopUtils {
    private PopUtils() {

    }

    public static TipDialog getTipDialog(Context context, boolean cancelable) {
        return new TipDialog.Builder(context)
            .setIconType(TipDialog.Builder.ICON_TYPE_LOADING)
            .create(cancelable);
    }

    /**
     * 获取菊花形状Dialog
     *
     * @param context 上下文
     * @return 返回TipDialog
     */
    public static TipDialog getTipDialog(Context context) {
        return getTipDialog(context, true);
    }

    /**
     * 获取带文字提示的菊花形状Dialog
     *
     * @param context 上下文
     * @param word    提示文字
     * @return 返回TipDialog
     */
    public static TipDialog getWordTipDialog(Context context, CharSequence word) {
        return new TipDialog
            .Builder(context)
            .setIconType(TipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(word)
            .create();
    }

}
