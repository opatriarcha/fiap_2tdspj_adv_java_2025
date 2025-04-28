package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProfileService {
    List<Profile> findAll();

    Page<Profile> findAllPaged(Pageable pageable);

    Optional<Profile> findById(UUID id);

    Profile save(Profile profile);

    void deleteById(UUID id);

    Profile partialUpdate(UUID id, Map<String, Object> updates);

    boolean existsById(UUID id);
}
