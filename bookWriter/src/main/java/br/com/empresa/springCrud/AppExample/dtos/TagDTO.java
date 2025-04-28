package br.com.empresa.springCrud.AppExample.dtos;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TagDTO {

    private UUID id;

    @NotBlank(message = "O nome da tag é obrigatório")
    @Size(max = 100, message = "O nome da tag deve ter no máximo 100 caracteres")
    private String name;

    // Métodos de conversão

    public static TagDTO fromEntity(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }

    public static Tag toEntity(TagDTO dto) {
        if (dto == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(dto.getId());
        tag.setName(dto.getName());
        return tag;
    }
}
