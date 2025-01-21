package App.security;

import App.jooq.tables.Users;
import App.model.User;
import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DSLContext dslContext;


    public CustomUserDetailsService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        var userRecord = dslContext.selectFrom(Users.USERS)
//                .where(Users.USERS.NAME.eq(username))
//                .fetchOne();
//        if (userRecord == null){
//            throw new UsernameNotFoundException(username);
//        }
//
//        return new CustomUserDetails(new User(userRecord.getName(),userRecord.getPassword()));
        return null;
    }
}
