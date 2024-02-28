package com.heeju.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) // Add @EntityListeners annotation to apply auditing.
@MappedSuperclass   // This annotation is used when common mapping information is needed and only provides mapping
                    // information to child classes that inherit the parent class.
@Data
public abstract class BaseTimeEntity {

    @CreatedDate               // Automatically saves the time when the entity is created and saved.
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate           // Automatically saves the time when changing the value of an entity.
    private LocalDateTime updateTime;

}
