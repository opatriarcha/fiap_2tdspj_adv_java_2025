package br.com.empresa.springCrud.AppExample.controllers.hateoas;

import br.com.empresa.springCrud.AppExample.controllers.TagController;
import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import br.com.empresa.springCrud.AppExample.repositories.TagRepository;
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
@RequestMapping("/api/hateoas/tags")
@RequiredArgsConstructor
public class TagHateoasController {

    private final TagRepository tagRepository;

    @GetMapping("/{id}")
    public EntityModel<Tag> getTag(@PathVariable UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(tag,
                linkTo(methodOn(TagHateoasController.class).getTag(id)).withSelfRel());
    }

    @GetMapping("/post/{postId}")
    public CollectionModel<EntityModel<Tag>> getTagsByPost(@PathVariable UUID postId) {
        List<EntityModel<Tag>> tags = tagRepository.findByPostsId(postId).stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagHateoasController.class).getTag(tag.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(tags,
                linkTo(methodOn(TagHateoasController.class).getTagsByPost(postId)).withSelfRel());
    }
}
