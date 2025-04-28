package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    @Override
    public Page<Profile> findAllPaged(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }

    @Override
    public Optional<Profile> findById(UUID id) {
        return profileRepository.findById(id);
    }

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public void deleteById(UUID id) {
        profileRepository.deleteById(id);
    }

    @Override
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
                    // Se quiser permitir atualizar mais campos depois, só adicionar aqui
                }
            });
            return profileRepository.save(profile);
        }
        return null; // Melhorar: pode lançar ResourceNotFoundException se quiser
    }


    @Override
    public boolean existsById(UUID id) {
        return profileRepository.existsById(id);
    }
}
