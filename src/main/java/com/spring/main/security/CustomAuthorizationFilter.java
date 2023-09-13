package com.spring.main.security;

import com.spring.main.SpringApplicationContext;
import com.spring.main.constants.AppConstants;
import com.spring.main.dtoClasses.UserDto;
import com.spring.main.services.UserService;
import com.spring.main.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AppConstants.HEADER_STRING);
        if(header==null||!header.startsWith(AppConstants.TOKEN_PREFIX)){
            filterChain.doFilter(request,response);
        }else {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(header);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String header) {
        if(header != null){
            String token = header.replace(AppConstants.TOKEN_PREFIX,"");
            String user = JWTUtils.hasTokenExpired(token)? null : JWTUtils.extractUser(token);
            List<GrantedAuthority> authorities = new ArrayList<>();

            if (user != null) {


                UserService userService = (UserService) SpringApplicationContext.getBean("userService");
                UserDto userDto = userService.getUser(user);
                String userRole = userDto.getRole();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole));

                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }
        }
        return null;
    }

}
