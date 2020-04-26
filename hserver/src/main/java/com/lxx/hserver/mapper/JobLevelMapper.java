package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.JobLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobLevelMapper {
    List<JobLevel> getAllJobLevel();
    List<JobLevel> getAllJobLevel2();

    int getJobLevelByName(String name);

    int addJobLevel(@Param("joblevel") JobLevel jobLevel);

    int updateJobLevel(@Param("joblevel")JobLevel jobLevel);

    int deleteByIdArry(@Param("ids") String[] ids);
}
