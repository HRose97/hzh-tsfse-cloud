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

    //有效期为1H
    public static Long jwtTtl = 60 * 60 *1000L;// 60 * 60 *1000  1个小时
    public static Long jwtTtlOneDay = jwtTtl * 24;// 60 * 60 *1000  1天
    public static Long jwtTtlOneWeek = jwtTtlOneDay * 7;// 60 * 60 *1000  1周
    public static Long jwtTtlOneMonth = jwtTtlOneDay * 30;// 60 * 60 *1000  1月
    //设置秘钥明文
    public static String jwtKey = "hzh-TSFSE";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }
    
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.jwtTtl;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("hzh")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        //jwt加密
        String jwt = createJWT("2123");
        System.out.println(jwt);
        //jwt解密
        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOTY2ZGE3NGYyZGM0ZDAxOGU1OWYwNjBkYmZkMjZhMSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTYzOTk2MjU1MCwiZXhwIjoxNjM5OTY2MTUwfQ.NluqZnyJ0gHz-2wBIari2r3XpPp06UMn4JS2sWHILs0");
        String subject = claims.getSubject();
        System.out.println(subject);
        System.out.println(claims);
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.jwtKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    //===================================
    public long getTtl(){
        return jwtTtl;
    }

    public void setJwtTtl(long jwtTtl){
        this.jwtTtl= jwtTtl;
    }

     /**
      * @Author Hou zhonghu
      * @Description  claims 载荷内容
      * @Date 2022/8/3 19:54
      * @Param JWT_TTL  有效时长
      * @return
      **/
     public static  String createToken(Map<String,Object> claims,long ttl,String salt){
         JwtUtil.jwtTtl = ttl;
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
        if (jwtTtl > 0){
            builder.setExpiration(new Date(nowMills + jwtTtl));
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