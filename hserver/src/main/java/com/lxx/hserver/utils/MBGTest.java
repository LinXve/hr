package com.lxx.hserver.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//mybatis逆向工程
public class MBGTest {
    public static void main(String[] args)throws Exception {
        List<String> list=new ArrayList<>();
        boolean orverwrite= true;
        File configFile=new File("mbg.xml");
        ConfigurationParser parser=new ConfigurationParser(list);
        Configuration configuration=parser.parseConfiguration(configFile);
        DefaultShellCallback callback=new DefaultShellCallback(orverwrite);
        MyBatisGenerator generator=new MyBatisGenerator(configuration,callback,list);
        generator.generate(null);
    }
}
