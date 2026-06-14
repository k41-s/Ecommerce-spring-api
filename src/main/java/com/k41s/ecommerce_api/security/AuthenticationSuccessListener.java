package com.k41s.ecommerce_api.security;

import com.k41s.ecommerce_api.enums.LogLevel;
import com.k41s.ecommerce_api.services.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LogService logService;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            String logMessage = getLogMessage(event, attributes);
            logService.log(LogLevel.INFORMATION, logMessage);

        } catch (Exception e) {
            logService.log(LogLevel.ERROR, "Failed to log authentication success: " + e.getMessage());
        }
    }

    private static @NonNull String getLogMessage(@NonNull AuthenticationSuccessEvent event, ServletRequestAttributes attributes) {
        HttpServletRequest request = attributes.getRequest();
        String ipAddress = request.getRemoteAddr();

        Object principal = event.getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }

        return String.format("Successful login for user: '%s' from IP Address: %s", username, ipAddress);
    }
}