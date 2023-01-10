package com.fyo.ggboo.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Account Repository
 * 
 * @author boolancpain
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
}