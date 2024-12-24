package com.ghostnet.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/secured/*") // Filter für alle Seiten im secured-Ordner
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisierung falls erforderlich
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Überprüfen, ob der Benutzer in der Sitzung gespeichert ist
        Object user = req.getSession().getAttribute("user");
        if (user == null) {
            // Kein Benutzer eingeloggt, Umleitung zur Login-Seite
            System.out.println("Benutzer ist nicht eingeloggt. Umleitung zur Login-Seite.");
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            // Benutzer ist eingeloggt, Anfrage fortsetzen
            System.out.println("Benutzer ist eingeloggt: " + user);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Ressourcen freigeben falls erforderlich
    }
}
