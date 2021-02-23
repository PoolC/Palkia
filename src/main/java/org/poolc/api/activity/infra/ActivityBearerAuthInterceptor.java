package org.poolc.api.activity.infra;

import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.auth.vo.ParsedTokenValues;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ActivityBearerAuthInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public ActivityBearerAuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        try {
            String token = removeBearerFromToken(request.getHeader("authorization"));
            ParsedTokenValues userInfo = new ParsedTokenValues(jwtTokenProvider.getBodyFromJwtToken(token));
            request.setAttribute("UUID", userInfo.getUUID());
            request.setAttribute("isAdmin", userInfo.getIsAdmin());
            return true;
        } catch (Exception e) {
            request.setAttribute("message", "유효하지 않은 토큰입니다");
            request.setAttribute("exceptionClass", "Exception");
            request.getRequestDispatcher("/error").forward(request, response);
            return false;
        }
    }

    String removeBearerFromToken(String token) {
        return token.substring(7);
    }
}
