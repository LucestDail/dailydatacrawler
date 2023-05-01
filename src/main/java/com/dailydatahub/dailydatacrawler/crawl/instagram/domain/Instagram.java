package com.dailydatahub.dailydatacrawler.crawl.instagram.domain;

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
public class Instagram extends BaseTimeEntity{

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
	public Instagram(Long id
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

	public Instagram toEntity(Instagram instagram) {
        return Instagram.builder()
                ._id(instagram.get_id().isBlank() ? this._id : instagram.get_id())
                .snsId(instagram.getSnsId().isBlank() ? this.snsId : instagram.getSnsId())
                .url(instagram.getUrl().isBlank() ? this.url : instagram.getUrl())
                .category(instagram.getCategory().isBlank() ? this.category : instagram.getCategory())
                .press(instagram.getPress().isBlank() ? this.press : instagram.getPress())
                .title(instagram.getTitle().isBlank() ? this.title : instagram.getTitle())
                .author(instagram.getAuthor().isBlank() ? this.author : instagram.getAuthor())
                .content(instagram.getContent().isBlank() ? this.content : instagram.getContent())
                .status(instagram.getStatus().isBlank() ? this.status : instagram.getStatus())
                .regDate(instagram.getRegDate() == null ? this.regDate : instagram.getRegDate())
				.build();
    }

	public Instagram toEntity() {
        return Instagram.builder()
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

    public Instagram toEntity(JSONObject instagramJsonObject) {
        Instagram instagram = new Instagram();
        instagram.set_id(instagramJsonObject.containsKey("_id") ? instagramJsonObject.get("_id").toString() : "");
        instagram.setSnsId(instagramJsonObject.containsKey("snsId") ? instagramJsonObject.get("snsId").toString() : "");
        instagram.setUrl(instagramJsonObject.containsKey("url") ? instagramJsonObject.get("url").toString() : "");
        instagram.setCategory(instagramJsonObject.containsKey("category") ? instagramJsonObject.get("category").toString() : "");
        instagram.setPress(instagramJsonObject.containsKey("press") ? instagramJsonObject.get("press").toString() : "");
        instagram.setTitle(instagramJsonObject.containsKey("title") ? instagramJsonObject.get("title").toString() : "");
        instagram.setAuthor(instagramJsonObject.containsKey("author") ? instagramJsonObject.get("author").toString() : "");
        instagram.setContent(instagramJsonObject.containsKey("content") ? instagramJsonObject.get("content").toString() : "");
        instagram.setStatus(instagramJsonObject.containsKey("status") ? instagramJsonObject.get("status").toString() : "");
        instagram.setRegDate(instagramJsonObject.containsKey("regDate") ? ZonedDateTime.parse(instagramJsonObject.get("regDate").toString()).toLocalDateTime() : null);
        return new Instagram().toEntity(instagram);
    }

    public JSONObject toJson(){
        JSONObject instagramJsonObject = new JSONObject();
        instagramJsonObject.put("id", this.id);
        instagramJsonObject.put("_id", this._id);
        instagramJsonObject.put("snsId", this.snsId);
        instagramJsonObject.put("url", this.url);
        instagramJsonObject.put("category", this.category);
        instagramJsonObject.put("press", this.press);
        instagramJsonObject.put("title", this.title);
        instagramJsonObject.put("author", this.author);
        instagramJsonObject.put("content", this.content);
        instagramJsonObject.put("status", this.status);
        instagramJsonObject.put("regDate", this.regDate);
        return instagramJsonObject;
    }
}
