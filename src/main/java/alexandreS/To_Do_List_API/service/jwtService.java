package alexandreS.To_Do_List_API.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class jwtService {

    private static final String SECRET_KEY = "a-string-secret-at-least-256-bits-long";

    private static final  long  TIME_MS = 10*60*1000;

    public  String gerarToken(Long id){
        return Jwts.builder().setSubject(Long.toString(id)).setIssuedAt(new Date()).
                setExpiration(new Date(System.currentTimeMillis()+TIME_MS)).
                signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256
                ).compact();
    }

    public  String extrairIdUsuario(String token){
        return  Jwts.parserBuilder().setSigningKey((SECRET_KEY.getBytes())).build().parseClaimsJws(token).getBody().getSubject();
    }

}
