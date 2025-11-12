package giuseppetuccilli.Capstone.Gestionale.Immobili.controllers;

import giuseppetuccilli.Capstone.Gestionale.Immobili.entities.Provincia;
import giuseppetuccilli.Capstone.Gestionale.Immobili.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/province")
public class ProvinciaController {
    @Autowired
    private ProvinciaService provinciaService;

    @GetMapping
    public List<Provincia> getProvince() {
        return provinciaService.findAll();
    }
}
