package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTag;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTagId;
import br.edu.ifpb.pweb2.sayxis.model.Tag;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoTagRepository;
import br.edu.ifpb.pweb2.sayxis.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PhotoTagRepository photoTagRepository;
    @Autowired
    private PhotoService photoService;


    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<String> getTagNames() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> tag.getTagName().toLowerCase())
                .collect(Collectors.toList());
    }

    public Tag findByTagName(String tagName) {
        return tagRepository.findByTagName(tagName).orElse(null);
    }

    public Tag addTag(String name) {
        Tag newTag = Tag.builder()
                .tagName(name)
                .build();
        return save(newTag);
    }

    public PhotoTagId addPhotoTagId(Integer photo_id, Integer tag_id) {
        return PhotoTagId.builder()
                .tag_id(tag_id)
                .photo_id(photo_id)
                .build();
    }

    public void addPhotoTag(PhotoTagId photoTag_id, Photo photo, Tag tag) {
        PhotoTag newPhotoTag = PhotoTag.builder()
                .id(photoTag_id)
                .photo(photo)
                .tag(tag)
                .build();
        savePhotoTag(newPhotoTag);
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void savePhotoTag(PhotoTag PhotoTag) {
        photoTagRepository.save(PhotoTag);
    }
}
