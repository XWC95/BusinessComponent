package me.businesscomponent.utils;

import com.from.business.http.HttpBusiness;

/**
 * @author Vea
 * @version 1.0.0
 * @since 2019-04-26
 */
public class ScreenUtils {
    private static final float CONVERT_VALUE = 0.5f;

    /**
     * Dip to px
     *
     * @param dipValue dip 值
     * @return 返回转换后的单位
     */
    public static int dip2px(float dipValue) {
        final float scale = HttpBusiness.getHttpComponent().application().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + CONVERT_VALUE);
    }
}
