package br.com.empresa.springCrud.AppExample.controllers.hateoas;


import br.com.empresa.springCrud.AppExample.domainmodel.Role;
import br.com.empresa.springCrud.AppExample.repositories.RoleRepository;
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
@RestController
@RequestMapping("/api/hateoas/roles")
@RequiredArgsConstructor
public class RoleHateoasController {

    private final RoleRepository roleRepository;

    @GetMapping("/{id}")
    public EntityModel<Role> getRole(@PathVariable UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(role,
                linkTo(methodOn(RoleHateoasController.class).getRole(id)).withSelfRel(),
                linkTo(methodOn(RoleHateoasController.class).getAllRoles()).withRel("all-roles")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<Role>> getAllRoles() {
        List<EntityModel<Role>> roles = roleRepository.findAll().stream()
                .map(role -> EntityModel.of(role,
                        linkTo(methodOn(RoleHateoasController.class).getRole(role.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(roles,
                linkTo(methodOn(RoleHateoasController.class).getAllRoles()).withSelfRel());
    }
}