package com.sae.event.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by ralmeida on 10/21/15.
 */
public class Token {
    private String token;

    @JsonProperty
    public String getToken() {
        return token;
    }

    @JsonProperty
    public void setToken(String token) {
        this.token = token;
    }

    public static Token buildNewToken(){

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();

            Token token = new Token();
            token.setToken(new String(Hex.encodeHex(secretKey.getEncoded())));
            return token;

        }
        catch (Exception ex){
        }

        return null;
    }
}
