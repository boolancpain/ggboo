package com.fyo.ggboo.domain.account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fyo.ggboo.domain.account.CategoryRequestDto.CategoryCreateDto;
import com.fyo.ggboo.domain.account.CategoryRequestDto.CategoryReorderDto;

/**
 * Category Controller
 * 
 * @author boolancpain
 */
@RestController
@RequestMapping("/accounts")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 카테고리 목록 조회
     * 
     * @param accountId
     */
    @GetMapping(value = "{accountId}/categories")
    public ResponseEntity<?> getCategories(@PathVariable Long accountId) {
        return categoryService.getCategories(accountId);
    }

    /**
     * 카테고리 생성
     * 
     * @param accountId
     * @param categoryCreateDto
     */
    @PostMapping(value = "{accountId}/categories")
    public ResponseEntity<?> createCategory(@PathVariable Long accountId, @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        return categoryService.createCategory(accountId, categoryCreateDto);
    }

    /**
     * 카테고리 조회
     * 
     * @param accountId
     * @param categoryId
     */
    @GetMapping(value = "{accountId}/categories/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long accountId, @PathVariable Long categoryId) {
        return categoryService.getCategory(accountId, categoryId);
    }

    /**
     * 카테고리 수정
     * 
     * @param accountId
     * @param categoryId
     * @param categoryCreateDto
     */
    @PutMapping(value = "{accountId}/categories/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable Long accountId, @PathVariable Long categoryId, @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        return categoryService.updateCategory(accountId, categoryId, categoryCreateDto);
    }

    /**
     * 카테고리 삭제
     * 
     * @param accountId
     * @param categoryId
     */
    @DeleteMapping(value = "{accountId}/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long accountId, @PathVariable Long categoryId) {
        return categoryService.deleteCategory(accountId, categoryId);
    }

    /**
     * 카테고리 정렬순서 수정
     * 
     * @param accountId
     * @param categoryReorderDto
     */
    @PutMapping(value = "{accountId}/categories/reorder")
    public ResponseEntity<?> reorderCategories(@PathVariable Long accountId, @Valid @RequestBody CategoryReorderDto categoryReorderDto) {
        return categoryService.reorderCategories(accountId, categoryReorderDto);
    }
}