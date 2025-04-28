package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Role;
import br.com.empresa.springCrud.AppExample.dtos.RoleDTO;
import br.com.empresa.springCrud.AppExample.services.RoleService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> findAll() {
        List<RoleDTO> roles = roleService.findAll()
                .stream()
                .map(RoleDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<RoleDTO>> findAllPaged(Pageable pageable) {
        Page<RoleDTO> page = roleService.findAllPaged(pageable)
                .map(RoleDTO::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> findById(@PathVariable UUID id) {
        return roleService.findById(id)
                .map(role -> ResponseEntity.ok(RoleDTO.fromEntity(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO roleDTO) {
        Role role = RoleDTO.toEntity(roleDTO);
        Role savedRole = roleService.save(role);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRole.getId())
                .toUri();
        return ResponseEntity.created(location).body(RoleDTO.fromEntity(savedRole));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable UUID id, @Valid @RequestBody RoleDTO roleDTO) {
        if (!roleService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Role role = RoleDTO.toEntity(roleDTO);
        role.setId(id);
        Role updatedRole = roleService.save(role);
        return ResponseEntity.ok(RoleDTO.fromEntity(updatedRole));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleDTO> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (!roleService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Role updatedRole = roleService.partialUpdate(id, updates);
        return ResponseEntity.ok(RoleDTO.fromEntity(updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!roleService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
