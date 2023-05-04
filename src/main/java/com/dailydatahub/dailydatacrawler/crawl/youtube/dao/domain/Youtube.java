package com.dailydatahub.dailydatacrawler.crawl.youtube.dao.domain;

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
public class Youtube extends BaseTimeEntity{
    

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
	public Youtube(Long id
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

	public Youtube toEntity(Youtube youtube) {
        return Youtube.builder()
                ._id(youtube.get_id().isBlank() ? this._id : youtube.get_id())
                .snsId(youtube.getSnsId().isBlank() ? this.snsId : youtube.getSnsId())
                .url(youtube.getUrl().isBlank() ? this.url : youtube.getUrl())
                .category(youtube.getCategory().isBlank() ? this.category : youtube.getCategory())
                .press(youtube.getPress().isBlank() ? this.press : youtube.getPress())
                .title(youtube.getTitle().isBlank() ? this.title : youtube.getTitle())
                .author(youtube.getAuthor().isBlank() ? this.author : youtube.getAuthor())
                .content(youtube.getContent().isBlank() ? this.content : youtube.getContent())
                .status(youtube.getStatus().isBlank() ? this.status : youtube.getStatus())
                .regDate(youtube.getRegDate() == null ? this.regDate : youtube.getRegDate())
				.build();
    }

	public Youtube toEntity() {
        return Youtube.builder()
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

    public Youtube toEntity(JSONObject youtubeJsonObject) {
        Youtube youtube = new Youtube();
        youtube.set_id(youtubeJsonObject.containsKey("_id") ? youtubeJsonObject.get("_id").toString() : "");
        youtube.setSnsId(youtubeJsonObject.containsKey("snsId") ? youtubeJsonObject.get("snsId").toString() : "");
        youtube.setUrl(youtubeJsonObject.containsKey("url") ? youtubeJsonObject.get("url").toString() : "");
        youtube.setCategory(youtubeJsonObject.containsKey("category") ? youtubeJsonObject.get("category").toString() : "");
        youtube.setPress(youtubeJsonObject.containsKey("press") ? youtubeJsonObject.get("press").toString() : "");
        youtube.setTitle(youtubeJsonObject.containsKey("title") ? youtubeJsonObject.get("title").toString() : "");
        youtube.setAuthor(youtubeJsonObject.containsKey("author") ? youtubeJsonObject.get("author").toString() : "");
        youtube.setContent(youtubeJsonObject.containsKey("content") ? youtubeJsonObject.get("content").toString() : "");
        youtube.setStatus(youtubeJsonObject.containsKey("status") ? youtubeJsonObject.get("status").toString() : "");
        youtube.setRegDate(youtubeJsonObject.containsKey("regDate") ? ZonedDateTime.parse(youtubeJsonObject.get("regDate").toString()).toLocalDateTime() : null);
        return new Youtube().toEntity(youtube);
    }

    public JSONObject toJson(){
        JSONObject youtubeJsonObject = new JSONObject();
        youtubeJsonObject.put("id", this.id);
        youtubeJsonObject.put("_id", this._id);
        youtubeJsonObject.put("snsId", this.snsId);
        youtubeJsonObject.put("url", this.url);
        youtubeJsonObject.put("category", this.category);
        youtubeJsonObject.put("press", this.press);
        youtubeJsonObject.put("title", this.title);
        youtubeJsonObject.put("author", this.author);
        youtubeJsonObject.put("content", this.content);
        youtubeJsonObject.put("status", this.status);
        youtubeJsonObject.put("regDate", this.regDate);
        return youtubeJsonObject;
    }
}
