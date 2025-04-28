package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfileApiController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public List<Profile> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Profile> findById(@PathVariable UUID id) {
        return profileService.findById(id);
    }

    @PostMapping
    public Profile save(@RequestBody Profile profile) {
        return profileService.save(profile);
    }

    @PutMapping("/{id}")
    public Profile update(@PathVariable UUID id, @RequestBody Profile profile) {
        profile.setId(id);
        return profileService.save(profile);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        profileService.deleteById(id);
    }
}
