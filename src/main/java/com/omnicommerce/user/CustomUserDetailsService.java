package com.omnicommerce.user;

import com.omnicommerce.user.position.Position;
import com.omnicommerce.user.position.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = (User) userOptional.get();
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            List<Position> positions = positionRepository.findByPositionKeyUserId(user.getId());
            for (Position r : positions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(r.getRole().getName()));
            }
            user.setRoles(grantedAuthorities);
            return user;
        } else return null;
    }
}
