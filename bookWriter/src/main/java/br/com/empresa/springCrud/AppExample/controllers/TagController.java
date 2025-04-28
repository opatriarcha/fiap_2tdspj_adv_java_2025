package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import br.com.empresa.springCrud.AppExample.dtos.TagDTO;
import br.com.empresa.springCrud.AppExample.services.TagService;

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
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> findAll() {
        List<TagDTO> tags = tagService.findAll()
                .stream()
                .map(TagDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TagDTO>> findAllPaged(Pageable pageable) {
        Page<TagDTO> page = tagService.findAllPaged(pageable)
                .map(TagDTO::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> findById(@PathVariable UUID id) {
        return tagService.findById(id)
                .map(tag -> ResponseEntity.ok(TagDTO.fromEntity(tag)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagDTO tagDTO) {
        Tag tag = TagDTO.toEntity(tagDTO);
        Tag savedTag = tagService.save(tag);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();
        return ResponseEntity.created(location).body(TagDTO.fromEntity(savedTag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> update(@PathVariable UUID id, @Valid @RequestBody TagDTO tagDTO) {
        if (!tagService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Tag tag = TagDTO.toEntity(tagDTO);
        tag.setId(id);
        Tag updatedTag = tagService.save(tag);
        return ResponseEntity.ok(TagDTO.fromEntity(updatedTag));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagDTO> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        if (!tagService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Tag updatedTag = tagService.partialUpdate(id, updates);
        return ResponseEntity.ok(TagDTO.fromEntity(updatedTag));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!tagService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
