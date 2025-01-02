package com.bkap.fruitshop.common.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class DBUtil {

    @PersistenceContext
    private EntityManager entityManager;

    public Date getDatabaseTimestamp() {
        Query query = entityManager.createNativeQuery("SELECT CURRENT_TIMESTAMP");
        Timestamp timestamp = (Timestamp) ((Query) query).getSingleResult(); // Lấy kết quả
        return new Date(timestamp.getTime()); // Chuyển từ Timestamp sang Date
    }

}
