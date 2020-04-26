package com.lxx.hserver.service;

import com.lxx.hserver.entity.JobLevel;
import com.lxx.hserver.mapper.JobLevelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class JobLevelService {
    @Autowired
    JobLevelMapper jobLevelMapper;

    public List<JobLevel> getAllJobLevels() {
        return jobLevelMapper.getAllJobLevel();
    }

    public List<JobLevel> getAllJobLevelWithBasic() {
        return jobLevelMapper.getAllJobLevel2();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public int addJobLevel(JobLevel jobLevel) {
        if (jobLevelMapper.getJobLevelByName(jobLevel.getName())==0) {
            jobLevel.setCreateDate(new Date());
            return  jobLevelMapper.addJobLevel(jobLevel);
        }
        throw new RuntimeException("该职称已存在！");
    }

    public int updateJobLevel(JobLevel jobLevel) {
        if (jobLevelMapper.getJobLevelByName(jobLevel.getName())<=1) {
            int i = jobLevelMapper.updateJobLevel(jobLevel);
            return i;
        }
        return 0;
    }

    public int jobLevelEnabled(JobLevel jobLevel) {
        jobLevel.setEnabled(!jobLevel.getEnabled());
        return jobLevelMapper.updateJobLevel(jobLevel);
    }

    public boolean deleteJobLevel(String ids) {
        String []id=ids.split(",");
        return jobLevelMapper.deleteByIdArry(id)==id.length;
    }
}
