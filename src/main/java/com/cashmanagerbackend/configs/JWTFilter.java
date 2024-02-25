//package com.cashmanagerbackend.configs;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.cashmanagerbackend.security.JWTUtil;
//import com.cashmanagerbackend.services.servieces_implementation.UserDetailsServiceImplementation;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//@Component
//public class JWTFilter extends OncePerRequestFilter {
//    private final JWTUtil jwtUtil;
//    private final UserDetailsServiceImplementation userDetailsService;
//
//    @Autowired
//    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImplementation userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
//            String jwt = authHeader.substring(7);
//            if (jwt.isBlank()  ){
//                filterChain.doFilter(request, response);
//                return;
//            }else{
//                try {
//                    String login = jwtUtil.validateTakenAndRetrieveClaim(jwt);
//                    UserDetails userDetails = userDetailsService.loadUserByUsername(login);
//
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    userDetails.getPassword(),
//                                    userDetails.getAuthorities());
//                    if (SecurityContextHolder.getContext().getAuthentication() == null){
//                        SecurityContextHolder.getContext()
//                                .setAuthentication(authenticationToken);
//                    }
//                } catch (JWTVerificationException exc) {
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
