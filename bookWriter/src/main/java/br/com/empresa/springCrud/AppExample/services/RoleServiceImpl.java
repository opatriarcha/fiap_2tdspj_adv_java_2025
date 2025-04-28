package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Role;
import br.com.empresa.springCrud.AppExample.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> findAllPaged(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(UUID id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role partialUpdate(UUID id, Map<String, Object> updates) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        role.setName((String) value);
                        break;
                    // outros campos se necessário no futuro
                }
            });
            return roleRepository.save(role);
        }
        return null; // Melhor ainda seria lançar ResourceNotFoundException
    }

    @Override
    public boolean existsById(UUID id) {
        return roleRepository.existsById(id);
    }
}
