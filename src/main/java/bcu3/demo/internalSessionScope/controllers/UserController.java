package bcu3.demo.internalSessionScope.controllers;

import bcu3.demo.internalSessionScope.entities.User;
import bcu3.demo.internalSessionScope.services.SecuritySessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    private final SecuritySessionContext securitySessionContext;

    @Autowired
    public UserController(SecuritySessionContext securitySessionContext) {
        this.securitySessionContext = securitySessionContext;
    }

    @PostMapping("signin")
    public void signin(@RequestBody User user) {
        securitySessionContext.saveUser(user);
    }

    @GetMapping("current")
    private User getUser() {
        return securitySessionContext.getUser();
    }
}
