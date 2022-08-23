package com.aplab.apsite.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aplab.apsite.dbmst.entity.user.UserTokenEntity;
import com.aplab.apsite.dbmst.mapper.CommonMapper;
import com.aplab.apsite.dbmst.repo.user.UserTokenRepo;
import com.aplab.apsite.model.dto.user.TokenDTO;
import com.aplab.apsite.model.vo.SessionVO;
import com.aplab.apsite.utils.JwtTokenUtil;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${spring.jwt.headerKey}")
    private String jwtHeaderKey;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserTokenRepo userTokenRepo;
    
    private final CommonMapper commonMapper;
    
    private final RequestMatcher apiRequests = new AntPathRequestMatcher("/api/**");
    private final RequestMatcher uploadRequests = new AntPathRequestMatcher("/upload/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (!apiRequests.matches(request) && !uploadRequests.matches(request)) {
            chain.doFilter(request, response);
            return;
        }
        
        String requestTokenHeader = request.getHeader(jwtHeaderKey);
        String langCd = request.getHeader("x-lang-cd");

        if (StringUtils.isEmpty(langCd)) {
            langCd = "ko";
        }

        if (log.isDebugEnabled()) {
            StringBuffer sbLog = new StringBuffer();
            sbLog.append("\n").append("URI : ").append(request.getRequestURI());
            sbLog.append("\n").append("Method : ").append(request.getMethod());
            sbLog.append("\n").append(jwtHeaderKey + " : ").append(requestTokenHeader);
            log.debug(sbLog.toString());
        }

        if (requestTokenHeader != null) {

            try {

                Claims claims = jwtTokenUtil.getAllClaimsFromToken(requestTokenHeader);

                // String subject = jwtTokenUtil.getClaimFromToken(claims, Claims::getSubject);
                Date expiration = jwtTokenUtil.getClaimFromToken(claims, Claims::getExpiration);

                if (SecurityContextHolder.getContext().getAuthentication() == null && expiration.after(new Date())) {
                    // String userCd = claims.get("userCd", String.class);

                    UserTokenEntity userTokenEntity = userTokenRepo.findById(requestTokenHeader).orElse(null);

                    if (userTokenEntity != null) {
                        String jsonData = userTokenEntity.getJsonData();
                        String loginPw = userTokenEntity.getLoginPw();
                        TokenDTO tokenDTO = (new Gson()).fromJson(jsonData, TokenDTO.class);

                        SessionVO sessionVO = SessionVO.builder()
                            .userCd(tokenDTO.getUserCd())
                            .loginId(tokenDTO.getLoginId())
                            .loginPw(loginPw)
                            .userNm(tokenDTO.getUserNm())
                            .compCd(tokenDTO.getCompCd())
                            .masterFlag(tokenDTO.getMasterFlag())
                            .roles(tokenDTO.getRoles())
                            .groups(tokenDTO.getGroups())
                            .langCd(langCd)
                            .build();

                        UserDetails userDetails = new SecurityUser(sessionVO);

                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    
                        List<String> inclusionURI = Arrays.asList("/api/comp", "/api/ingrd", "/api/myInfo", "/api/council", "/api/sourcing");
                        int pageAuthCount = 0;
                        for (String uri : inclusionURI) {
                            if (request.getRequestURI().indexOf(uri) > -1) {
                                pageAuthCount = commonMapper.findPageAuthCount(tokenDTO.getGroups(), uri);
                                
                                if (pageAuthCount == 0) {
                                    throw new BusinessException("NO_AUTH", "권한 불충분");
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }

        chain.doFilter(request, response);
    }
}
