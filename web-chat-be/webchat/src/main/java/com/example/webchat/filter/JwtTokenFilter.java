package com.example.webchat.filter;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.model.Users;
import com.example.webchat.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;
    private final ServletContext servletContext;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authenticate = request.getHeader("Authorization");
            if (authenticate == null || !authenticate.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return; // Kết thúc xử lý ngay khi lỗi
            }

            final String token = authenticate.substring(7); // lay doan ma token tu chi so 7 den het chuoi
            final String username = jwtTokenUtils.extractUserName(token);
            List<String> tokenList = redisService.getTokenList(username);
            for (String tokenLogout : tokenList ) {
                if (tokenLogout.equals(token)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Token này đã bị chặn. Vui lòng đăng nhập lại.");
                    return;
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Users userDetials = (Users) userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtils.validateToken(token, (UserDetails) userDetials)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetials, null, userDetials.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"unAuthorized");
            return;
        }
    }



    /**
     * kiem tra xem yeu cau co phai bo qua xac thuc hay khong
     * @param request doi tuong HttpServletRequest chua thong tin yeu cau tu Request
     * @return tra ve true neu bo qua token , false neu khong
     */

    private boolean isByPassToken(@NonNull HttpServletRequest request) {

        final List<org.modelmapper.internal.Pair<String, String >> bypassTokens = Arrays.asList(
                org.modelmapper.internal.Pair.of("/api/user/register","POST"),
                org.modelmapper.internal.Pair.of("/api/user/image","GET"),
                org.modelmapper.internal.Pair.of("/api/login","POST"),
                org.modelmapper.internal.Pair.of("/ws","GET"),
                org.modelmapper.internal.Pair.of("/ws/","GET"),
                org.modelmapper.internal.Pair.of("/ws/**","GET"),
                org.modelmapper.internal.Pair.of("/api/user/login-google-facebook","POST")
        );

        for (Pair<String, String > token : bypassTokens) {
            if (request.getServletPath().contains(token.getLeft())
                    && request.getMethod().equals(token.getRight())) {
                return true;
            }
        }
        return false;
    }
}
