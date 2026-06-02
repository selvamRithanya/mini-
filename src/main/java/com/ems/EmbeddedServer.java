package com.ems;

import com.ems.filter.AuthFilter;
import com.ems.servlet.EmployeeServlet;
import com.ems.servlet.LoginServlet;
import com.ems.servlet.LogoutServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import jakarta.servlet.DispatcherType;

/**
 * Embedded Jetty server — used by start-server.bat (no Maven required).
 */
public class EmbeddedServer {

    private static final int PORT = 8082;

    public static void main(String[] args) throws Exception {
        String projectDir = System.getProperty("user.dir");
        String webappDir = projectDir + File.separator + "src" + File.separator + "main" + File.separator + "webapp";
        String classesDir = projectDir + File.separator + "target" + File.separator + "classes";

        ClassLoader appLoader = buildClassLoader(classesDir);
        Thread.currentThread().setContextClassLoader(appLoader);

        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(webappDir);
        context.setClassLoader(appLoader);

        ServletHolder defaultServlet = new ServletHolder("default", DefaultServlet.class);
        defaultServlet.setInitParameter("dirAllowed", "false");
        context.addServlet(defaultServlet, "/");

        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(LogoutServlet.class, "/logout");
        context.addServlet(EmployeeServlet.class, "/employees/*");

        FilterHolder authFilter = new FilterHolder(AuthFilter.class);
        context.addFilter(authFilter, "/employees/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(authFilter, "/dashboard.html", EnumSet.of(DispatcherType.REQUEST));

        server.setHandler(context);
        server.start();

        System.out.println("============================================");
        System.out.println(" Server running at http://localhost:" + PORT);
        System.out.println(" Login:  http://localhost:" + PORT + "/login.html");
        System.out.println(" Press Ctrl+C to stop.");
        System.out.println("============================================");
        server.join();
    }

    private static ClassLoader buildClassLoader(String classesDir) throws Exception {
        File libDir = new File("lib");
        List<URL> urls = new ArrayList<>();
        urls.add(new File(classesDir).toURI().toURL());
        if (libDir.isDirectory()) {
            File[] jars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jars != null) {
                for (File jar : jars) {
                    urls.add(jar.toURI().toURL());
                }
            }
        }
        return new URLClassLoader(urls.toArray(URL[]::new), EmbeddedServer.class.getClassLoader());
    }
}
