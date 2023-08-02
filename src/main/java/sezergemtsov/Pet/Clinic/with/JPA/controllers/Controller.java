package sezergemtsov.Pet.Clinic.with.JPA.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sezergemtsov.Pet.Clinic.with.JPA.model.Pet;
import sezergemtsov.Pet.Clinic.with.JPA.servicies.Service;

import java.util.List;

@RestController
@AllArgsConstructor
public class Controller {

    private final Service service;

    @GetMapping("/")
    public String hello() {
        return "Pet Clinic";
    }

    @GetMapping("/owner")
    public void newOwner(@RequestParam("name") String name, @RequestParam("number") String number) {
        service.newOwner(name, number);
    }

    @GetMapping("/pet")
    public void newPet(@RequestParam("name") String name, @RequestParam("number") Long number) {
        service.newPet(name, number);
    }

    @GetMapping("/visit")
    public void newVisit(@RequestParam("owner") Long ownerId, @RequestParam("collar") Long collarId) {
        service.newVisit(ownerId, collarId);
    }

    @GetMapping("/name")
    public String getPetName(@RequestParam("visit") Long visitId) {
        return service.getName(visitId);
    }
    @GetMapping("/fill")
    public void fill() {
        service.fill();
    }

    @GetMapping("/test")
    public List<?> test() {
        return service.joinTest();
    }

}
