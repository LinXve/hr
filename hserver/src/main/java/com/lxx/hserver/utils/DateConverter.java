package com.lxx.hserver.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * 日期传递过滤
 */
public class DateConverter implements Converter<String,Date> {
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Date convert(String s) {
        if (StringUtils.isEmpty(s)){
            return null;
        }
        try {
            return format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
