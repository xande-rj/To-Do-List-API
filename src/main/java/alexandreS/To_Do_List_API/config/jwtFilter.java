package alexandreS.To_Do_List_API.config;

import alexandreS.To_Do_List_API.service.jwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class jwtFilter extends OncePerRequestFilter {

private final jwtService jwtService;
public jwtFilter(jwtService jwtService){
    this.jwtService = jwtService;
}
@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

        if(header!=null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            String id = jwtService.extrairIdUsuario(token);
            var auth = new UsernamePasswordAuthenticationToken(id,null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filter.doFilter(request,response);

    }


}
