package org.alpermelkeli.security.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.alpermelkeli.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;


public class LoginRateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private  CacheManager cacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getServletPath().equals("/v1/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = getClientIP(request);

        Bucket bucket = getBucket(ip);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many login attempts. Please try again later.");
        }
    }

    private Bucket getBucket(String key) {
        Cache cache = cacheManager.getCache("loginBuckets");
        if (cache != null) {
            Bucket bucket = cache.get(key, Bucket.class);
            if (bucket != null) {
                return bucket;
            }

            bucket = createNewBucket();
            cache.put(key, bucket);
            return bucket;
        }
        return createNewBucket();
    }

    private Bucket createNewBucket() {
        // Rate limit rules: 5 try / 15 minutes
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(SecurityConstants.rateLimitToken, Duration.ofMinutes(SecurityConstants.rateLimitDuration)));
        return Bucket4j.builder().addLimit(limit).build();
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
