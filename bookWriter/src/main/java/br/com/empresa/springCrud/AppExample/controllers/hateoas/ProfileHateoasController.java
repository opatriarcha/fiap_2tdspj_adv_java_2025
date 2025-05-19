package br.com.empresa.springCrud.AppExample.controllers.hateoas;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import br.com.empresa.springCrud.AppExample.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/hateoas/profiles")
@RequiredArgsConstructor
public class ProfileHateoasController {

    private final ProfileRepository profileRepository;

    @GetMapping("/user/{userId}")
    public EntityModel<Profile> getProfileByUser(@PathVariable UUID userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(profile,
                linkTo(methodOn(ProfileHateoasController.class).getProfileByUser(userId)).withSelfRel(),
                linkTo(methodOn(UserHateoasController.class).getUser(userId)).withRel("user")
        );
    }
}