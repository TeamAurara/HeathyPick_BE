package com.soongsil.eolala.record.persistence;

import com.soongsil.eolala.record.domain.Records;
import com.soongsil.eolala.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long> {

    @Query("""
    SELECT r
    FROM Records r
    LEFT JOIN FETCH r.food f
    LEFT JOIN FETCH r.customFood cf
    WHERE r.user = :user
    AND function('date', r.createdDate) = :createdDate
    """)
    List<Records> findByUserAndCreatedDate(User user, LocalDate createdDate);
}
