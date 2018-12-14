package javaday.istanbul.sliconf.micro.template.Service;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.template.model.Template;
import javaday.istanbul.sliconf.micro.template.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Profile({"prod", "dev","test"})
public class TemplateRepositoryService {

    @Autowired
    protected TemplateRepository repo;

    public Optional<Template> findById(String id) { return repo.findById(id); }
    public Template findOne(String title) {
        return repo.findOne(title);
    }
    public Template findByTitle(String title){return repo.findByTitle(title); }
    public Template findByContent(String content){return repo.findByContent(content);}
    public Template findByCode(String code){return repo.findByCode(code);}

    public ResponseMessage save(Template template){
        ResponseMessage responseMessage = new ResponseMessage(true, "", "");
      if(Objects.isNull(template.getCode())||template.getCode().isEmpty()){
          responseMessage.setStatus(false);
          responseMessage.setMessage("code alanı null");

          return responseMessage;
      }
         Template tempTemplate=repo.findByCode(template.getCode());

      if(Objects.isNull(tempTemplate)){

         repo.save(template);
         responseMessage.setMessage("kayıt gerçekleşti");
         responseMessage.setStatus(true);


      }
      else responseMessage.setStatus(false);

      return responseMessage;

   }

}
