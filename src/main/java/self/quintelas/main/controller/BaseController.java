package self.quintelas.main.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import self.quintelas.main.model.Pet;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<ModelType> {
    protected String pagePath;
    protected String pageName;

    protected JpaRepository repository;

    public BaseController(String pagePath, String pageName, JpaRepository repository) {
        this.pagePath = pagePath;
        this.pageName = pageName;
        this.repository = repository;
    }

    public abstract String getEditandoText(ModelType model);
    public abstract String getInserindoText();
    public abstract ModelType getModelInstance();

    @RequestMapping("")
    public String index(Model model) {
        List<ModelType> models = repository.findAll();

        model.addAttribute("page", pageName);
        model.addAttribute("pagePath", pagePath);
        model.addAttribute("models", models);

        return pagePath + "/lista";
    }

    @RequestMapping("{id}")
    public String show(@PathVariable int id, Model model) {
        Optional<ModelType> pet = repository.findById(id);

        if (!pet.isPresent()) {
            return "home";
        }

        ModelType petModel = pet.get();

        model.addAttribute("page", pageName);
        model.addAttribute("pagePath", pagePath);
        model.addAttribute("model", petModel);
        model.addAttribute("editandoText", getEditandoText(petModel));
        return "form";
    }

    @RequestMapping("/inserir")
    public String showInsert(Model model) {
        model.addAttribute("page", pageName);
        model.addAttribute("pagePath", pagePath);
        model.addAttribute("model", getModelInstance());
        model.addAttribute("inserindoText", getInserindoText());
        return "form";
    }

    @RequestMapping(value = "inserir", method = RequestMethod.POST)
    public String insert(@ModelAttribute() ModelType entity, Model model) {
        repository.save(entity);

        model.addAttribute("msg", "Inserido com sucesso!");
        return index(model);
    }

    @RequestMapping(value = "editar", method = RequestMethod.POST)
    public String update(@ModelAttribute() ModelType entity, Model model) {
        repository.save(entity);

        model.addAttribute("msg", "Editado com sucesso!");
        return index(model);
    }

    @RequestMapping("{id}/deletar")
    public String delete(@PathVariable int id, Model model) {
        repository.deleteById(id);

        model.addAttribute("msg", "Deletado com sucesso!");
        return index(model);
    }
}
