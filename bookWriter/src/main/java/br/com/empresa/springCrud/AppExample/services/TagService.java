package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TagService {
    List<Tag> findAll();

    Page<Tag> findAllPaged(Pageable pageable);

    Optional<Tag> findById(UUID id);

    Tag save(Tag tag);

    void deleteById(UUID id);

    Tag partialUpdate(UUID id, Map<String, Object> updates);

    boolean existsById(UUID id);
}
