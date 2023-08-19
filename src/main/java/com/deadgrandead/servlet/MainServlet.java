package com.deadgrandead.servlet;

import com.deadgrandead.controller.PostController;
import com.deadgrandead.repository.PostRepository;
import com.deadgrandead.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private static final String API_POSTS = "/api/posts";
    private static final String API_POSTS_WITH_ID = "/api/posts/\\d+";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";
    
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

        if (METHOD_GET.equals(method) && API_POSTS.equals(path)) {
            controller.all(resp);
        } else if (METHOD_GET.equals(method) && path.matches(API_POSTS_WITH_ID)) {
            final var id = extractIdFromPath(path);
            controller.getById(id, resp);
        } else if (METHOD_POST.equals(method) && API_POSTS.equals(path)) {
            controller.save(req.getReader(), resp);
        } else if (METHOD_DELETE.equals(method) && path.matches(API_POSTS_WITH_ID)) {
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
