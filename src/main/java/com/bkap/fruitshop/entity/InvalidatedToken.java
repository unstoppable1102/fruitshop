package com.bkap.fruitshop.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InvalidatedToken {

    @Id
    private String id;

    private Date expiryTime;
}
