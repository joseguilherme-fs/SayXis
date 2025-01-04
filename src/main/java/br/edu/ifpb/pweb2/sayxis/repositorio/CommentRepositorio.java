package br.edu.ifpb.pweb2.sayxis.repositorio;

import br.edu.ifpb.pweb2.sayxis.modelo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepositorio extends JpaRepository<Comment, Integer> {

}
