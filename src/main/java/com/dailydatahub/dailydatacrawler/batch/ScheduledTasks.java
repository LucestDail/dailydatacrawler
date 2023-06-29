package com.dailydatahub.dailydatacrawler.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dailydatahub.dailydatacrawler.crawl.dcinside.service.DcinsideService;
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

    @Autowired
    private DcinsideService dcinsideService;
    
    @Value("${crawler.log.flag}")
    private String logFlag;

    @Value("${crawler.dcinside.flag}")
    private String dcinsideFlag;

    @Value("${crawler.twitter.flag}")
    private String twitterFlag;

    @Value("${crawler.youtube.flag}")
    private String youtubeFlag;

    @Value("${crawler.instagram.flag}")
    private String instagramFlag;

    @Scheduled(cron="* * * * * *")
	public void reportCurrentTime(){
        if(Boolean.parseBoolean(logFlag)){
            log.info("<REPORTING> {}", dateFormat.format(new Date()));
        }
	}

	@Scheduled(cron="0 0 * * * *")
	public void youtubeExplore(){
        if(Boolean.parseBoolean(logFlag)){
		    log.info("<EXECUTION> youtubeExplore Execute {}", dateFormat.format(new Date()));
         }
        try{
            if(Boolean.parseBoolean(youtubeFlag)){
                youtubeService.exploreSave();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	}

    @Scheduled(cron="0 0 * * * *")
	public void twitterExplore(){
        if(Boolean.parseBoolean(logFlag)){
		    log.info("<EXECUTION> twitterExplore Execute {}", dateFormat.format(new Date()));
         }
        try{
            if(Boolean.parseBoolean(twitterFlag)){
                twitterService.exploreSave();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	}

    @Scheduled(cron="0 0 */3 * * *")
	public void instagramExplore(){
        if(Boolean.parseBoolean(logFlag)){
            log.info("<EXECUTION> instagramExplore Execute {}", dateFormat.format(new Date()));
        }
        try{
            if(Boolean.parseBoolean(instagramFlag)){
                instagramService.exploreSave();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	}

    @Scheduled(cron="0 0 * * * *")
	public void dcinsideExplore(){
        if(Boolean.parseBoolean(logFlag)){
		    log.info("<EXECUTION> dcinsideExplore Execute {}", dateFormat.format(new Date()));
        }
        try{
            if(Boolean.parseBoolean(dcinsideFlag)){
                dcinsideService.exploreSave();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	}
}