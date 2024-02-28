package com.heeju.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.Authenticator;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName(); // Query the information of the currently logged in user and specify
                                               // the user's name as the registrant and modifier.
        }

        return Optional.of(userId);
        // Optional: Provides a clear and explicit way to convey the message that there may be no value without using null.
        // Optional.of: If the value is not null, if some data is never null, Optional.of() can be created.
        // If you try to store null with Optional.of(), a NullPointerException occurs.
    }
}
