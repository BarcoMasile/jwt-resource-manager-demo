package com.hpc.relearn.demo.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
public class JWTProvider {

    private final JwtEncoder encoder;
    private final String publicKey = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGYLQZgd2oGRlPwATU8EQTVl7G7g\n" +
            "RPDWREA+0pQyb4Rlp8jtSNBRcT/J9FDfkbtf10KZqu48qU4HIK7Dh69KxBCp2le4\n" +
            "JynwhcZmguJK6TPyhJv627syxjUNDD19vCcnVT/zoMz4a83KqVInX6YhpgPpqdYY\n" +
            "CxTw7+uDfD2X9vjDAgMBAAE=";

    private final String privateKey = "MIICWgIBAAKBgGYLQZgd2oGRlPwATU8EQTVl7G7gRPDWREA+0pQyb4Rlp8jtSNBR\n" +
            "cT/J9FDfkbtf10KZqu48qU4HIK7Dh69KxBCp2le4JynwhcZmguJK6TPyhJv627sy\n" +
            "xjUNDD19vCcnVT/zoMz4a83KqVInX6YhpgPpqdYYCxTw7+uDfD2X9vjDAgMBAAEC\n" +
            "gYAaMbOBz9ACSJOc6JrvuKDgiyxY/drUk7vrXfUCM7wqmUmrqzVwvCc8/6Nmiy1G\n" +
            "kwCXyQh2pT20mOHL2sdeuOuBI6UvsG5EFqDylW3y8CZhHxIb9jfIwGFZT4dTLDCk\n" +
            "ZuCO4l79zvzSAjDYPNxqfrif+PK39yOyXLY4gPjIiNHHeQJBALtmzyqF1A5DK4XN\n" +
            "WExwqD+nItsSEALTS8pSSAc+r6VjymbF6ZIg0Am4wD82jYqrB5c0ViR0X2iCwPrH\n" +
            "zPq1bR0CQQCLZa48rNENz7oFkoyIp20LVbRIri/RhjQpcNwSl3LrVqw9/GD1ILe9\n" +
            "JyHtcjmMW2Bd7Bq+YQugP29b1wlHp3dfAkA0OEPGH6ClkMYR9NSECGh5xEMMNI5N\n" +
            "mz9lU2RK0Ib1WUdPOBsL4yTVcKvxcWwjleD5m1XdtqfXYYgQ2ZgezGtpAkA1ul9J\n" +
            "66I4HJSqiS25ji6ta78tNFYiAAQi5OkuEUmuA54n1hrY4F9xfJ/LEXe07ZNbICG+\n" +
            "t1Dv1wNo9p0RDS8pAkA2USodtrY84jW0fOzuJ8y2OICHK1rDhNVxM85+BSCXV9hb\n" +
            "fdWxtXXffOeMA6D7AA5RdwEGbXxn+0iYBHcFI9qq";

    @Autowired
    private JwtDecoder decoder;

    public JWTProvider() {
        ImmutableSecret<SecurityContext> immutableSecret = new ImmutableSecret<>(privateKey.getBytes());
        encoder = new NimbusJwtEncoder(immutableSecret);
    }

    public Jwt generateToken() {
        JwsHeader headers = JwsHeader.with(SignatureAlgorithm.RS256)
                .type("JWT")
                .keyId("enginframe")
                .build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://enginframe.com")
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .audience(Collections.singletonList("EnginFrame"))
                .subject("EnginFrameUser")
                .build();

        Jwt jwt = encoder.encode(JwtEncoderParameters.from(headers, claims));

        return jwt;
    }

    public JWTClaimsSet parse(String token) {
        ConfigurableJWTProcessor<SimpleSecurityContext> processor = new DefaultJWTProcessor<>();
        ImmutableSecret<SimpleSecurityContext> immutableSecret = new ImmutableSecret<>(privateKey.getBytes());
        JWSAlgorithm alg = JWSAlgorithm.RS256;
        JWSVerificationKeySelector<SimpleSecurityContext> keySelector = new JWSVerificationKeySelector<>(alg, immutableSecret);
        processor.setJWSKeySelector(keySelector);
        try {
            return processor.process(token, null);
        } catch (ParseException | BadJOSEException | JOSEException e) {
            return null;
        }
    }

    @PostConstruct
    void check() {
        Jwt jwt = generateToken();
    }
}
