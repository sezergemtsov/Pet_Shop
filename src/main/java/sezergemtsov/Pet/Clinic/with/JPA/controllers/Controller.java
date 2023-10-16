package sezergemtsov.Pet.Clinic.with.JPA.controllers;

import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sezergemtsov.Pet.Clinic.with.JPA.Customvalidator;
import sezergemtsov.Pet.Clinic.with.JPA.model.OwnerModel;
import sezergemtsov.Pet.Clinic.with.JPA.model.OwnerModel1;
import sezergemtsov.Pet.Clinic.with.JPA.servicies.Service;

import java.util.List;

@RestController
@AllArgsConstructor
@SuppressWarnings("unused")
public class Controller {

    private final Service service;
    private final Customvalidator customvalidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(customvalidator);
    }

    @PostMapping("/owner")
    public ResponseEntity<?> newOwner1(@RequestBody @Validated OwnerModel owner, Errors errors) {
        try {
            if(errors.getErrorCount()>0){
                errors.getAllErrors().forEach(e-> System.out.println(e.getDefaultMessage()));
            }
            service.newOwner1(owner.getName(), owner.getPhoneNumber());
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/owner1")
    public ResponseEntity<?> newOwner2(@RequestBody @Validated OwnerModel1 owner, Errors errors) {
        try {
            if(errors.getErrorCount()>0){
                errors.getAllErrors().forEach(e-> System.out.println(e.getDefaultMessage()));
                StringBuilder b = new StringBuilder();
                errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).forEach(v-> {
                    b.append(v);
                    b.append('\n');
                });
                String result = b.toString();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            } else {
                service.newOwner1(owner.getName(), owner.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body("success");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/")
    public String hello() {
        return "Pet Clinic";
    }

    @GetMapping("/owner")
    public void newOwner(@RequestParam("name") String name, @RequestParam("number") String number) {
        service.newOwner1(name, number);
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
