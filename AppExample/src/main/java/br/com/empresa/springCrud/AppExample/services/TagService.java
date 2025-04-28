package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import br.com.empresa.springCrud.AppExample.repositories.TagOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TagService {

    @Autowired
    private TagOrderRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findById(UUID id) {
        return tagRepository.findById(id);
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteById(UUID id) {
        tagRepository.deleteById(id);
    }

    public Tag partialUpdate(UUID id, Map<String, Object> updates) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        tag.setName((String) value);
                        break;
                }
            });
            return tagRepository.save(tag);
        }
        return null; // Ou lançar uma exceção
    }
}