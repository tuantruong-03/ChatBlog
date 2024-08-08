package simple.blog.backend.config.webSocket;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import simple.blog.backend.exception.AppException;
import simple.blog.backend.util.JwtUtil;

@Component
@RequiredArgsConstructor
public class ChannelInterceptorImpl implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        MessageHeaders messageHeaders = message.getHeaders();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, List<String>> multiValueMap = messageHeaders.get(StompHeaderAccessor.NATIVE_HEADERS,
                MultiValueMap.class);
        if (accessor.getCommand() != StompCommand.CONNECT) {
            // Handle subscription requests here if needed
            return message;
        }
        if (multiValueMap == null || multiValueMap.isEmpty()) {
            throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }

        List<String> authorizationHeaders = multiValueMap.get("Authorization");
        if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
            throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }

        String bearerToken = authorizationHeaders.get(0);
        String accessToken = resolveToken(bearerToken);
        if (accessToken == null) {

            throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }
        try {
            boolean isTokenValid = jwtUtil.isTokenValid(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AppException("Token is expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new AppException("Token is not valid", HttpStatus.UNAUTHORIZED);
        }

        return message;
    }

    private String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
