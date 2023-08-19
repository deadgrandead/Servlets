package com.deadgrandead.repository;

import com.deadgrandead.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long newId = currentId.incrementAndGet();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        } else {
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
                return post;
            } else {
                return null;
            }
        }
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
