package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.Tag;
import br.com.empresa.springCrud.AppExample.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Page<Tag> findAllPaged(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Optional<Tag> findById(UUID id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteById(UUID id) {
        tagRepository.deleteById(id);
    }

    @Override
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
        return null; // Melhor ainda seria lançar ResourceNotFoundException
    }

    @Override
    public boolean existsById(UUID id) {
        return tagRepository.existsById(id);
    }
}
