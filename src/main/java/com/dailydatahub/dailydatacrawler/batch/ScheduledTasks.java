package com.dailydatahub.dailydatacrawler.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dailydatahub.dailydatacrawler.crawl.instagram.service.InstagramService;
import com.dailydatahub.dailydatacrawler.crawl.twitter.service.Twitterservice;
import com.dailydatahub.dailydatacrawler.crawl.youtube.service.YoutubeService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private InstagramService instagramService;

    @Autowired
    private Twitterservice twitterService;

    @Scheduled(cron="* * * * * *")
	public void reportCurrentTime(){
		log.info("<REPORTING> {}", dateFormat.format(new Date()));
	}

	@Scheduled(cron="0 10 * * * *")
	public void youtubeExplore(){
		log.info("youtubeExplore Execute {}", dateFormat.format(new Date()));
        try{
            youtubeService.explore();
        }catch(Exception e){
            e.printStackTrace();
        }
	}

    @Scheduled(cron="0 30 * * * *")
	public void twitterExplore(){
		log.info("twitterExplore Execute {}", dateFormat.format(new Date()));
        try{
            twitterService.explore();
        }catch(Exception e){
            e.printStackTrace();
        }
	}

    @Scheduled(cron="0 50 * * * *")
	public void instagramExplore(){
		log.info("instagramExplore Execute {}", dateFormat.format(new Date()));
        try{
            instagramService.explore();
        }catch(Exception e){
            e.printStackTrace();
        }
	}
}