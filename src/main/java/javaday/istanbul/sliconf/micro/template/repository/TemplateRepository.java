package javaday.istanbul.sliconf.micro.template.repository;


import javaday.istanbul.sliconf.micro.template.model.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {
    Optional<Template> findById(String id);

    Template findByTitle(String title);

    Template findByContent(String content);

    Template findByCode(String code);



}
