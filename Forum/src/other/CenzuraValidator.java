package other;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("cenzura")
public class CenzuraValidator implements Validator {

	@Override
	public void validate(FacesContext arg0,
			UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (arg1 instanceof UIInput) {
			if (arg2 == null)
				return;
			String text = arg2.toString();
			
			if (text.contains("kot")) {
				FacesMessage msg = new FacesMessage("Tekst zawiera niedozwolone wyrazy!");
				throw new ValidatorException(msg);
			}
		}
	}

}
