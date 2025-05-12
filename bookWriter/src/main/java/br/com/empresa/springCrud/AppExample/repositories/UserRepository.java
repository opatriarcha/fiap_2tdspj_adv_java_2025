package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.OrderItem;
import br.com.empresa.springCrud.AppExample.domainmodel.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>,
        QuerydslPredicateExecutor<User>,
        UserRepositoryCustom<User, UUID>{


    @Override
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<User> findAll();

    List<User> findByEmail(final String email);

}
