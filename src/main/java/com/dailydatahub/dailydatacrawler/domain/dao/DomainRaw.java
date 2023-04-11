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
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
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

	public DomainRaw toEntity(DomainRaw domainRaw) {
        return DomainRaw.builder()
				.domainCode(domainRaw.getDomainCode().isBlank() ? this.domainCode : domainRaw.getDomainCode())
				.title(domainRaw.getTitle().isBlank() ? this.title : domainRaw.getTitle())
                .contents(domainRaw.getContents().isBlank() ? this.contents : domainRaw.getContents())
                .contentsRaw(domainRaw.getContentsRaw().isBlank() ? this.contentsRaw : domainRaw.getContentsRaw())
				.contentsWriter(domainRaw.getContentsWriter().isBlank() ? this.contentsWriter : domainRaw.getContentsWriter())
                .contentsDt(domainRaw.getContentsDt() == null ? this.contentsDt : domainRaw.getContentsDt())
				.build();
    }

	public DomainRaw toEntity() {
        return DomainRaw.builder()
				.domainCode(this.domainCode)
				.title(this.title)
                .contents(this.contents)
                .contentsRaw(this.contentsRaw)
				.contentsWriter(this.contentsWriter)
                .contentsDt(this.contentsDt)
				.build();
    }
}