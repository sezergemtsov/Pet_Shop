package sezergemtsov.Pet.Clinic.with.JPA;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sezergemtsov.Pet.Clinic.with.JPA.model.Owner;
import sezergemtsov.Pet.Clinic.with.JPA.model.OwnerModel;
import sezergemtsov.Pet.Clinic.with.JPA.model.OwnerModel1;

@Component
public class Customvalidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return OwnerModel.class.isAssignableFrom(clazz) | OwnerModel1.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        System.out.println(target.getClass());

        if(target.getClass()== OwnerModel.class){
            OwnerModel ownerModel = (OwnerModel) target;
            if (ownerModel.getPhoneNumber() == null) {
                errors.reject("phone number must be provided", "phone number must be provided");
            }
        } else if(target.getClass()== OwnerModel1.class){
            OwnerModel1 ownerModel = (OwnerModel1) target;
            if (ownerModel.getEmail() == null) {
                errors.reject("email must be provided", "email must be provided");
            }
        } else {
            errors.reject("type error");
        }


    }
}
