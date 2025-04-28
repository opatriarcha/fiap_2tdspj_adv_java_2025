package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import br.com.empresa.springCrud.AppExample.dtos.PostDTO;
import br.com.empresa.springCrud.AppExample.services.PostServiceImpl;

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
@RequestMapping("/api/posts")
public class PostController {

    private final PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAll() {
        List<PostDTO> posts = postService.findAll()
                .stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PostDTO>> findAllPaged(Pageable pageable) {
        Page<PostDTO> page = postService.findAllPaged(pageable)
                .map(PostDTO::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable UUID id) {
        return postService.findById(id)
                .map(post -> ResponseEntity.ok(PostDTO.fromEntity(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostDTO postDTO) {
        Post post = PostDTO.toEntity(postDTO);
        Post savedPost = postService.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(PostDTO.fromEntity(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable UUID id, @Valid @RequestBody PostDTO postDTO) {
        if (!postService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Post post = PostDTO.toEntity(postDTO);
        post.setId(id);
        Post updatedPost = postService.save(post);
        return ResponseEntity.ok(PostDTO.fromEntity(updatedPost));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (!postService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Post updatedPost = postService.partialUpdate(id, updates);
        return ResponseEntity.ok(PostDTO.fromEntity(updatedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!postService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
