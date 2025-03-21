package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.*;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PhotoTagService photoTagService;

    //retorna a página de criação da publicação
    @GetMapping("/upload")
    public String showUploadPhotoForm() {
        return "/photo-form";
    }

    //cria uma publicação
    @PostMapping("/upload")
    public String uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            Model model,
            Principal principal
    ) {
        try {

            Photographer photographer = photographerService.findByUsername(principal.getName());

            PhotoDTO savedPhoto = photoService.addPhoto(photographer, file.getBytes(), false);
            Photo photo = photoService.findById(savedPhoto.getId());
            photo.setImageUrl("http://localhost:8080/photo/" + photo.getId() + "/image");

            //adiciona hashtags, se houver
            if (!hashtags.isEmpty()) {
                photoTagService.processTags(hashtags, photo);
            }

            //adiciona legenda como comentário, se houver
            if (!caption.isEmpty()) {
                commentService.addComment(photographer, savedPhoto, caption);
            }
            model.addAttribute("successMessage", "Envio da foto realizado com sucesso.");

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Erro ao enviar a foto: " + e.getMessage());
        }

        return "redirect:/photo/upload";
    }

    //retorna a página da foto
    @GetMapping("/{photo_id}")
    public String getPhoto(Model model, @PathVariable Integer photo_id, Principal principal, @RequestParam(value = "editTags", required = false, defaultValue = "false") boolean editTags) {
        String link_photo = "http://localhost:8080/photo/" + photo_id + "/image";
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        Photo photo = photoService.findById(photo_id);
        Photographer author = photo.getPhotographer();

        Integer photographer_id = photographerLogged.getId();
        boolean canComment = photographerLogged.isHas_comment_permission();
        boolean isAuthor = photographer_id.equals(photoService.authorId(photo_id));
        model.addAttribute("linkPhoto", link_photo);
        model.addAttribute("numberOfLikes", likeService.countLikes(photo_id));
        model.addAttribute("isLiked", likeService.isLiked(photographer_id, photo_id));
        model.addAttribute("comments", commentService.getComments(photo_id));
        model.addAttribute("tags", photoTagService.getTags(photo_id));
        model.addAttribute("notPhotoTags", photoTagService.getNotPhotoTags(photo_id));
        model.addAttribute("photo_id", photo_id);
        model.addAttribute("author", author);
        model.addAttribute("photographerLogged", photographerLogged);
        model.addAttribute("profilePhoto", photoService.findProfilePhoto(author));
        model.addAttribute("canComment", canComment);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("editTags", editTags);
        if (commentService.getCaption(photo_id) != null) {
            model.addAttribute("caption", commentService.getCaption(photo_id).getCommentText());
        }
        return "/photo";
    }

    @GetMapping("/{photo_id}/edit-tags")
    public String editPhotoTags(@PathVariable Integer photo_id){
        return "redirect:/photo/" + photo_id + "?editTags=true";
    }

    @GetMapping("/{photo_id}/save-tag-edition")
    public String savePhotoTagEditions(@PathVariable Integer photo_id, @RequestParam(required = false) String hashtags){
        Photo photo = photoService.findById(photo_id);
        photoTagService.processTags(hashtags, photo);
        return "redirect:/photo/" + photo_id + "?editTags=false";
    }

    //retorna a imagem da página da foto
    @GetMapping("/{photo_id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer photo_id) {
        Photo photo = photoService.findById(photo_id);
        if (photo != null) {
            byte[] imageData = photo.getImageData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        }
        return null;
    }

    @PostMapping("/{photo_id}/like")
    public String addLike(Principal principal, @PathVariable Integer photo_id) {

        Photographer photographerLogged = photographerService.findByUsername(principal.getName());
        Integer photographer_id = photographerLogged.getId();
        if (photographer_id != null) {
            likeService.addLike(photographer_id, photo_id);
            return "redirect:/photo/{photo_id}";
        }
        return null;
    }

    @PostMapping("/{photo_id}/unlike")
    public String removeLike(Principal principal, @PathVariable Integer photo_id) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());
        Integer photographer_id = photographerLogged.getId();

        if (photographer_id != null) {
            likeService.removeLike(photographer_id, photo_id);
            return "redirect:/photo/{photo_id}";
        }
        return null;
    }

    @PostMapping("/{photo_id}/add-comment")
    public String addComment(RedirectAttributes ra, Principal principal, @PathVariable Integer photo_id, @RequestParam("comment") String commentText) {
        if (commentText.length() > 512) {
            ra.addFlashAttribute("tooLong", true);
            ra.addFlashAttribute("sentComment", commentText);
            return "redirect:/photo/{photo_id}";
        }
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());
        Integer photographer_id = photographerLogged.getId();
        if (photographer_id != null) {
            Photographer photographer = photographerService.findById(photographer_id);
            Photo photo = photoService.findById(photo_id);

            Comment newComment = Comment.builder()
                    .photographer(photographer)
                    .photo(photo)
                    .commentText(commentText)
                    .isCaption(false)
                    .build();

            commentService.save(newComment);
        }
        return "redirect:/photo/{photo_id}";
    }

    @PostMapping("/{photo_id}/comment/{comment_id}/delete")
    public String deleteComment(@PathVariable("photo_id") Integer photo_Id, @PathVariable("comment_id") Integer comment_Id, Principal principal) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        if (photographerLogged != null) {
            commentService.deleteComment(comment_Id);
            return "redirect:/photo/{photo_id}";
        }
        return null;
    }

    @GetMapping("/{photo_id}/comment/{comment_id}/edit")
    public String editCommentForm(@PathVariable Integer photo_id, @PathVariable Integer comment_id, Model model) {
        Comment comment = commentService.findById(comment_id);
        model.addAttribute("comment", comment);
        model.addAttribute("photo_id", photo_id);
        return "/edit-comment";
    }

    @PostMapping("/{photo_id}/comment/{comment_id}/update")
    public String updateComment(@PathVariable Integer photo_id, @PathVariable Integer comment_id, @RequestParam("comment") String newCommentText, Principal principal) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        if (photographerLogged != null) {
            Comment comment = commentService.findById(comment_id);

            if (comment != null && comment.getPhotographer().getId().equals(photographerLogged.getId())) {
                if (newCommentText.trim().isEmpty()) {
                    // Se o comentário estiver vazio, deleta do banco
                    commentService.deleteComment(comment_id);
                } else {
                    // Caso contrário, apenas atualiza o texto
                    comment.setCommentText(newCommentText);
                    commentService.save(comment);
                }
            }
        }

        return "redirect:/photo/" + photo_id;
    }



}
