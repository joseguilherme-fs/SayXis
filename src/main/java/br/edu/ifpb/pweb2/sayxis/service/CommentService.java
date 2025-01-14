package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Comment;
import br.edu.ifpb.pweb2.sayxis.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentService implements Service<Comment, Integer>{

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PhotoService photoService;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Integer integer) {
        return null;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Integer photoId) {
        if (photoService.findById(photoId) != null) {
            return commentRepository.getComments(photoId);
        } else {
            return null;
        }
    }
}
