package blackjack.backend.repository;


import blackjack.backend.domain.AdminUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends MongoRepository<AdminUser, String> {
    Optional<AdminUser> findOneByEmailAndPassword(String email, String password);
    AdminUser findByEmail(String email);
}
