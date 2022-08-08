package com.riven.service.token;
import com.riven.constant.Constants;
import com.riven.model.LoginUser;
import com.riven.util.RedisCache;
import com.riven.util.StringUtils;
import com.riven.util.uuid.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token操作工具类
 */
@Service
public class TokenService {
    //令牌自定义标识
    @Value("${token.header}")
    private String header;
    //令牌密钥
    @Value("${token.secret}")
    private String secret;
    //令牌有效期(默认30分钟)
    @Value("${token.expireTime}")
    private int expireTime;
    @Resource
    private RedisCache redisCache;
    //一秒
    protected static final long MILLIS_SECOND = 1000;
    //一分钟
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    //20分钟
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;
/*    *//**
     * 获取加密算法
     * @return
     * @throws UnsupportedEncodingException
     *//*
    private Algorithm getSAlgorithm() throws UnsupportedEncodingException {
        return Algorithm.HMAC256("secret");
    }*/

/*    *//**
     * 生成token
     * @param authentication
     * @return
     *//*
    public String getToken(Authentication authentication){
        String token=null;
        //设置过期时间1分钟
        Date date = new Date(System.currentTimeMillis() + 600000);
        try {
            token= JWT.create()
                    .withIssuer("zjl11")
                    .withExpiresAt(date)
                    .withClaim("username",authentication.getName())
                    .sign(this.getSAlgorithm());

        }catch (JWTCreationException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        saveToken(token);
        return token;
    }*/

/*    *//**
     * 缓存Token
     * @param token
     *//*
    public void saveToken(String token){
        redisCache.setCacheObject("token",token);
    }*/

/*    *//**
     * 验证token合法性
     * @param token
     * @return
     *//*
    public boolean Verified(String token){
        try {
            JWTVerifier jwtVerifier=JWT.require(this.getSAlgorithm())
                    .withIssuer("zjl11")
                    .build();
            DecodedJWT verify = jwtVerifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }catch (JWTVerificationException e){
            e.printStackTrace();
            return false;
        }

    }*/

    /**
     * 生成token
     * 存储的是登录用户的唯一标识uuid，登录用户则存于缓存中
     * @param loginUser
     * @return
     */
    public String createToken(LoginUser loginUser){
        //为登录用户生成唯一标识uuid
        String uuid = IdUtils.fastUUID();
        loginUser.setUuId(uuid);
        //设置登录时间以及token有效期，并将loginUser存入缓存
        refreshToken(loginUser);
        //定义数据声明，将uuid存入
        Map<String,Object> claims=new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY,uuid);
        //从数据声明生成token
        String token=Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512,secret).compact();
        return token;
    }

    /**
     * 获取登录用户身份信息
     * @param request
     * @return
     */
    public LoginUser getLoginUser(HttpServletRequest request){
        //从请求头中获取token
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)){
            //解析token，获取数据声明
            Claims claims = parseToken(token);
            //从数据声明中解析出其携带的uuid
            String uuid= getUUid(claims);
            //拼接uuid获取缓存中loginUser对应的key
            String tokenKey = getTokenKey(uuid);
            //通过key从缓存中取出loginUser对象
            LoginUser loginUser = redisCache.getCacheObject(tokenKey);
            return loginUser;
        }
        return null;

    }

    /**
     * 从数据声明中解析出uuid
     * @param claims
     * @return
     */
    public String getUUid(Claims claims){
        return (String) claims.get(Constants.LOGIN_USER_KEY);
    }

    /**
     * 获取请求头携带的token
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request){
        String token = request.getHeader(this.header);
        if(StringUtils.isNotEmpty(token)&&token.startsWith(Constants.TOKEN_PREFIX)){
            token=token.replace(Constants.TOKEN_PREFIX,"");
        }
        return token;
    }

    /**
     * 解析token，从其中获取数据声明
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 拼接获取缓存对象loginUser对应的key
     * @param uuid
     * @return
     */
    public String getTokenKey(String uuid){
        return Constants.LOGIN_USER_KEY+uuid;
    }

    /**
     * 验证token有效期(是否过期)
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser){
        Long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime-currentTime<=MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌的有效期
     * @param loginUser
     */
    public void refreshToken(LoginUser loginUser){
        //重置登录时间与token有效期
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime()+expireTime*MILLIS_MINUTE);
        //根据token解析出uuid，拼接成存入缓存所对应的key
        String tokenKey = getTokenKey(loginUser.getUuId());
        //loginUser存入缓存
        redisCache.setCacheObject(tokenKey,loginUser,expireTime, TimeUnit.MINUTES);
    }

    /**
     * 删除用户身份信息
     * @param loginUser
     */
    public void deleteLoginUser(LoginUser loginUser){
        if (StringUtils.isNotNull(loginUser)){
            String uuId = loginUser.getUuId();
            String tokenKey = getTokenKey(uuId);
            redisCache.deleteObject(tokenKey);
        }
    }



















}
