package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Comment;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.photo = :photo and c.isCaption = false")
    List<Comment> getComments(@Param("photo") Photo photo);

    @Query("select c from Comment c where c.photo = :photo and c.isCaption = true")
    Comment getCaption(@Param("photo") Photo photo);

    @Query("select count(c) from Comment c where c.photo = :photo")
    int countComments(@Param("photo") Photo photo);
}

