package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Authority;
import br.edu.ifpb.pweb2.sayxis.model.User;
import br.edu.ifpb.pweb2.sayxis.repository.AuthorityRepository;
import br.edu.ifpb.pweb2.sayxis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Salva o usuário no repositório
        User savedUser = userRepository.save(user);

        // Cria a Authority com a role ROLE_CLIENTE
        Authority authority = new Authority();
        Authority.AuthorityId authorityId = new Authority.AuthorityId(savedUser.getUsername(), "ROLE_CLIENTE");
        authority.setId(authorityId);
        authority.setUsername(savedUser);  // Associa o usuário salvo à authority

        // Salva a authority no repositório
        authorityRepository.save(authority);

        return savedUser;
    }
}