package com.sn.shehan_n.cafe_management_system.auth.filter;

import com.sn.shehan_n.cafe_management_system.auth.service.CustomUserDetailsService;
import com.sn.shehan_n.cafe_management_system.auth.util.JwtUtil;
import com.sn.shehan_n.cafe_management_system.auth.util.TenantContextHolder;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    //jwtFilter

    @Autowired
    private JwtUtil jwtUtil;//token provider

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    Claims claims=null;
    private String userName=null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().matches("/user/login | /user/signUp | /user/forgotPassword")) {
            filterChain.doFilter(request, response);
        } else {
           try{
               String authorizationHeader = request.getHeader("Authorization-Token");
               String token = null;

               if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                   token = authorizationHeader.substring(7);
                   userName = jwtUtil.extractUsername(token);
                   claims = jwtUtil.extractAllClaims(token);
               }
               if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                   UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
                   if (jwtUtil.validateToken(token, userDetails)) {
                       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                               new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                       usernamePasswordAuthenticationToken.setDetails(
                               new WebAuthenticationDetailsSource().buildDetails(request)
                       );
                       SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                   }
               }
           }catch (Exception ex){
               log.error("Could not set user authentication in security context", ex);

           }
            filterChain.doFilter(request, response);
        }
    }
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public boolean isUser() {
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        log.info(userName);
        return userName;
    }

    //for additional token extraction
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization-Token");

        /* BEGIN ADDITIONAL TENANT RESOLVER LOGIC*/
        String tenantID = request.getHeader("X-TenantID");

        if (tenantID == null) {
            String requestParameter = request.getParameter("X-TenantID");
            if (requestParameter != null) {
                tenantID = requestParameter;
            }
        }

        if (tenantID != null) {
            TenantContextHolder.setTenantId(tenantID);
        }
        /* END ADDITIONAL TENANT RESOLVER LOGIC*/

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
