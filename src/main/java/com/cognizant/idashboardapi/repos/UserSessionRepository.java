package com.cognizant.idashboardapi.repos;

import com.cognizant.idashboardapi.models.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSessionRepository extends MongoRepository<UserSession, String> {
}
