package bcu3.demo.internalSessionScope.services;

import bcu3.demo.internalSessionScope.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Service
public class SecuritySessionContextProxy extends SecuritySessionContext {

    private static final String SCOPED_TARGET_PARAM_NAME = "scopedTarget.SecuritySessionContext";

    @Override
    public void saveUser(User user) {
        getDelegate().saveUser(user);
    }

    @Override
    public User getUser() {
        return getDelegate().getUser();
    }

    private SecuritySessionContext getDelegate() {
        RequestContextHolder.getRequestAttributes();
        var session = getSession((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        return Optional.ofNullable(session.getAttribute(SCOPED_TARGET_PARAM_NAME))
                .map(attribute -> castAttribute(attribute, SecuritySessionContext.class))
                .orElseGet(() -> createSecuritySessionContext(SecuritySessionContext.class, session));
    }

    private <T extends SecuritySessionContext> T createSecuritySessionContext(Class<T> clazz, HttpSession session) {
        try {
            var context = clazz.getDeclaredConstructor().newInstance();
            session.setAttribute(SCOPED_TARGET_PARAM_NAME, context);

            return context;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            System.out.println("Exception: " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private HttpSession getSession(ServletRequestAttributes attributes) {
        return Optional.ofNullable(attributes)
                .map(ServletRequestAttributes::getRequest)
                .map(HttpServletRequest::getSession)
                .orElseThrow();
    }

    private <T extends SecuritySessionContext> T castAttribute(Object attribute, Class<T> clazz) {
        return clazz.cast(attribute);
    }
}
