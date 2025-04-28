package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import br.com.empresa.springCrud.AppExample.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deleteById(UUID id) {
        postRepository.deleteById(id);
    }

    public Post partialUpdate(UUID id, Map<String, Object> updates) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "title":
                        post.setTitle((String) value);
                        break;
                    case "content":
                        post.setContent((String) value);
                        break;
                }
            });
            return postRepository.save(post);
        }
        return null; // Ou lançar uma exceção
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> findByTitle(String title, Pageable pageable) {
        return postRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Post> findByUserId(UUID userId, Pageable pageable) {
        return postRepository.findByUser_Id(userId, pageable);
    }

    public Page<Post> findByTagName(String tagName, Pageable pageable) {
        return postRepository.findByTags_NameContainingIgnoreCase(tagName, pageable);
    }
}
