package com.fyo.ggboo.domain.account;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fyo.ggboo.domain.account.CategoryRequestDto.CategoryCreateDto;
import com.fyo.ggboo.domain.account.CategoryRequestDto.CategoryReorderDto;
import com.fyo.ggboo.domain.account.CategoryResponseDto.CategoryInfoDto;
import com.fyo.ggboo.domain.member.Member;
import com.fyo.ggboo.domain.member.MemberRepository;
import com.fyo.ggboo.global.SecurityUtil;
import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * Category Service
 * 
 * @author boolancpain
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomMessageSource customMessageSource;

    /**
     * 카테고리 목록 조회
     * 
     * @param accountId
     */
    public ResponseEntity<?> getCategories(Long accountId) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("account.notfound", accountId)));

        // 카테고리 목록 조회
        List<CategoryInfoDto> categories = account.getCategories().stream()
                .map(category -> CategoryInfoDto.builder()
                        .categoryId(category.getId())
                        .alias(category.getAlias())
                        .sequence(category.getSequence())
                        .build())
                .sorted(Comparator.comparingInt(CategoryInfoDto::getSequence))
                .collect(Collectors.toList());

        // 200
        return ResponseEntity.ok(categories);
    }

    /**
     * 카테고리 생성
     * 
     * @param accountId
     * @param categoryCreateDto
     */
    @Transactional
    public ResponseEntity<?> createCategory(Long accountId, CategoryCreateDto categoryCreateDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }
        
        // 장부 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("account.notfound", accountId)));

        // 카테고리 유무 체크
        if(categoryRepository.existsByAccountIdAndAlias(accountId, categoryCreateDto.getAlias())) {
            // 409
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("category.exists"))
                            .build());
        }

        // 장부별 카테고리 수 제한
        // TODO 카테고리 수 설정값으로
        if(account.getCategories().size() >= 50) {
            // 409
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("category.toomany", 50))
                            .build());
        }

        // 카테고리 생성
        Category category = Category.builder()
                .accountId(accountId)
                .alias(categoryCreateDto.getAlias())
                .sequence(account.getCategories().size() + 1)
                .build();

        // save
        categoryRepository.save(category);

        CategoryInfoDto categoryDto = CategoryInfoDto.builder()
                .categoryId(category.getId())
                .alias(category.getAlias())
                .sequence(category.getSequence())
                .build();

        // 200
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * 카테고리 조회
     * 
     * @param accountId
     * @param categoryId
     */
    public ResponseEntity<?> getCategory(Long accountId, Long categoryId) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 카테고리 조회
        Category category = categoryRepository.findByIdAndAccountId(categoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", categoryId)));

        CategoryInfoDto categoryDto = CategoryInfoDto.builder()
                .categoryId(category.getId())
                .alias(category.getAlias())
                .sequence(category.getSequence())
                .build();

        // 200
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * 카테고리 수정
     * 
     * @param categoryId
     * @param categoryCreateDto
     */
    @Transactional
    public ResponseEntity<?> updateCategory(Long accountId, Long categoryId, CategoryCreateDto categoryCreateDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 카테고리 유무 체크
        if(categoryRepository.existsByAccountIdAndAlias(accountId, categoryCreateDto.getAlias())) {
            // 409
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("category.exists"))
                            .build());
        }

        // 카테고리 조회
        Category category = categoryRepository.findByIdAndAccountId(categoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", categoryId)));

        // update
        category.updateAlias(categoryCreateDto.getAlias());

        CategoryInfoDto categoryDto = CategoryInfoDto.builder()
                .categoryId(category.getId())
                .alias(category.getAlias())
                .sequence(category.getSequence())
                .build();

        // 200
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * 카테고리 삭제
     * 
     * @param categoryId
     */
    public ResponseEntity<?> deleteCategory(Long accountId, Long categoryId) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 카테고리 조회
        Category category = categoryRepository.findByIdAndAccountId(categoryId, accountId)
                .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", categoryId)));

        // delete
        categoryRepository.delete(category);

        // 204
        return ResponseEntity.noContent().build();
    }

    /**
     * 카테고리 정렬순서 수정
     * 
     * @param accountId
     * @param categoryReorderDto
     */
    @Transactional
    public ResponseEntity<?> reorderCategories(Long accountId, CategoryReorderDto categoryReorderDto) {
        // 로그인 회원
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UsernameNotFoundException(null));

        // 장부 id 체크
        if(member.getAccount().getId() != accountId) {
            // 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(BaseResponse.builder()
                            .message(customMessageSource.getMessage("forbidden"))
                            .build());
        }

        // 카테고리 조회
        List<Category> categories = categoryReorderDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndAccountId(categoryId, accountId)
                            .orElseThrow(() -> new EntityNotFoundException(customMessageSource.getMessage("category.notfound", categoryId))))
                .collect(Collectors.toList());

        // update sequence
        for(int i = 0;i < categories.size();i++) {
            categories.get(i).updateSequence(i + 1);
        }

        // 카테고리 목록 조회
        return this.getCategories(accountId);
    }
}