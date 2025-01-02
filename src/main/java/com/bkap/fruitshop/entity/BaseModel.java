package com.bkap.fruitshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseModel {

    @Column(name = "created_by", length = 50, updatable = false)
    @JsonIgnore
    protected String createdBy;

    @Column(name = "created_at")
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    protected Date createdAt;

    @Column(name = "updated_by", length = 50)
    @JsonIgnore
    protected String updatedBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date updatedAt;
}
