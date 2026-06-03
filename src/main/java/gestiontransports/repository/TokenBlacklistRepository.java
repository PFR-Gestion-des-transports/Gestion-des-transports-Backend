package gestiontransports.repository;

import gestiontransports.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {

    @Modifying
    @Transactional
    @Query("DELETE FROM TokenBlacklist t WHERE t.expireAt < :now")
    void deleteExpired(LocalDateTime now);
}
