package com.ems.servlet;

import com.ems.dao.UserDAO;
import com.ems.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (isBlank(username) || isBlank(password) || isBlank(email)) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=1");
            return;
        }

        try {
            Optional<User> user = userDAO.authenticate(username, password, email);
            if (user.isPresent()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.get().getId());
                session.setAttribute("username", user.get().getUsername());
                session.setAttribute("email", user.get().getEmail());
                response.sendRedirect(request.getContextPath() + "/employees/");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.html?error=1");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Login failed", e);
            response.sendRedirect(request.getContextPath() + "/login.html?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
