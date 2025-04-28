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
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Page<Post> findAllPaged(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        postRepository.deleteById(id);
    }

    @Override
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
        return null; // Ideal seria lançar ResourceNotFoundException
    }

    @Override
    public boolean existsById(UUID id) {
        return postRepository.existsById(id);
    }
}
