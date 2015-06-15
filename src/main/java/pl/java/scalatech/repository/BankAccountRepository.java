package pl.java.scalatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.java.scalatech.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
