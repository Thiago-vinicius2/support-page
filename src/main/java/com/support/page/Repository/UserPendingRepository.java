package com.support.page.Repository;

import com.support.page.Entity.UserPending.UserPending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPendingRepository extends JpaRepository<UserPending, Long> {

    Optional<UserPending> findByEmail(String email);

}
