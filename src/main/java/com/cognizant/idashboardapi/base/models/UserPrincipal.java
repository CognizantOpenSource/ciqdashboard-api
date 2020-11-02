package com.cognizant.idashboardapi.base.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
public class UserPrincipal implements UserDetails {
    private String id;
    private String name;
    private boolean active;
    @JsonIgnore
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
    private Account account;

    private UserPrincipal() {
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getAccount().getRoles().stream()
                .map(Role::getPermissions)
                .flatMap(List::stream)
                .map(Permission::getId)
                .map(SimpleGrantedAuthority::new).forEach(authorities::add);

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.id = user.getId();
        userPrincipal.name = user.getName();
        userPrincipal.active = user.isActive();
        userPrincipal.email = user.getEmail();
        userPrincipal.account = user.getAccount();
        userPrincipal.authorities = authorities;
        return userPrincipal;
    }

    public static UserDetails create(User user, List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        permissions.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.id = user.getId();
        userPrincipal.name = user.getName();
        userPrincipal.active = user.isActive();
        userPrincipal.email = user.getEmail();
        userPrincipal.account = user.getAccount();
        userPrincipal.authorities = authorities;
        return userPrincipal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @JsonIgnore
    @Value("${app.permission.admin://}")
    private String adminPermission;

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                ", account=" + account +
                '}';
    }
}
