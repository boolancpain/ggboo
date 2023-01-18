package com.fyo.ggboo.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import com.fyo.ggboo.domain.account.Account;
import com.fyo.ggboo.global.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Member Class
 * 
 * @author boolancpain
 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Member extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 255, nullable = false, unique = true)
	private String memberId;
	
	@Column(length = 255, nullable = false)
	private String password;
	
	@Column(length = 255, nullable = false)
	private String memberName;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	@Builder
	public Member(String memberId, String password, String memberName) {
		this.memberId = memberId;
		this.password = password;
		this.memberName = memberName;
	}
	
	/**
	 * 비밀번호 업데이트
	 * 
	 * @param encryptedPassword
	 */
	public void updatePassword(String encryptedPassword) {
		if(StringUtils.hasText(encryptedPassword)) {
			this.password = encryptedPassword;
		}
	}
	
	/**
	 * 회원 이름 업데이트 
	 * 
	 * @param memberName
	 */
	public void updateMemberName(String memberName) {
		if(StringUtils.hasText(memberName)) {
			this.memberName = memberName;
		}
	}
	
	/**
	 * 장부 업데이트
	 * 
	 * @param account
	 */
	public void updateAccount(Account account) {
		this.account = account;
	}
}