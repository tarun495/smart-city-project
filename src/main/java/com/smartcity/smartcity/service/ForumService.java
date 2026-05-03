package com.smartcity.smartcity.service;

import com.smartcity.smartcity.model.ForumPost;
import com.smartcity.smartcity.model.User;
import com.smartcity.smartcity.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private UserService userService;

    // Get all top-level posts (not replies)
    public List<ForumPost> getAllTopLevelPosts() {
        return forumRepository.findByParentPostIsNull();
    }

    public List<ForumPost> getPostsByCategory(String category) {
        return forumRepository.findByCategory(category);
    }

    public ForumPost getPostById(Long id) {
        return forumRepository.findById(id).orElse(null);
    }

    public ForumPost createPost(ForumPost post, String username) {
        User author = userService.findByUsername(username).orElse(null);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
        return forumRepository.save(post);
    }

    // Reply to a post
    public ForumPost replyToPost(Long parentId, ForumPost reply, String username) {
        ForumPost parent = getPostById(parentId);
        User author = userService.findByUsername(username).orElse(null);
        reply.setParentPost(parent);
        reply.setAuthor(author);
        reply.setCreatedAt(LocalDateTime.now());
        return forumRepository.save(reply);
    }

    public void deletePost(Long id) {
        forumRepository.deleteById(id);
    }
}