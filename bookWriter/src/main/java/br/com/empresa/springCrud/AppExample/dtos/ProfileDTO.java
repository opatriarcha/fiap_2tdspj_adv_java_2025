package br.com.empresa.springCrud.AppExample.dtos;

import br.com.empresa.springCrud.AppExample.domainmodel.Profile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileDTO {

    private UUID id;

    @NotBlank(message = "A biografia é obrigatória")
    @Size(max = 500, message = "A biografia deve ter no máximo 500 caracteres")
    private String bio;

    @Size(max = 255, message = "A URL da foto de perfil deve ter no máximo 255 caracteres")
    private String profilePictureUrl;

    private UUID userId; // Vamos mapear apenas o ID do usuário

    // Métodos de conversão

    public static ProfileDTO fromEntity(Profile profile) {
        if (profile == null) {
            return null;
        }
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setBio(profile.getBio());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
        }
        return dto;
    }

    public static Profile toEntity(ProfileDTO dto) {
        if (dto == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(dto.getId());
        profile.setBio(dto.getBio());
        profile.setProfilePictureUrl(dto.getProfilePictureUrl());
        // ATENÇÃO: Aqui não setamos o User completo, apenas o ID
        if (dto.getUserId() != null) {
            var user = new br.com.empresa.springCrud.AppExample.domainmodel.User();
            user.setId(dto.getUserId());
            profile.setUser(user);
        }
        return profile;
    }
}
