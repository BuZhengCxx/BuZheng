package com.bu.zheng.log;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.bu.zheng.R;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by BuZheng on 2017/5/11.
 */
public class LoggerTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void d() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Logger.d("Topic",
                "TopicDetailActivity",
                String.format(appContext.getString(R.string.log_test_str1), "topic data return"));
    }

}