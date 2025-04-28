package br.com.empresa.springCrud.AppExample.controllers;

import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.services.UserService;
import br.com.empresa.springCrud.AppExample.services.UserServiceImpl;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
//@Tag(name = "User", description = "Operações CRUD para usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
////    @Operation(summary = "Listar todos os usuários",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
//            })
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
//    @Operation(summary = "Buscar usuário por ID",
//            parameters = {
//                    @Parameter(name = "id", description = "ID do usuário", required = true)
//            })
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
//    @Operation(summary = "Criar um novo usuário")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
//    @Operation(summary = "Atualizar um usuário completamente")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.save(user));
    }

    @PatchMapping("/{id}")
//    @Operation(summary = "Atualizar parcialmente um usuário")
    public ResponseEntity<User> partialUpdate(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
//        return ResponseEntity.ok(userService.partialUpdate(id, updates));
    return null;
    }

    @DeleteMapping("/{id}")
//    @Operation(summary = "Excluir um usuário")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
