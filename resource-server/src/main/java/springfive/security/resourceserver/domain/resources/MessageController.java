package springfive.security.resourceserver.domain.resources;

import net.minidev.json.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfive.security.resourceserver.domain.services.MessageService;
import springfive.security.resourceserver.domain.vo.Authority;
import springfive.security.resourceserver.domain.vo.Message;
import springfive.security.resourceserver.domain.vo.User;
import springfive.security.resourceserver.domain.vo.UserUserDetails;


import java.util.*;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/{id}")
    String message(@PathVariable("id") Long id, Authentication authentication) {

        Optional<Message> message = messageService.getMessage(id);

        return message.isPresent() ? message.toString() : "Message Not Found!!";

    }

    @GetMapping("/my-message/{id}")
    String message(@PathVariable("id") Long id) {

        Optional<Message> message = messageService.getMyMessage(id);

        return message.isPresent() ? message.toString() : "Message Not Found!!";
    }


    @GetMapping("/foo")
    Map<String, String>  message(@AuthenticationPrincipal Jwt jwt) {

        List<Authority> authorities = new ArrayList<>();

        JSONObject jo = new JSONObject(jwt.getClaims().get("resource_access").toString());
        JSONArray ja = jo.getJSONObject("oidc-demo").getJSONArray("roles");

        for (int i=0; i<ja.length(); i++) {
            authorities.add(new Authority(ja.getString(i)));
        }

        //Create
        User user = new User();
        user.setEmail(jwt.getClaims().get("email").toString());
        user.setAuthorities(authorities);
        UserDetails userDetails = new UserUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        messageService.getMessage(1L);

        return Collections.singletonMap("text", String.format("Hello %s.  Your roles are %s",
                                        jwt.getClaims().get("email"),
                                        jwt.getClaims().get("resource_access")));



    }
}
