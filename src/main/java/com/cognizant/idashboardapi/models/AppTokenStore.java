package com.cognizant.idashboardapi.models;

import com.cognizant.idashboardapi.base.models.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "appTokenStore")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppTokenStore extends BaseModel {
    @Id
    private String id;
    @NotBlank(message = "Reference Id should not be empty/null")
    @Size(min = 24, message = "Reference Id should be minimum 24 characters")
    @Indexed(unique = true)
    private String refId;
    private String name;
    private AppTokenType type;
    private String tokenExpiresAt;
    @JsonIgnore
    private String token;

    public enum  AppTokenType{
        ROBOT, DASHBOARD_PROJECT, USER
    }
}
