package com.hzh.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {


    //单位是毫秒
    private static long ttl = Constants.Millions.TWO_HOUR;  //2h

    public long getTtl(){
        return ttl;
    }

    public void setTtl(long ttl){
        this.ttl= ttl;
    }

     /**
      * @Author Hou zhonghu
      * @Description  claims 载荷内容
      * @Date 2022/8/3 19:54
      * @Param JWT_TTL  有效时长
      * @return
      **/
     public static  String createToken(Map<String,Object> claims,long ttl,String salt){
         JwtUtil.ttl = ttl;
         return createToken(claims,salt);
     }

     public static  String createRefreshToken(String userId,long ttl,String salt){
         long nowMills = System.currentTimeMillis();
         Date now = new Date(nowMills);
         JwtBuilder builder = Jwts.builder().setId(userId)
                 .setIssuedAt(now)
                 .signWith(SignatureAlgorithm.HS256,salt);
         if (ttl > 0){
             builder.setExpiration(new Date(nowMills + ttl));
         }
         return builder.compact();
     }

    /**
     * @Author Hou zhonghu
     * @Description  claims 载荷内容
     * @Date 2022/8/3 19:54
     * @Param JWT_TTL  有效时长
     * @return
     **/
    public static  String createToken(Map<String,Object> claims,String salt){
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256,salt);
        if (claims != null){
            builder.setClaims(claims);
        }
        if (ttl > 0){
            builder.setExpiration(new Date(nowMills + ttl));
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwtStr,String salt){
        return Jwts.parser()
                .setSigningKey(salt)
                .parseClaimsJws(jwtStr)
                .getBody();
    }

}