package com.fyo.ggboo.domain.account;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fyo.ggboo.domain.member.Member;
import com.fyo.ggboo.global.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;

/**
 * Account Class
 * 
 * @author boolancpain
 */
@Entity
@Table(name = "account")
@Getter
@DynamicUpdate
public class Account extends BaseTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy = "account")
	private List<Member> members;
	
	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;
	
	@OneToMany
	@JoinColumn(name = "accountId")
	private List<Category> categories;
	
	@Builder
	public Account() {
		
	}
	
	/**
	 * 장부의 회원 목록에서 제거
	 * 
	 * @param memberId
	 */
	public void removeMember(Long memberId) {
		this.members.removeIf(member -> member.getId() == memberId);
	}
}