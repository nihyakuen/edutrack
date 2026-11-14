package aloe.edutrack.security;

import aloe.edutrack.domain.user.User;
import aloe.edutrack.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NullMarked
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    public Long getId() { return user.getId(); }
    public String getEmail() { return user.getEmail(); }
    public String getFirstName() { return user.getFirstName(); }
    public String getLastName() { return user.getLastName(); }
    public UserRole getRole() { return user.getRole(); }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return this.user.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /*
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
*/
}

