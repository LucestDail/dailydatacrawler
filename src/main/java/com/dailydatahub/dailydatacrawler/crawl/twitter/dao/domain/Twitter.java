package com.dailydatahub.dailydatacrawler.crawl.twitter.dao.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.json.simple.JSONObject;

import com.dailydatahub.dailydatacrawler.util.BaseTimeEntity;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Twitter extends BaseTimeEntity{
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
	private String _id;

    @Column(columnDefinition = "text", nullable = false)
	private String snsId;

    @Column(columnDefinition = "text", nullable = false)
	private String url;

	@Column(columnDefinition = "text", nullable = false)
	private String category;

	@Column(columnDefinition = "text", nullable = false)
	private String press;

	@Column(columnDefinition = "text", nullable = false)
	private String title;

	@Column(columnDefinition = "text", nullable = false)
	private String author;

    @Column(columnDefinition = "text", nullable = false)
	private String content;

    @Column(columnDefinition = "text", nullable = false)
	private String status;

	@Column(columnDefinition = "text", nullable = false)
	private LocalDateTime regDate;

	@Builder
	public Twitter(Long id
                , String _id
                , String snsId
                , String url
                , String category
                , String press
                , String title
                , String author
                , String content
                , String status
                , LocalDateTime regDate) {
		this.id = id;
        this._id = _id;
        this.snsId = snsId;
        this.url = url;
        this.category = category;
        this.press = press;
        this.title = title;
        this.author = author;
        this.content = content;
        this.status = status;
        this.regDate = regDate;
	}

	public Twitter toEntity(Twitter twitter) {
        return Twitter.builder()
                ._id(twitter.get_id().isBlank() ? this._id : twitter.get_id())
                .snsId(twitter.getSnsId().isBlank() ? this.snsId : twitter.getSnsId())
                .url(twitter.getUrl().isBlank() ? this.url : twitter.getUrl())
                .category(twitter.getCategory().isBlank() ? this.category : twitter.getCategory())
                .press(twitter.getPress().isBlank() ? this.press : twitter.getPress())
                .title(twitter.getTitle().isBlank() ? this.title : twitter.getTitle())
                .author(twitter.getAuthor().isBlank() ? this.author : twitter.getAuthor())
                .content(twitter.getContent().isBlank() ? this.content : twitter.getContent())
                .status(twitter.getStatus().isBlank() ? this.status : twitter.getStatus())
                .regDate(twitter.getRegDate() == null ? this.regDate : twitter.getRegDate())
				.build();
    }

	public Twitter toEntity() {
        return Twitter.builder()
                ._id(this._id)
                .snsId(this.snsId)
                .url(this.url)
                .category(this.category)
                .press(this.press)
                .title(this.title)
                .author(this.author)
                .content(this.content)
                .status(this.status)
                .regDate(this.regDate)
                .build();
    }

    public Twitter toEntity(JSONObject twitterJsonObject) {
        Twitter twitter = new Twitter();
        twitter.set_id(twitterJsonObject.containsKey("_id") ? twitterJsonObject.get("_id").toString() : "");
        twitter.setSnsId(twitterJsonObject.containsKey("snsId") ? twitterJsonObject.get("snsId").toString() : "");
        twitter.setUrl(twitterJsonObject.containsKey("url") ? twitterJsonObject.get("url").toString() : "");
        twitter.setCategory(twitterJsonObject.containsKey("category") ? twitterJsonObject.get("category").toString() : "");
        twitter.setPress(twitterJsonObject.containsKey("press") ? twitterJsonObject.get("press").toString() : "");
        twitter.setTitle(twitterJsonObject.containsKey("title") ? twitterJsonObject.get("title").toString() : "");
        twitter.setAuthor(twitterJsonObject.containsKey("author") ? twitterJsonObject.get("author").toString() : "");
        twitter.setContent(twitterJsonObject.containsKey("content") ? twitterJsonObject.get("content").toString() : "");
        twitter.setStatus(twitterJsonObject.containsKey("status") ? twitterJsonObject.get("status").toString() : "");
        twitter.setRegDate(twitterJsonObject.containsKey("regDate") ? ZonedDateTime.parse(twitterJsonObject.get("regDate").toString()).toLocalDateTime() : null);
        return new Twitter().toEntity(twitter);
    }

    public JSONObject toJson(){
        JSONObject twitterJsonObject = new JSONObject();
        twitterJsonObject.put("id", this.id);
        twitterJsonObject.put("_id", this._id);
        twitterJsonObject.put("snsId", this.snsId);
        twitterJsonObject.put("url", this.url);
        twitterJsonObject.put("category", this.category);
        twitterJsonObject.put("press", this.press);
        twitterJsonObject.put("title", this.title);
        twitterJsonObject.put("author", this.author);
        twitterJsonObject.put("content", this.content);
        twitterJsonObject.put("status", this.status);
        twitterJsonObject.put("regDate", this.regDate);
        return twitterJsonObject;
    }
}
