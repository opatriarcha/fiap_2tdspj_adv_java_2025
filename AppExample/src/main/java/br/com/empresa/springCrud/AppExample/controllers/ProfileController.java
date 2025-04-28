package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profile", description = "Operações CRUD para perfis")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @Operation(summary = "Listar todos os perfis")
    public List<Profile> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil por ID")
    public ResponseEntity<Profile> findById(@PathVariable UUID id) {
        return profileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo perfil")
    public Profile create(@RequestBody Profile profile) {
        return profileService.save(profile);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um perfil completamente")
    public ResponseEntity<Profile> update(@PathVariable UUID id, @RequestBody Profile profile) {
        profile.setId(id);
        return ResponseEntity.ok(profileService.save(profile));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um perfil")
    public ResponseEntity<Profile> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(profileService.partialUpdate(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um perfil")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        profileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}