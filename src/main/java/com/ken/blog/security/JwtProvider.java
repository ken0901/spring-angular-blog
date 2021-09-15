package com.ken.blog.security;

import com.ken.blog.exception.SpringBlogException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    /**
     * Asymmetric Encryption using Java keystore
     * keytool -genkey -alias springblog -keyalg RSA -keystore springblog.jks -keysize 2048
     */
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "777777".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringBlogException("Exception occurred while loading keystore");
        }

    }

    /**
     * @param authentication
     * @return JWT token
     */
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    /**
     * @return Key
     * @throws SpringBlogException
     */
    private Key getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "777777".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringBlogException("Exception occurred while retrieving public key from keystore");
        }
    }

    /**
     * @param jwt token
     * @return boolean true
     */
    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    /**
     * @return PublicKey
     * @throws SpringBlogException
     */
    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringBlogException("Exception occurred while retrieving public key from keystore");
        }
    }

    /**
     * @param jwt token
     * @return
     */
    public String getUsernameFromJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(jwt)
                .getBody();

        return claims.getSubject();
    }
}
