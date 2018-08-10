package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import entity.Doctor;
import entity.Patient;
import entity.Recipe;
import entity.User;
import db.DatabaseOperations;
@ManagedBean
@SessionScoped
public class PatientBean {
	private EntityManager manager;
	private String firstName;
	private String lastName;
	private String editPatientId;
	
	public PatientBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEditPatientId() {
		return editPatientId;
	}

	public void setEditPatientId(String editPatientId) {
		this.editPatientId = editPatientId;
	}
	
	public List<Patient> getPatientsList(){
		return DatabaseOperations.getAllPatientDetails();
	}
	public String addNewPatient(PatientBean patient) {
		return DatabaseOperations.createNewObject(patient.getFirstName(),patient.getLastName(),Patient.class);		
	}
	
	public String deletePatientById(int docId) {
		return DatabaseOperations.deleteObjectDetails(docId,Patient.class);		
	}
	
	public String showRecipesById() {
		return "show_patient_recipes.xhtml";
	}
	
	public List<Recipe> getPatientRecipesList(){
		Patient patient = new Patient();
		editPatientId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedPatientId");
		patient = DatabaseOperations.getPatientById(Integer.parseInt(editPatientId));
		return DatabaseOperations.getPatientRecipes(patient);
	}
	
	public String editPatientDetailsById() {
		editPatientId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedPatientId");		
		return "edit_patient.xhtml";
	}
	
	public String updatePatientDetails(PatientBean patientBean) {
		return DatabaseOperations.updateObjDetails(Integer.parseInt(patientBean.getEditPatientId()),Patient.class, patientBean.getFirstName(),patientBean.getLastName());		
	}

}
