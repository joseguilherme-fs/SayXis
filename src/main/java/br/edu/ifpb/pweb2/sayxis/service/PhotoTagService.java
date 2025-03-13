package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTag;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTagId;
import br.edu.ifpb.pweb2.sayxis.model.Tag;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhotoTagService {

    @Autowired
    private PhotoTagRepository photoTagRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private TagService tagService;

    public void processTags(String hashtags, Photo photo) {
        if (hashtags == null || hashtags.trim().isEmpty()) {
            return;
        }

        String[] tagsArray = hashtags.split(",");

        Set<String> existingTags = new HashSet<>(tagService.getTagNames());

        Set<String> photoTags = new HashSet<>(getTags(photo.getId()));

        for (String tag : tagsArray) {
            String tagName = tag.trim().toLowerCase();

            if (!tagName.isEmpty() && !photoTags.contains(tagName)) {
                Tag savedTag;

                if (existingTags.contains(tagName)) {
                    savedTag = tagService.findByTagName(tagName);
                } else {
                    savedTag = tagService.addTag(tagName);
                    existingTags.add(tagName);
                }

                PhotoTagId savedPhotoTagId = tagService.addPhotoTagId(photo.getId(), savedTag.getId());
                tagService.addPhotoTag(savedPhotoTagId, photo, savedTag);
            }
        }
    }


    public List<String> getTags(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            List<PhotoTag> photoTagList = photoTagRepository.getPhotoTagList(photo);
            List<String> tagList = new ArrayList<String>();
            for (PhotoTag photoTag : photoTagList) {
                tagList.add(photoTag.getTag().getTagName());
            }
            return tagList;
        }
        return null;
    }

    public List<String> getNotPhotoTags(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            List<PhotoTag> notPhotoTagList = photoTagRepository.getNotPhotoTagList(photo);
            List<String> tagList = new ArrayList<String>();
            for (PhotoTag photoTag : notPhotoTagList) {
                tagList.add(photoTag.getTag().getTagName());
            }
            return tagList;
        }
        return null;
    }

    public PhotoTag findById(PhotoTagId photoTag_id) {
        return photoTagRepository.findById(photoTag_id)
                .orElseThrow(() -> new RuntimeException("A tag não foi encontrada nessa foto."));
    }

    public PhotoTag save(PhotoTag photoTag) {
        return photoTagRepository.save(photoTag);
    }
}
