package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.User;

import java.util.List;

public interface UserRepositoryCustom<User, UUID> {
    public User fetchByEmail(final String email);
}
