package com.soongsil.eolala.record.persistence;

import com.soongsil.eolala.record.domain.Records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordsRepository extends JpaRepository<Records, Long> {

}
