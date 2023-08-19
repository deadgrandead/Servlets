package com.deadgrandead.servlet;

import com.deadgrandead.controller.PostController;
import com.deadgrandead.repository.PostRepository;
import com.deadgrandead.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            route(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void route(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var path = req.getRequestURI();
        final var method = req.getMethod();

        if (method.equals("GET") && path.equals("/api/posts")) {
            controller.all(resp);
        } else if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
            final var id = extractIdFromPath(path);
            controller.getById(id, resp);
        } else if (method.equals("POST") && path.equals("/api/posts")) {
            controller.save(req.getReader(), resp);
        } else if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
            final var id = extractIdFromPath(path);
            controller.removeById(id, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private long extractIdFromPath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
