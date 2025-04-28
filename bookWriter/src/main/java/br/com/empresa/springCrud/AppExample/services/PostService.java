package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    List<Post> findAll();

    Page<Post> findAllPaged(Pageable pageable);

    Optional<Post> findById(UUID id);

    Post save(Post post);

    void deleteById(UUID id);

    Post partialUpdate(UUID id, Map<String, Object> updates);

    boolean existsById(UUID id);
}
