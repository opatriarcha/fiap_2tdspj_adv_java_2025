package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.dtos.UserDTO;
import br.com.empresa.springCrud.AppExample.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UserApiController {

    private final UserServiceImpl userService;

    @Autowired
    public UserApiController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista completa de usuários")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = userService.findAll()
                .stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Listar usuários paginados", description = "Retorna uma página de usuários de acordo com os parâmetros de paginação")
    @GetMapping("/paged")
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable) {
        Page<UserDTO> usersPage = userService.findAllPaged(pageable)
                .map(UserDTO::fromEntity);
        return ResponseEntity.ok(usersPage);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário específico usando seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(
            @Parameter(description = "ID do usuário a ser buscado", required = true)
            @PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserDTO.fromEntity(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar um novo usuário", description = "Cria e retorna um novo usuário")
    @PostMapping
    public ResponseEntity<UserDTO> save(
            @Parameter(description = "Objeto de usuário a ser criado", required = true)
            @Valid @RequestBody UserDTO userDTO) {
        User user = UserDTO.toEntity(userDTO);
        User savedUser = userService.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(UserDTO.fromEntity(savedUser));
    }

    @Operation(summary = "Atualizar um usuário existente", description = "Atualiza completamente um usuário já existente")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "ID do usuário a ser atualizado", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Dados atualizados do usuário", required = true)
            @Valid @RequestBody UserDTO userDTO) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        User user = UserDTO.toEntity(userDTO);
        user.setId(id);
        User updatedUser = userService.save(user);
        return ResponseEntity.ok(UserDTO.fromEntity(updatedUser));
    }

    @Operation(summary = "Excluir um usuário", description = "Remove um usuário usando seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID do usuário a ser excluído", required = true)
            @PathVariable UUID id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
