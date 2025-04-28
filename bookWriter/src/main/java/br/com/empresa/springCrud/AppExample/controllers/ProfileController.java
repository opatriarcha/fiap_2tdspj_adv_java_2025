package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.dtos.ProfileDTO;
import br.com.empresa.springCrud.AppExample.services.ProfileService;

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
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> findAll() {
        List<ProfileDTO> profiles = profileService.findAll()
                .stream()
                .map(ProfileDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ProfileDTO>> findAllPaged(Pageable pageable) {
        Page<ProfileDTO> profilesPage = profileService.findAllPaged(pageable)
                .map(ProfileDTO::fromEntity);
        return ResponseEntity.ok(profilesPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> findById(@PathVariable UUID id) {
        return profileService.findById(id)
                .map(profile -> ResponseEntity.ok(ProfileDTO.fromEntity(profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfileDTO> create(@Valid @RequestBody ProfileDTO profileDTO) {
        Profile profile = ProfileDTO.toEntity(profileDTO);
        Profile savedProfile = profileService.save(profile);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProfile.getId())
                .toUri();
        return ResponseEntity.created(location).body(ProfileDTO.fromEntity(savedProfile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> update(@PathVariable UUID id, @Valid @RequestBody ProfileDTO profileDTO) {
        if (!profileService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Profile profile = ProfileDTO.toEntity(profileDTO);
        profile.setId(id);
        Profile updatedProfile = profileService.save(profile);
        return ResponseEntity.ok(ProfileDTO.fromEntity(updatedProfile));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfileDTO> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (!profileService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Profile updatedProfile = profileService.partialUpdate(id, updates);
        return ResponseEntity.ok(ProfileDTO.fromEntity(updatedProfile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!profileService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        profileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
