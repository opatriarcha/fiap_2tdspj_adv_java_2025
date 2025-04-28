package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Optional<Profile> findById(UUID id) {
        return profileRepository.findById(id);
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public void deleteById(UUID id) {
        profileRepository.deleteById(id);
    }

    public Profile partialUpdate(UUID id, Map<String, Object> updates) {
        Optional<Profile> profileOptional = profileRepository.findById(id);
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "bio":
                        profile.setBio((String) value);
                        break;
                    case "profilePictureUrl":
                        profile.setProfilePictureUrl((String) value);
                        break;
                }
            });
            return profileRepository.save(profile);
        }
        return null; // Ou lançar uma exceção
    }
}