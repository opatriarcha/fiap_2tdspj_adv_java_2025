package br.com.empresa.springCrud.AppExample.services;

import br.com.empresa.springCrud.AppExample.domainmodel.QUser;
import br.com.empresa.springCrud.AppExample.domainmodel.User;
import br.com.empresa.springCrud.AppExample.repositories.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public User partialUpdate(UUID id, Map<String, Object> updates) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        user.setName((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "password":
                        user.setPassword((String) value);
                        break;
                    // Você pode adicionar mais campos aqui, se quiser permitir atualizações parciais deles
                }
            });
            return userRepository.save(user);
        }
        return null; // Em projetos sérios, melhor lançar exceção do tipo ResourceNotFoundException
    }

    @Override
    public Page<User> findAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    public Iterable<User> searchByNameLike( final String name){
        QUser user = QUser.user;
        return this.userRepository.findAll( user.name.like(name));
    }
}
