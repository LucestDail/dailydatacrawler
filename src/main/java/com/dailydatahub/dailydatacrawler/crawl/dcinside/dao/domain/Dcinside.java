package com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.domain;

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
public class Dcinside extends BaseTimeEntity{

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
	public Dcinside(Long id
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

	public Dcinside toEntity(Dcinside Dcinside) {
        return Dcinside.builder()
                ._id(Dcinside.get_id().isBlank() ? this._id : Dcinside.get_id())
                .snsId(Dcinside.getSnsId().isBlank() ? this.snsId : Dcinside.getSnsId())
                .url(Dcinside.getUrl().isBlank() ? this.url : Dcinside.getUrl())
                .category(Dcinside.getCategory().isBlank() ? this.category : Dcinside.getCategory())
                .press(Dcinside.getPress().isBlank() ? this.press : Dcinside.getPress())
                .title(Dcinside.getTitle().isBlank() ? this.title : Dcinside.getTitle())
                .author(Dcinside.getAuthor().isBlank() ? this.author : Dcinside.getAuthor())
                .content(Dcinside.getContent().isBlank() ? this.content : Dcinside.getContent())
                .status(Dcinside.getStatus().isBlank() ? this.status : Dcinside.getStatus())
                .regDate(Dcinside.getRegDate() == null ? this.regDate : Dcinside.getRegDate())
				.build();
    }

	public Dcinside toEntity() {
        return Dcinside.builder()
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

    public Dcinside toEntity(JSONObject DcinsideJsonObject) {
        Dcinside Dcinside = new Dcinside();
        Dcinside.set_id(DcinsideJsonObject.containsKey("_id") ? DcinsideJsonObject.get("_id").toString() : "");
        Dcinside.setSnsId(DcinsideJsonObject.containsKey("snsId") ? DcinsideJsonObject.get("snsId").toString() : "");
        Dcinside.setUrl(DcinsideJsonObject.containsKey("url") ? DcinsideJsonObject.get("url").toString() : "");
        Dcinside.setCategory(DcinsideJsonObject.containsKey("category") ? DcinsideJsonObject.get("category").toString() : "");
        Dcinside.setPress(DcinsideJsonObject.containsKey("press") ? DcinsideJsonObject.get("press").toString() : "");
        Dcinside.setTitle(DcinsideJsonObject.containsKey("title") ? DcinsideJsonObject.get("title").toString() : "");
        Dcinside.setAuthor(DcinsideJsonObject.containsKey("author") ? DcinsideJsonObject.get("author").toString() : "");
        Dcinside.setContent(DcinsideJsonObject.containsKey("content") ? DcinsideJsonObject.get("content").toString() : "");
        Dcinside.setStatus(DcinsideJsonObject.containsKey("status") ? DcinsideJsonObject.get("status").toString() : "");
        Dcinside.setRegDate(DcinsideJsonObject.containsKey("regDate") ? ZonedDateTime.parse(DcinsideJsonObject.get("regDate").toString()).toLocalDateTime() : null);
        return new Dcinside().toEntity(Dcinside);
    }

    public JSONObject toJson(){
        JSONObject DcinsideJsonObject = new JSONObject();
        DcinsideJsonObject.put("id", this.id);
        DcinsideJsonObject.put("_id", this._id);
        DcinsideJsonObject.put("snsId", this.snsId);
        DcinsideJsonObject.put("url", this.url);
        DcinsideJsonObject.put("category", this.category);
        DcinsideJsonObject.put("press", this.press);
        DcinsideJsonObject.put("title", this.title);
        DcinsideJsonObject.put("author", this.author);
        DcinsideJsonObject.put("content", this.content);
        DcinsideJsonObject.put("status", this.status);
        DcinsideJsonObject.put("regDate", this.regDate);
        return DcinsideJsonObject;
    }
}
