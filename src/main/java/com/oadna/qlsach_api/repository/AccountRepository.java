package com.oadna.qlsach_api.repository;

import com.oadna.qlsach_api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
