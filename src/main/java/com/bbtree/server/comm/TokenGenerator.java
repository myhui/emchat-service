package com.bbtree.server.comm;

import com.alibaba.fastjson.JSONObject;
import com.bbtree.constants.Constant;
import com.bbtree.redis.RedisUtil;
import com.bbtree.server.api.AuthTokenAPI;
import com.bbtree.server.comm.wrapper.ResponseWrapper;
import com.bbtree.utils.HttpUtil;
import com.bbtree.utils.SpringUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    private String accessToken;
    private Long expiredAt = -1L;
    private ClientContext context;

    public TokenGenerator() {
    }

    public TokenGenerator(String accessToken, long expiredAt, ClientContext context) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
        this.context = context;
    }

    public TokenGenerator(ClientContext context) {
        this.context = context;
    }

    public String request(Boolean force) {
        force = (null == force) ? Boolean.FALSE : force;

        if (isExpired() || force) {

            if (SpringUtil.isDev()) {

                if (null == context || !context.isInitialized()) {
                    logger.error(MessageTemplate.INVAILID_CONTEXT_MSG);
                    throw new RuntimeException(MessageTemplate.INVAILID_CONTEXT_MSG);
                }

                AuthTokenAPI authService = (AuthTokenAPI) context.getAPIFactory().newInstance(EasemobRestAPIFactory.TOKEN_CLASS);
                String clientId = ClientContext.getInstance().getClientId();
                String clientSecret = ClientContext.getInstance().getClientSecret();
                ResponseWrapper response = (ResponseWrapper) authService.getAuthToken(clientId, clientSecret);

                if (null != response && 200 == response.getResponseStatus() && null != response.getResponseBody()) {
                    ObjectNode responseBody = (ObjectNode) response.getResponseBody();
                    String newToken = responseBody.get("access_token").asText();
                    Long newTokenExpire = responseBody.get("expires_in").asLong() * 1000;

                    logger.debug("New token: " + newToken);
                    logger.debug("New token expires: " + newTokenExpire);

                    this.accessToken = newToken;
                    this.expiredAt = System.currentTimeMillis() + newTokenExpire;
                    logger.info(MessageTemplate.print(MessageTemplate.REFRESH_TOKEN_MSG, new String[]{accessToken, expiredAt.toString()}));
                } else {
                    logger.error(MessageTemplate.REFRESH_TOKEN_ERROR_MSG);
                }

                logger.info("开发环境，获取Token: access_token = "+ this.accessToken+", expiredAt = "+this.expiredAt);

            } else {
                RedisUtil redisUtil = SpringUtil.getRedisBean();
                String token = redisUtil.getStr(Constant.TOKEN);
                token = token.replace("\\", "");
                JSONObject jsonToken = JSONObject.parseObject(token);
                if (jsonToken.getInteger("code") == 200) {
                    JSONObject body = jsonToken.getJSONObject("body");
                    String newToken = body.getString("access_token");
                    Long newTokenExpire = body.getLong("expires_in") * 1000;
                    this.accessToken = newToken;
                    this.expiredAt = System.currentTimeMillis() + newTokenExpire;
                }

                logger.info("从Redis获取Token: access_token = "+ this.accessToken+", expiredAt = "+this.expiredAt);

            }

        }

        return accessToken;
    }

    public Boolean isExpired() {
        return System.currentTimeMillis() > expiredAt;
    }

    public void setContext(ClientContext context) {
        this.context = context;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    @Override
    public String toString() {
        return accessToken;
    }

}
