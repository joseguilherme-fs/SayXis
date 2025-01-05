package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
