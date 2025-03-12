package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Comment;
import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PhotoService photoService;

    public Comment findById(Integer comment_id) {
        return commentRepository.findById(comment_id)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado."));
    }

    public void addComment(Photographer photographer, PhotoDTO photoDto, String text) {
        Photo photo = photoService.findById(photoDto.getId());
        Comment comment = Comment.builder()
                .photographer(photographer)
                .photo(photo)
                .commentText(text)
                .isCaption(true)
                .build();
        save(comment);
    }

    public void deleteComment(Integer comment_id) {
        if (findById(comment_id) != null) {
            commentRepository.deleteById(comment_id);
        }
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getComments(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            return commentRepository.getComments(photo);
        }
        return null;
    }

    public Integer countComments(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            return commentRepository.countComments(photo);
        } else {
            return null;
        }
    }

    public Comment getCaption(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            return commentRepository.getCaption(photo);
        } else {
            return null;
        }
    }
}
