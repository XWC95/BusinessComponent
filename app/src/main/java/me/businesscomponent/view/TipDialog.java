package me.businesscomponent.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.businesscomponent.R;
import me.businesscomponent.utils.ScreenUtils;

/**
 * 一些基本样式的dialog
 *
 * @author ym.li
 * @version 1.2.0
 * @since 2018/7/28/028
 */
public class TipDialog extends Dialog {
    private static final int LOADING_SIZE = 30;
    private static final float TIP_WORD_TOP_MARGIN = 12;
    private static final float TIP_WORD_TEXT_SIZE = 14;
    private static final int DIALOG_MIN_SIZE = 90;

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public TipDialog(Context context) {
        this(context, R.style.TipDialog);
    }

    /**
     * 构造器
     *
     * @param context    上下文
     * @param themeResId 主題資源
     */
    public TipDialog(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }

    /**
     * 生成默认的 {@link TipDialog}
     * <p>
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 {@link IconType}
     * </p>
     *
     * @see CustomBuilder
     */
    public static class Builder {
        /**
         * 不显示任何icon
         */
        public static final int ICON_TYPE_NOTHING = 0;
        /**
         * 显示 Loading 图标
         */
        public static final int ICON_TYPE_LOADING = 1;
        /**
         * 显示成功图标
         */
        public static final int ICON_TYPE_SUCCESS = 2;
        /**
         * 显示失败图标
         */
        public static final int ICON_TYPE_FAIL = 3;
        /**
         * 显示信息图标
         */
        public static final int ICON_TYPE_INFO = 4;

        /**
         * 限定类型
         */
        @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }

        private @IconType
        int mCurrentIconType = ICON_TYPE_NOTHING;

        private Context mContext;

        private CharSequence mTipWord;

        /**
         * 构造器
         *
         * @param context 上下文
         */
        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置 icon 显示的内容
         *
         * @param iconType icon类型
         * @return 当前类
         */
        public Builder setIconType(@IconType int iconType) {
            mCurrentIconType = iconType;
            return this;
        }

        /**
         * 设置显示的文案
         *
         * @param tipWord 提示文案
         * @return 当前类
         */
        public Builder setTipWord(CharSequence tipWord) {
            mTipWord = tipWord;
            return this;
        }

        /**
         * 创建TipDialog
         *
         * @return 返回TipDialog
         */
        public TipDialog create() {
            return create(true);
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @param cancelable 按系统返回键是否可以取消
         * @return 创建的 Dialog
         */
        public TipDialog create(boolean cancelable) {
            TipDialog dialog = new TipDialog(mContext);
            dialog.setCancelable(cancelable);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentWrap = dialog.findViewById(R.id.contentWrap);
            if (mCurrentIconType == ICON_TYPE_LOADING) {
                LoadingView loadingView = new LoadingView(mContext);
                loadingView.setColor(Color.WHITE);
                loadingView.setSize(ScreenUtils.dip2px(LOADING_SIZE));
                LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                loadingView.setLayoutParams(loadingViewLP);
                contentWrap.addView(loadingView);
            } else if (mCurrentIconType == ICON_TYPE_SUCCESS || mCurrentIconType == ICON_TYPE_FAIL || mCurrentIconType == ICON_TYPE_INFO) {
                ImageView imageView = new ImageView(mContext);
                LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(imageViewLP);
                if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_notify_done));
                } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_notify_error));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.icon_notify_info));
                }
                contentWrap.addView(imageView);
            }
            if (mTipWord != null && mTipWord.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (mCurrentIconType != ICON_TYPE_NOTHING) {
                    tipViewLP.topMargin = ScreenUtils.dip2px(TIP_WORD_TOP_MARGIN);
                }
                tipView.setLayoutParams(tipViewLP);
                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TIP_WORD_TEXT_SIZE);
                tipView.setText(mTipWord);
                contentWrap.setMinimumWidth(ScreenUtils.dip2px(DIALOG_MIN_SIZE));
                contentWrap.setMinimumHeight(ScreenUtils.dip2px(DIALOG_MIN_SIZE));
                contentWrap.addView(tipView);
            } else {
                contentWrap.setMinimumWidth(0);
                contentWrap.setMinimumHeight(0);
            }
            return dialog;
        }
    }

    /**
     * 传入自定义的布局并使用这个布局生成 TipDialog
     */
    public static class CustomBuilder {
        private Context mContext;
        private int mContentLayoutId;

        /**
         * 构造器
         *
         * @param context 上下文
         */
        public CustomBuilder(Context context) {
            mContext = context;
        }

        /**
         * 自定义布局id
         *
         * @param layoutId 布局文件id
         * @return 返回当前类
         */
        public CustomBuilder setContent(@LayoutRes int layoutId) {
            mContentLayoutId = layoutId;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public TipDialog create() {
            TipDialog dialog = new TipDialog(mContext);
            dialog.setContentView(R.layout.tip_dialog_layout);
            ViewGroup contentWrap = dialog.findViewById(R.id.contentWrap);
            LayoutInflater.from(mContext).inflate(mContentLayoutId, contentWrap, true);
            return dialog;
        }
    }
}
