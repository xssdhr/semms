package com.xssdhr.semms.utils;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * jwt工具类
 */
public class JwtUtil {
    //有效期为
    public static final long JWT_TTL = 60 * 60 * 1000L;
    //设置秘钥明文
    public static final String JWT_KEY = "xssdhr";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-","");
        return token;
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     */
    public static String createJWT(String subject){
        JwtBuilder builder = getJwtBuilder(subject,null,getUUID());
        return builder.compact();
    }
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String subject,Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject,ttlMillis,getUUID());
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param id
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String id,String subject,Long ttlMillis){
        JwtBuilder builder = getJwtBuilder(subject,ttlMillis,id);
        return builder.compact();
    }


    private static JwtBuilder getJwtBuilder(String subject,Long ttlMillis,String uuid){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.ES256;
        SecretKey secretKey = generalkey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis +ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)            //唯一的id
                .setSubject(subject)    //主题 可以使json数据
                .setIssuer("xssdhr")    //签发人
                .setIssuedAt(now)       //签发时间
                .signWith(signatureAlgorithm,secretKey) //使用HS256对称加密算法签名，第二个参数为秘钥
                .setExpiration(expDate);
    }

    public static void main(String[] args) throws Exception{
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdHkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzZyIsImlzcyI6InNnIiwiaWFOIjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * 生成加密后的秘钥secretKey
     */
    public static SecretKey generalkey(){
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }


    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception{
        SecretKey secretKey = generalkey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
