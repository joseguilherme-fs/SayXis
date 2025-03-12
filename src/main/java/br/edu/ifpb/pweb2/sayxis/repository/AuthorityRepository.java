package br.edu.ifpb.pweb2.sayxis.repository;


import br.edu.ifpb.pweb2.sayxis.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Authority.AuthorityId> {
}