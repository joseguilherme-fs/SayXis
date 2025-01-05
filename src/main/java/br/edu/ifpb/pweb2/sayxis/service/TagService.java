package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Tag;
import br.edu.ifpb.pweb2.sayxis.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagService implements Service<Tag, Integer> {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(Integer integer) {
        return null;
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }
}
