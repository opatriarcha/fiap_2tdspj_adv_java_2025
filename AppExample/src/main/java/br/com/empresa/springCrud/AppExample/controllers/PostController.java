package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import br.com.empresa.springCrud.AppExample.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "Operações CRUD para posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    @Operation(summary = "Listar todos os posts")
    public List<Post> findAll() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar post por ID")
    public ResponseEntity<Post> findById(@PathVariable UUID id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar um novo post")
    public Post create(@RequestBody Post post) {
        return postService.save(post);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um post completamente")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody Post post) {
        post.setId(id);
        return ResponseEntity.ok(postService.save(post));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um post")
    public ResponseEntity<Post> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(postService.partialUpdate(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um post")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAllPosts(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String tagName,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {

        if (title != null) {
            return ResponseEntity.ok(postService.findByTitle(title, pageable));
        } else if (userId != null) {
            return ResponseEntity.ok(postService.findByUserId(userId, pageable));
        } else if (tagName != null) {
            return ResponseEntity.ok(postService.findByTagName(tagName, pageable));
        } else {
            return ResponseEntity.ok(postService.findAll(pageable));
        }
    }
}