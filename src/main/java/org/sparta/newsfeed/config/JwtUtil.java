package org.sparta.newsfeed.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME =  30 * 60 * 1000L;    //30분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key ;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct // 빈 초기화 직후 추가작업
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes); // 이후 JWT 토큰 생성 및 검승에 사용됨
    }

    // JWT 생성
    public String createToken(Long userId, String email){
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // JWT 추출
    public String substringToken(String tokenValue){
        if(StringUtils.hasText(tokenValue)&&tokenValue.startsWith(BEARER_PREFIX)){
            return tokenValue.substring(7);//Bearer
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // JWT 파싱후 Claims 추출
    //// Claims = 페이로드. 즉, 토큰에 담긴 사용자 정보, 만료 시간 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // JWT에서 직접 이메일 추출
//    public String getEmailFromRequest(HttpServletRequest request) {
//        // Authorization 헤더에서 JWT 토큰 추출
//        String token = resolveToken(request);
//        if (token != null) {
//            // 토큰에서 클레임을 추출하여 이메일을 반환
//            Claims claims = extractClaims(token);
//            return claims.get("email", String.class);
//        }
//        log.error("Invalid or missing JWT token");
//        return null;
//    }
//
//    // Authorization 헤더에서 JWT 토큰을 추출
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            return bearerToken.substring(BEARER_PREFIX.length());
//        }
//        return null;
//    }

}
