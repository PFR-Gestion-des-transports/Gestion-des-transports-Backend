package gestiontransports.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {

    @Id
    private String jti;

    private LocalDateTime expireAt;

    public TokenBlacklist() {}

    public TokenBlacklist(String jti, LocalDateTime expireAt) {
        this.jti = jti;
        this.expireAt = expireAt;
    }

    public String getJti() { return jti; }
    public LocalDateTime getExpireAt() { return expireAt; }
}
