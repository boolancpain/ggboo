package com.fyo.ggboo.domain.account;

import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fyo.ggboo.domain.account.AccountResponseDto.AccountInfoDto;
import com.fyo.ggboo.domain.member.Member;
import com.fyo.ggboo.domain.member.MemberRepository;
import com.fyo.ggboo.global.SecurityUtil;
import com.fyo.ggboo.global.enums.DefaultCategory;
import com.fyo.ggboo.global.response.BaseResponse;
import com.fyo.ggboo.infra.message.CustomMessageSource;

/**
 * Account Service
 * 
 * @author boolancpain
 */
@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CustomMessageSource customMessageSource;
	
	/**
	 * 장부 생성
	 */
	@Transactional
	public ResponseEntity<?> createAccount() {
		// 로그인 회원
		Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
				.orElseThrow(() -> new UsernameNotFoundException(null));
		
		// 장부 유무 체크
		if(member.getAccount() != null) {
			// 409
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(BaseResponse.builder()
							.message(customMessageSource.getMessage("account.exists"))
							.build());
		}
		
		// 새 장부 생성
		Account account = Account.builder().build();
		
		// 회원 장부 추가
		member.updateAccount(account);
		
		// account save
		accountRepository.save(account);
		
		// 기본 카테고리 생성
		Stream.of(DefaultCategory.values()).forEach(defaultCategory -> {
			// 카테고리 생성
			Category category = Category.builder()
					.accountId(account.getId())
					.alias(defaultCategory.getAlias())
					.sequence(defaultCategory.getSequence())
					.build();
			
			// save
			categoryRepository.save(category);
		});
		
		AccountInfoDto accountInfoDto = AccountInfoDto.builder()
				.accountId(account.getId())
				.build();
		
		// 200
		return ResponseEntity.ok(accountInfoDto);
	}
	
	/**
	 * 회원 장부 조회
	 */
	public ResponseEntity<?> getMyAccount() {
		// 로그인 회원
		Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
				.orElseThrow(() -> new UsernameNotFoundException(null));
		
		Account account = member.getAccount();
		
		AccountInfoDto accountInfoDto = AccountInfoDto.builder()
				.accountId(account.getId())
				.build();
		
		// 200
		return ResponseEntity.ok(account == null ? null : accountInfoDto);
	}
	
	/**
	 * 장부 삭제(내 장부를 삭제)
	 * 
	 * @param accountId
	 * TODO 거래내역 입력된 장부를 삭제할때 거래내역을 어떻게 처리할지
	 * 1. 장부에서 나오며 거래내역 처리에 대한 내용
	 *  1-1. 기존 장부의 거래내역의 member_id를 null 처리하여 익명회원의 거래내역으로 처리
	 *  1-2. 기존 거래내역의 장부 id를 null처리 했다가 새 장부 생성 or 참여시 이관
	 * 2. 장부 잘 삭제되는지 테스트
	 */
	@Transactional
	public ResponseEntity<?> deleteAccount(Long accountId) {
		// 로그인 회원
		Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
				.orElseThrow(() -> new UsernameNotFoundException(null));
		
		// 장부 조회
		Account account = member.getAccount();
		if(account == null || account.getId() != accountId) {
			// 403
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(BaseResponse.builder()
							.message(customMessageSource.getMessage("account.notfound", accountId))
							.build());
		}
		
		// 장부의 회원 목록에서 제거
		account.removeMember(member.getId());
		
		// 회원의 장부 키 삭제
		member.updateAccount(null);
		
		// 장부를 소유한 회원이 없는 경우 장부와 관련 정보 모두 삭제
		if(account.getMembers().isEmpty()) {
			// 카테고리 삭제
			categoryRepository.deleteAllByAccountId(account.getId());
			
			// 거래내역 삭제
			transactionRepository.deleteAllByAccountId(account.getId());
			
			// 장부 삭제
			accountRepository.deleteById(account.getId());
		}
		
		// 204
		return ResponseEntity.noContent().build();
	}
}