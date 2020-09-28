package com.wdqsoft.shiro.jwt;

import com.wdqsoft.common.utils.aesutils.AESCoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "jwtconfig.jwt")
public class JwtUtils {
    private String secret;//秘钥
    private long expire;//超时时间
    private String header;
    private String asekey;
    /**
     * token生成器
     * @param userId
     * @return
     */
    public String generateToken(String userId){
        Date nowDate=new Date();
        Date exporeDate=new Date(nowDate.getTime()+expire*1000);
        String token= Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(exporeDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact()
                ;
        try {
//            log.info("加密前token"+token);
            token=  AESCoder.setData(asekey,token);
//            log.info("加密后token"+token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public Claims getClaimByToken(String token){
        try {
//            String token2="";
//            log.info("解密前token"+token);
            token= AESCoder.getData(asekey,token);
//            log.info("解密后token"+token);
            return Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.info("validate is token error"+e);
            return null;
        }
    }

    /**
     * 判断token是否过期
     * @param expiration
     * @return true 过期
     */
    public boolean isTokenExpired(Date expiration){
        return expiration.before(new Date());
    }

}
