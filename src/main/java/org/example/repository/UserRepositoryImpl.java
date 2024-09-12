package org.example.repository;

import org.example.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDto> getAllUserAndRoleId() {
        String sql = "SELECT u.id, u.username, u.password, r.id AS role_id FROM users AS u INNER JOIN roles AS r ON u.id = r.user_id";
        Query query = entityManager.createNativeQuery(sql, "UserRoleDtoMapping");
        return query.getResultList();
    }
}
