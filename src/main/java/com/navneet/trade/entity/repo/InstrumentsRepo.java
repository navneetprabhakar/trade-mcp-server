package com.navneet.trade.entity.repo;

import com.navneet.trade.entity.Instruments;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author navneet.prabhakar
 */
@Repository
public interface InstrumentsRepo extends JpaRepository<Instruments, Long> {

  /**
   * Finds distinct instruments by name containing a specified string (case-insensitive),
   * exchange, and segment.
   *
   * @param name     The substring to search for in the instrument names.
   * @param exchange The exchange to filter the instruments by.
   * @param segment  The segment to filter the instruments by.
   * @return A list of distinct Instruments matching the criteria.
   */
  List<Instruments> findDistinctByNameContainingIgnoreCaseAndExchangeAndSegment(
      String name, String exchange, String segment);
}
