package com.cognizant.ciqdashboardapi.models;

import com.cognizant.ciqdashboardapi.base.models.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "userSessions")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserSession extends BaseModel {
    @Id
    private String userId;
    private String emailId;
    private Date issuedAt;
}
