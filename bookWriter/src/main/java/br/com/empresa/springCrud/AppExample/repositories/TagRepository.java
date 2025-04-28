package br.com.empresa.springCrud.AppExample.repositories;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>,
        QuerydslPredicateExecutor<Tag> {
}