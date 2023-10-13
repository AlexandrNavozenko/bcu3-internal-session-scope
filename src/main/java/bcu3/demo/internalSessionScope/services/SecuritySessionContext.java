package bcu3.demo.internalSessionScope.services;

import bcu3.demo.internalSessionScope.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

//@Service
//@SessionScope
public class SecuritySessionContext {

    private User user;

    public void saveUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
