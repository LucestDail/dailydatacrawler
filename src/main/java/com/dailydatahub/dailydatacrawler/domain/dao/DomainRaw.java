package com.dailydatahub.dailydatacrawler.domain.dao;

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
public class DomainRaw extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "text", nullable = false)
	private String domainCode;

	@Column(columnDefinition = "text", nullable = false)
	private String title;

	@Column(columnDefinition = "text", nullable = false,length=10000)
	private String contents;

	@Column(columnDefinition = "text", nullable = false)
	private String contentsRaw;

	@Column(columnDefinition = "text", nullable = false)
	private String contentsWriter;

	@Column(columnDefinition = "text", nullable = false)
	private LocalDateTime contentsDt;

	@Builder
	public DomainRaw(Long id, String domainCode, String title, String contents, String contentsRaw, String contentsWriter, LocalDateTime contentsDt) {
		this.id = id;
		this.domainCode = domainCode;
		this.title = title;
		this.contents = contents;
		this.contentsRaw = contentsRaw;
		this.contentsWriter = contentsWriter;
		this.contentsDt = contentsDt;
	}
}