package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.models.UserSession;
import com.cognizant.ciqdashboardapi.repos.UserSessionRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserSessionService {
    @Autowired
    UserSessionRepository repository;

    public List<UserSession> getAll() {
        return repository.findAll();
    }

    public Optional<UserSession> get(String id) {
        return repository.findById(id);
    }

    public UserSession save(UserSession userSession) {
        return repository.save(userSession);
    }

    public boolean validateSession(Claims claims) {
        Optional<UserSession> optional = get(claims.getSubject());
        Date issuedAt = claims.getIssuedAt();
        UserSession userSession = new UserSession(claims.getSubject(), claims.get("email", String.class), issuedAt);
        if (optional.isEmpty()) {
            save(userSession);
            return true;
        } else {
            UserSession session = optional.get();
            long issuedAtTime = issuedAt.getTime();
            long sessionIssueTime = session.getIssuedAt().getTime();
            if (issuedAtTime == sessionIssueTime){
                return true;
            }else if (issuedAtTime > sessionIssueTime){
                save(userSession);
                return true;
            }else {
                return false;
            }
        }
    }
}
