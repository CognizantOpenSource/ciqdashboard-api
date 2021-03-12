package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSessionRepository extends MongoRepository<UserSession, String> {
}
