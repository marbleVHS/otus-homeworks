package ru.otus.servlet;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

@SuppressWarnings({"squid:S1948"})
public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/clients");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
