package springfive.security.resourceserver.domain.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springfive.security.resourceserver.domain.repositories.UserRepository;
import springfive.security.resourceserver.domain.vo.User;
import springfive.security.resourceserver.domain.vo.UserUserDetails;

@Service("userService")
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserRepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(username);

        if (null != user) {
            //adapt it to create UserDetails Object
            return new UserUserDetails(user);
        } else {
            throw new UsernameNotFoundException(String.format("could not find username $s", username ));
        }
    }
}
