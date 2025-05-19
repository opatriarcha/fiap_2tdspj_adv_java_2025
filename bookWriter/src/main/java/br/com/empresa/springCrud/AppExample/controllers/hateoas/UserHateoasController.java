package br.com.empresa.springCrud.AppExample.controllers.hateoas;

import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/hateoas/users")
@RequiredArgsConstructor
public class UserHateoasController {
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public EntityModel<User> getUser(@PathVariable UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(user,
                linkTo(methodOn(UserHateoasController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(ProfileHateoasController.class).getProfileByUser(id)).withRel("profile"),
                linkTo(methodOn(PostHateoasController.class).getPostsByUser(id)).withRel("posts")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserHateoasController.class).getUser(user.getId())).withSelfRel()))
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserHateoasController.class).getAllUsers()).withSelfRel());
    }
}