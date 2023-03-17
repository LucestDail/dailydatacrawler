package com.dailydatahub.dailydatacrawler.util;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
	
	@CreatedDate
	@Column(updatable=false, nullable = false)
	private LocalDateTime createDt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updateDt;
	
	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createDt = now;
		updateDt = now;
	}
	
	@PreUpdate
	public void preUpdate() {
		updateDt = LocalDateTime.now();
	}
}