package com.bu.zheng.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BuZheng on 2017/6/29.
 */

public class Util {

    //\w只能匹配一个字符
    //
    public static void test1() {
        //String input = "ceratA";

        //Pattern pattern = Pattern.compile("\\wt\u0041");

        String input = "ceshi13233892041;14234b15458792574";
        //Pattern pattern = Pattern.compile("[\u0041-\u0097]");
        Pattern pattern = Pattern.compile("((13)|15)\\d{9}");

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

}
