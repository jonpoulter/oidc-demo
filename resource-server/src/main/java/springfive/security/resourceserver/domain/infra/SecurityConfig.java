package springfive.security.resourceserver.domain.infra;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * Ensure all inbound requests are associated with the logicalglue.com group.
     * Created new Mapper entry with Token Claim Name 'groups'
     * Please see Clients -> oidc-demo -> Mappers -> Groups
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

       http.authorizeRequests()
               .anyRequest().access("principal?.claims['groups'].contains('/acme.com')")
               .and().oauth2ResourceServer().jwt().jwtAuthenticationConverter(grantedAuthoritiesExtractor());

    }

    Converter<Jwt, AbstractAuthenticationToken> grantedAuthoritiesExtractor() {
        return new GrantedAuthoritiesExtractor();
    }

    /**
     * Extracting Authorities based on KeyCloak's default client roles Token mapper.
     * This has Token Claim Name resource_access.${client_id}.roles
     * Please see Clients -> oidc-demo -> Mappers -> client roles
     */
    static class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {


        @Override
        protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Object resourceAccessObj = jwt.getClaims().get("resource_access");
            if (resourceAccessObj != null) {
                JSONObject jo = new JSONObject(resourceAccessObj.toString());
                JSONArray ja = jo.getJSONObject("oidc-demo").getJSONArray("roles");

                for (int i=0; i<ja.length(); i++) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + ja.getString(i)));
                }
            }

            return authorities;

        }
    }



}
