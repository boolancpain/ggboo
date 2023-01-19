package com.fyo.ggboo.domain.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fyo.ggboo.domain.account.AccountRequestDto.AccountTransactionDto;
import com.fyo.ggboo.domain.account.TransactionRequestDto.TransactionCreateDto;
import com.fyo.ggboo.domain.account.TransactionResponseDto.TransactionInfoDto;
import com.fyo.ggboo.domain.member.Member;
import com.fyo.ggboo.domain.member.MemberRepository;
import com.fyo.ggboo.global.DateUtil;
import com.fyo.ggboo.global.SecurityUtil;
import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * Transaction Service
 * 
 * @author boolancpain
 */
@Service
public class TransactionService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomMessageSource customMessageSource;

    @Autowired
    private DateUtil dateUtil;

    /**
     * 거래내역 목록 조회
     * 
     * @param accountId
     * @param accountTransactionDto
     */
    public ResponseEntity<?> getTransactions(Long accountId, AccountTransactionDto accountTransactionDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 체크
        Account account = member.getAccount();
        if(account == null || account.getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 검색기간 세팅(default today)
        String searchDate = accountTransactionDto.getDate();
        if(!StringUtils.hasText(searchDate)) {
            searchDate = LocalDate.now().toString();
        }
        LocalDateTime startDate = dateUtil.toStartLocalDateTime(searchDate);
        LocalDateTime endDate = dateUtil.toEndLocalDateTime(searchDate);

        // 거래내역
        List<TransactionInfoDto> transactions = transactionRepository.findAllByAccountIdAndTransactedDateBetween(account.getId(), startDate, endDate).stream()
                .map(transaction -> TransactionInfoDto.builder()
                        .transactionId(transaction.getId())
                        .memberId(transaction.getMember().getId())
                        .categoryId(transaction.getCategory().getId())
                        .cost(transaction.getCost())
                        .remark(transaction.getRemark())
                        .transactedDate(transaction.getTransactedDate())
                        .build())
                .collect(Collectors.toList());

        // 200
        return ResponseEntity.ok(transactions);
    }

    /**
     * 거래내역 생성
     * 
     * @param accountId
     * @param transactionCreateDto
     */
    public ResponseEntity<?> createTransaction(Long accountId, TransactionCreateDto transactionCreateDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 회원 장부 체크
        Account account = member.getAccount();
        if(account == null || account.getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 카테고리
        Category category = categoryRepository.findByIdAndAccountId(transactionCreateDto.getCategoryId(), accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", transactionCreateDto.getCategoryId())));

        // 거래일 세팅(default today)
        String requestedTransactedDate = transactionCreateDto.getTransactedDate();
        if(!StringUtils.hasText(requestedTransactedDate)) {
            requestedTransactedDate = LocalDate.now().toString();
        }
        LocalDateTime transactedDate = dateUtil.toLocalDateTime(requestedTransactedDate);

        // 거래내역 생성
        Transaction transaction = Transaction.builder()
                .member(member)
                .account(account)
                .category(category)
                .cost(transactionCreateDto.getCost())
                .remark(transactionCreateDto.getRemark())
                .transactedDate(transactedDate)
                .build();

        // save
        transactionRepository.save(transaction);

        TransactionInfoDto transactionInfoDto = TransactionInfoDto.builder()
                .memberId(member.getId())
                .categoryId(transaction.getCategory().getId())
                .cost(transaction.getCost())
                .remark(transaction.getRemark())
                .transactedDate(transaction.getTransactedDate())
                .build();

        // 200
        return ResponseEntity.ok(transactionInfoDto);
    }

    /**
     * 거래내역 조회
     * 
     * @param accountId
     * @param transactionId
     */
    public ResponseEntity<?> getTransaction(Long accountId, Long transactionId) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 회원 장부 체크
        Account account = member.getAccount();
        if(account == null || account.getId() != accountId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 거래내역
        Transaction transaction = transactionRepository.findByIdAndAccountId(transactionId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("transaction.notfound", transactionId)));

        // convert to dto
        TransactionInfoDto transactionDto = TransactionInfoDto.builder()
                .memberId(transaction.getMember().getId())
                .categoryId(transaction.getCategory().getId())
                .cost(transaction.getCost())
                .remark(transaction.getRemark())
                .transactedDate(transaction.getTransactedDate())
                .build();

        // 200
        return ResponseEntity.ok(transactionDto);
    }

    /**
     * 거래내역 수정
     * 
     * @param accountId
     * @param transactionId
     * @param transactionCreateDto
     */
    @Transactional
    public ResponseEntity<?> updateTransaction(Long accountId, Long transactionId, TransactionCreateDto transactionCreateDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 회원 장부 체크
        Account account = member.getAccount();
        if(account == null || account.getId() != accountId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 거래내역
        Transaction transaction = transactionRepository.findByIdAndAccountId(transactionId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("transaction.notfound", transactionId)));

        /* 변경 사항 업데이트 */
        // 1. 카테고리
        Category category = categoryRepository.findByIdAndAccountId(transactionCreateDto.getCategoryId(), accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", transactionCreateDto.getCategoryId())));

        // update
        transaction.updateCategory(category);

        // 2. 금액
        // update
        transaction.updateCost(transactionCreateDto.getCost());

        // 3. 비고
        // update
        transaction.updateRemark(transactionCreateDto.getRemark());

        // 4. 거래일
        LocalDateTime transactedDate = dateUtil.toLocalDateTime(transactionCreateDto.getTransactedDate());

        // update
        transaction.updateTransactedDate(transactedDate);

        // convert to dto
        TransactionInfoDto transactionDto = TransactionInfoDto.builder()
                .transactionId(transaction.getId())
                .memberId(transaction.getMember().getId())
                .categoryId(transaction.getCategory().getId())
                .cost(transaction.getCost())
                .remark(transaction.getRemark())
                .transactedDate(transaction.getTransactedDate())
                .build();

        // 200
        return ResponseEntity.ok(transactionDto);
    }

    /**
     * 거래내역 삭제
     * 
     * @param accountId
     * @param transactionId
     */
    public ResponseEntity<?> deleteTransaction(Long accountId, Long transactionId) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 회원 장부 체크
        Account account = member.getAccount();
        if(account == null || account.getId() != accountId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 거래내역
        Transaction transaction = transactionRepository.findByIdAndAccountId(transactionId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("transaction.notfound", transactionId)));

        // delte
        transactionRepository.delete(transaction);

        // 204
        return ResponseEntity.noContent().build();
    }
}