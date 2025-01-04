package br.edu.ifpb.pweb2.sayxis.servico;

import br.edu.ifpb.pweb2.sayxis.modelo.Comment;
import br.edu.ifpb.pweb2.sayxis.repositorio.CommentRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentService implements Service<Comment, Integer>{

    @Autowired
    private CommentRepositorio commentRepositorio;

    @Override
    public List<Comment> findAll() {
        return commentRepositorio.findAll();
    }

    @Override
    public Comment findById(Integer integer) {
        return null;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepositorio.save(comment);
    }
}
