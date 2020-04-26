package com.lxx.hserver.service;

import com.lxx.hserver.entity.JobLevel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class JobLevelServiceTest {
    @Autowired
    JobLevelService jobLevelService;
    @Test
    void getAllJobLevels() {
        List<JobLevel> allJobLevels = jobLevelService.getAllJobLevels();
        for (JobLevel allJobLevel : allJobLevels) {
            System.out.println(allJobLevel.toString());
        }
    }
    @Test
    void add(){
        JobLevel jobLevel = new JobLevel();
        jobLevel.setCreateDate(new Date());
        jobLevel.setName("1");
        jobLevel.setTitleLevel("222");
        jobLevelService.addJobLevel(jobLevel);

    }
}