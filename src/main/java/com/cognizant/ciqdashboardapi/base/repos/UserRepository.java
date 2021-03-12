package com.cognizant.ciqdashboardapi.base.repos;

import com.cognizant.ciqdashboardapi.base.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by 784420 on 7/17/2019 7:24 PM
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    void deleteByIdIn(List<String> ids);
}
