package com.fyo.ggboo.domain.account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Transaction Repository
 * 
 * @author boolancpain
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * 장부 id와 검색 기간으로 거래내역을 조회
     * 
     * @param memberId
     * @param accountId
     * @param startDate
     * @param endDate
     */
    public List<Transaction> findAllByAccountIdAndTransactedDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 거래내역 id, 장부 id로 거래내역 조회
     * 
     * @param id
     * @param memberId
     * @param accountId
     */
    public Optional<Transaction> findByIdAndAccountId(Long id, Long accountId);

    /**
     * 장부 id로 거래내역 삭제
     * 
     * @param accountId
     */
    public void deleteAllByAccountId(Long accountId);
}