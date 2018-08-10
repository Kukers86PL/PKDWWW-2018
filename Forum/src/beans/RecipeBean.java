package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.hibernate.bytecode.internal.bytebuddy.PassThroughInterceptor;

import entity.Doctor;
import entity.Medicine;
import entity.Patient;
import entity.Recipe;
import entity.User;
import db.DatabaseOperations;
@ManagedBean
@SessionScoped
public class RecipeBean {
	private EntityManager manager;
	private String name;
	private String editRecipeId;
	private String selectedPatientId;
	private String selectedDoctorId;
	private List<SelectItem> patientItems;
    private List<SelectItem> doctorItems;
	
	public String getSelectedDoctorId() {
		return selectedDoctorId;
	}
	
	public void setSelectedDoctorId(String selectedDoctorId) {
		this.selectedDoctorId = selectedDoctorId;
	}

	public String getSelectedPatientId() {
		return selectedPatientId;
	}

	public void setSelectedPatientId(String selectedPatientId) {
		this.selectedPatientId = selectedPatientId;
	}

	public RecipeBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getEditRecipeId() {
		return editRecipeId;
	}

	public void setEditRecipeId(String editRecipeId) {
		this.editRecipeId = editRecipeId;
	}

	public List<Recipe> getRecipesList(){
		return DatabaseOperations.getAllRecipeDetails();
	}
	
	public List<Patient> getPatientsList(){
		return DatabaseOperations.getAllPatientDetails();
	}
	
	public List<SelectItem> getPatientsItems(){
        if (patientItems == null){
        	patientItems = new ArrayList<SelectItem>();
            List<Patient> patientList = DatabaseOperations.getAllPatientDetails();
            for (Patient patient : patientList){
            	patientItems.add(new SelectItem(
            			patient.getId(), 
            			patient.getFirstName() + " " + patient.getLastName()
                        )
            	); // value, label, the value to choose and label to appear fo the user
            }
        }
        return patientItems;
    }
	
	public List<SelectItem> getDoctorsItems(){
        if (doctorItems == null){
        	doctorItems = new ArrayList<SelectItem>();
            List<Doctor> doctorList = DatabaseOperations.getAllDoctorDetails();
            for (Doctor doc : doctorList){
            	doctorItems.add(new SelectItem(
            			doc.getId(), 
            			doc.getFirstName() + " " + doc.getLastName()
                        )
            	); // value, label, the value to choose and label to appear fo the user
            }
        }
        return doctorItems;
	} 
	 
	public String addNewRecipe(RecipeBean recipe) {
		Patient patient = new Patient();
		Doctor doctor = new Doctor();
		patient = DatabaseOperations.getPatientById(Integer.parseInt(selectedPatientId));
		doctor = DatabaseOperations.getDoctorById(Integer.parseInt(selectedDoctorId));
		return DatabaseOperations.createRecipe(recipe.getName(),patient,doctor);		
	}
	
	public String deleteRecipeById(int docId) {
		return DatabaseOperations.deleteObjectDetails(docId,Recipe.class);		
	}
	
	public String editRecipeDetailsById() {
		editRecipeId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedRecipeId");		
		return "edit_recipe.xhtml";
	}
	
	public String showMedicinesById() {
		return "show_recipe_medicines.xhtml";
	}
	
	public List<Medicine> getRecipeMedicinesList(){
		Recipe recipe = new Recipe();
		editRecipeId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedRecipeId");
		recipe = DatabaseOperations.getRecipeById(Integer.parseInt(editRecipeId));
		return DatabaseOperations.getRecipeMedicines(recipe);
	}
	
	public String updateRecipeDetails(RecipeBean recipeBean) {
		Patient patient = new Patient();
		Doctor doctor = new Doctor();
		patient = DatabaseOperations.getPatientById(Integer.parseInt(selectedPatientId));
		doctor = DatabaseOperations.getDoctorById(Integer.parseInt(selectedDoctorId));
		return DatabaseOperations.updateRecipeDetails(Integer.parseInt(recipeBean.getEditRecipeId()),recipeBean.getName(),doctor,patient);		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
