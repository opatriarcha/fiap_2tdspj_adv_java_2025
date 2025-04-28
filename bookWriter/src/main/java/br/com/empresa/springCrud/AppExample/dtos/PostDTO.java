package br.com.empresa.springCrud.AppExample.dtos;

import br.com.empresa.springCrud.AppExample.domainmodel.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PostDTO {

    private UUID id;

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
    private String title;

    @NotBlank(message = "O conteúdo é obrigatório")
    private String content;

    private UUID userId; // Apenas o id do User associado

    // Métodos de conversão

    public static PostDTO fromEntity(Post post) {
        if (post == null) {
            return null;
        }
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
        }
        return dto;
    }

    public static Post toEntity(PostDTO dto) {
        if (dto == null) {
            return null;
        }
        Post post = new Post();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        if (dto.getUserId() != null) {
            var user = new br.com.empresa.springCrud.AppExample.domainmodel.User();
            user.setId(dto.getUserId());
            post.setUser(user);
        }
        return post;
    }
}
