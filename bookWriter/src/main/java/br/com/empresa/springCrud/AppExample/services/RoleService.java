package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {
    List<Role> findAll();

    Page<Role> findAllPaged(Pageable pageable);

    Optional<Role> findById(UUID id);

    Role save(Role role);

    void deleteById(UUID id);

    Role partialUpdate(UUID id, Map<String, Object> updates);

    boolean existsById(UUID id);
}
