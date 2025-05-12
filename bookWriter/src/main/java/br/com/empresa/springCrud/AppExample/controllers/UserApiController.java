package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.dtos.UserDTO;
import br.com.empresa.springCrud.AppExample.repositories.UserRepository;
import br.com.empresa.springCrud.AppExample.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
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


    //http://localhost:8080/api/users/paged-default?pageNumber=1&pageSize=10&sortField=name&sortDirection=asc
    @GetMapping("/paged-default")
    @Operation(summary = "Listar todos os usuarios com paginacao e ordenacao por campos distintos")
    public ResponseEntity<Page<UserDTO>> findAllPagedDefault( @RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam String sortField, String sortDirection     ) {
        Pageable pageable = null;
        List<String> possibleParameters = Arrays.asList("asc", "desc");
        if( sortDirection == null || sortDirection.isEmpty() || !possibleParameters.contains(sortDirection) )
            throw new IllegalArgumentException("Sorting Cannot be null or empty or outside domain asc / desc");

        if(sortDirection.equalsIgnoreCase("asc"))
            pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortField).ascending());
        else if(sortDirection.equalsIgnoreCase("desc"))
            pageable = PageRequest.of(pageNumber,pageSize, Sort.by(sortField).descending());

        Page<UserDTO> users = this.userService.findAllPaged( pageable)
                .map(UserDTO::fromEntity);
        return ResponseEntity.ok(users);
    }

    //http://localhost:8080/api/users/paged?page=0&size=1000&sort=name,desc
    @Operation(summary = "Listar usuarios com paginacao e ordenacao")
    @GetMapping("/paged")
    public ResponseEntity<Page<UserDTO>> findAllPaged( @Parameter(hidden = true) Pageable pageable ) {
        Page<UserDTO> result = this.userService.findAllPaged(pageable)
                .map(UserDTO::fromEntity);
        return ResponseEntity.ok(result);
    }



}
