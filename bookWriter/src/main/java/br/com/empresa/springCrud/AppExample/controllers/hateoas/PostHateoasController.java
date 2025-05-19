package br.com.empresa.springCrud.AppExample.controllers.hateoas;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import br.com.empresa.springCrud.AppExample.repositories.PostRepository;
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
@RequestMapping("/api/hateoas/posts")
@RequiredArgsConstructor
public class PostHateoasController {

    private final PostRepository postRepository;

    @GetMapping("/{id}")
    public EntityModel<Post> getPost(@PathVariable UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(post,
                linkTo(methodOn(PostHateoasController.class).getPost(id)).withSelfRel(),
                linkTo(methodOn(UserHateoasController.class).getUser(post.getUser().getId())).withRel("user"),
                linkTo(methodOn(TagHateoasController.class).getTagsByPost(id)).withRel("tags")
        );
    }

    @GetMapping("/user/{userId}")
    public CollectionModel<EntityModel<Post>> getPostsByUser(@PathVariable UUID userId) {
        List<EntityModel<Post>> posts = postRepository.findByUserId(userId).stream()
                .map(post -> EntityModel.of(post,
                        linkTo(methodOn(PostHateoasController.class).getPost(post.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return CollectionModel.of(posts,
                linkTo(methodOn(PostHateoasController.class).getPostsByUser(userId)).withSelfRel());
    }
}