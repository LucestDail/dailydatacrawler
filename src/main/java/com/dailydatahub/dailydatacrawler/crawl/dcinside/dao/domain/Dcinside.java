package com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.domain;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

	@Column(columnDefinition = "text", nullable = true)
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

	public Dcinside toEntity(Dcinside dcinside) {
        return Dcinside.builder()
                ._id(dcinside.get_id().isBlank() ? this._id : dcinside.get_id())
                .snsId(dcinside.getSnsId().isBlank() ? this.snsId : dcinside.getSnsId())
                .url(dcinside.getUrl().isBlank() ? this.url : dcinside.getUrl())
                .category(dcinside.getCategory().isBlank() ? this.category : dcinside.getCategory())
                .press(dcinside.getPress().isBlank() ? this.press : dcinside.getPress())
                .title(dcinside.getTitle().isBlank() ? this.title : dcinside.getTitle())
                .author(dcinside.getAuthor().isBlank() ? this.author : dcinside.getAuthor())
                .content(dcinside.getContent().isBlank() ? this.content : dcinside.getContent())
                .status(dcinside.getStatus().isBlank() ? this.status : dcinside.getStatus())
                .regDate(dcinside.getRegDate() == null ? this.regDate : dcinside.getRegDate())
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

    public Dcinside toEntity(JSONObject dcinsideJsonObject) {
        Dcinside dcinside = new Dcinside();
        dcinside.set_id(dcinsideJsonObject.containsKey("_id") ? dcinsideJsonObject.get("_id").toString() : "");
        dcinside.setSnsId(dcinsideJsonObject.containsKey("snsId") ? dcinsideJsonObject.get("snsId").toString() : "");
        dcinside.setUrl(dcinsideJsonObject.containsKey("url") ? dcinsideJsonObject.get("url").toString() : "");
        dcinside.setCategory(dcinsideJsonObject.containsKey("category") ? dcinsideJsonObject.get("category").toString() : "");
        dcinside.setPress(dcinsideJsonObject.containsKey("press") ? dcinsideJsonObject.get("press").toString() : "");
        dcinside.setTitle(dcinsideJsonObject.containsKey("title") ? dcinsideJsonObject.get("title").toString() : "");
        dcinside.setAuthor(dcinsideJsonObject.containsKey("author") ? dcinsideJsonObject.get("author").toString() : "");
        dcinside.setContent(dcinsideJsonObject.containsKey("content") ? dcinsideJsonObject.get("content").toString() : "");
        dcinside.setStatus(dcinsideJsonObject.containsKey("status") ? dcinsideJsonObject.get("status").toString() : "");
        try{
            dcinside.setRegDate(dcinsideJsonObject.containsKey("regDate") ? LocalDateTime.parse(dcinsideJsonObject.get("regDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
        }catch(Exception e){
            dcinside.setRegDate(dcinsideJsonObject.containsKey("regDate") ? LocalDateTime.parse(dcinsideJsonObject.get("regDate").toString(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")) : null);
        }
        //dcinside.setRegDate(dcinsideJsonObject.containsKey("regDate") ? LocalDateTime.parse(dcinsideJsonObject.get("regDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
        return new Dcinside().toEntity(dcinside);
    }

    public JSONObject toJson(){
        JSONObject dcinsideJsonObject = new JSONObject();
        dcinsideJsonObject.put("id", this.id);
        dcinsideJsonObject.put("_id", this._id);
        dcinsideJsonObject.put("snsId", this.snsId);
        dcinsideJsonObject.put("url", this.url);
        dcinsideJsonObject.put("category", this.category);
        dcinsideJsonObject.put("press", this.press);
        dcinsideJsonObject.put("title", this.title);
        dcinsideJsonObject.put("author", this.author);
        dcinsideJsonObject.put("content", this.content);
        dcinsideJsonObject.put("status", this.status);
        dcinsideJsonObject.put("regDate", this.regDate);
        return dcinsideJsonObject;
    }
}
