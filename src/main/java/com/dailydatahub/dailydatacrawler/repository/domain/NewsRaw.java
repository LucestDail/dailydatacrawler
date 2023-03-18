package com.dailydatahub.dailydatacrawler.repository.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.dailydatahub.dailydatacrawler.util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@ToString
public class NewsRaw extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "text", nullable = false)
	private String title;

	@Column(columnDefinition = "text", nullable = false,length=10000)
	private String contents;

	@Column(columnDefinition = "text", nullable = false)
	private String contentsRaw;

	@Column(columnDefinition = "text", nullable = false)
	private String newsWriter;

	@Column(columnDefinition = "text", nullable = false)
	private LocalDateTime newsDt;

	@Builder
	public NewsRaw(Long id, String title, String contents, String contentsRaw, String newsWriter, LocalDateTime newsDt) {
		this.id = id;
		this.title = title;
		this.contents = contents;
		this.contentsRaw = contentsRaw;
		this.newsWriter = newsWriter;
		this.newsDt = newsDt;
	}
}