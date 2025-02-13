package org.bank.store.mysql.global.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface JpaBaseRepository<T, ID> extends JpaRepository<T, ID> {
    Page<T> findAll(Pageable pageable);
    Optional<T> findById(ID id);
    void deleteById(ID id);
}
