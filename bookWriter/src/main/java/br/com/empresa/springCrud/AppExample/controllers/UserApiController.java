package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.dtos.UserDTO;
import br.com.empresa.springCrud.AppExample.repositories.UserRepository;
import br.com.empresa.springCrud.AppExample.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UserApiController {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @Autowired
    public UserApiController(UserServiceImpl userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Listar todos os usuários")
    @GetMapping
    @Cacheable(value = "usersCache", key = "'findAll'")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Listar usuários paginados")
    @GetMapping("/paged")
    @Cacheable(value = "usersCache", key = "'findAllPaged-' + #pageable.pageNumber")
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable) {
        Page<UserDTO> usersPage = userService.findAllPaged(pageable)
                .map(UserDTO::fromEntity);
        return ResponseEntity.ok(usersPage);
    }

    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{id}")
    @Cacheable(value = "usersCache", key = "#id")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserDTO.fromEntity(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar novo usuário")
    @PostMapping
    @CachePut(value = "usersCache", key = "#result.body.id")
    public ResponseEntity<UserDTO> save(@Valid @RequestBody UserDTO userDTO) {
        User user = UserDTO.toEntity(userDTO);
        User savedUser = userService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(UserDTO.fromEntity(savedUser));
    }

    @Operation(summary = "Atualizar usuário")
    @PutMapping("/{id}")
    @CachePut(value = "usersCache", key = "#id")
    public ResponseEntity<UserDTO> update(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        User user = UserDTO.toEntity(userDTO);
        user.setId(id);
        User updatedUser = userService.save(user);
        return ResponseEntity.ok(UserDTO.fromEntity(updatedUser));
    }

    @Operation(summary = "Deletar usuário")
    @DeleteMapping("/{id}")
    @CacheEvict(value = "usersCache", key = "#id")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test-cache")
    public ResponseEntity<String> testCache() {
        long start = System.currentTimeMillis();
        List<User> users = userRepository.findAll();
        long end = System.currentTimeMillis();

        long elapsed = end - start;
        System.out.println("Tempo de execução: " + elapsed + " ms ({} usuários)");

        return ResponseEntity.ok("Executado em " + elapsed + " ms " + users.size() + "usuários encontrados: "  );
    }
}
