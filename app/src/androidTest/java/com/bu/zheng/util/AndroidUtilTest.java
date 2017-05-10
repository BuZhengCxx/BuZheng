package com.bu.zheng.util;

import org.junit.Test;

/**
 * Created by BuZheng on 2017/5/10.
 */
public class AndroidUtilTest {
    @Test
    public void isHuaweiPhone() throws Exception {
        boolean isHuaweiPhone = AndroidUtil.isHuaweiPhone();
        System.out.println("isHuaweiPhone:" + isHuaweiPhone);
    }

}