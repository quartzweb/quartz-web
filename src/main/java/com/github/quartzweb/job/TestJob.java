package com.github.quartzweb.job;

import com.github.quartzweb.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author quxiucheng [quxiuchengdev@gmail.com]
 */
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(DateUtils.formart(new Date()) + ":job");
    }
}
