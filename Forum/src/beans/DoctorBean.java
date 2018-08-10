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
public class DoctorBean {
	private EntityManager manager;
	private String firstName;
	private String lastName;
	private String editDoctorId;
	private List<Doctor> doctorsList;

	public DoctorBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getEditDoctorId() {
		return editDoctorId;
	}

	public void setEditDoctorId(String editDoctorId) {
		this.editDoctorId = editDoctorId;
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
	
	private void loadDataList() {
		doctorsList = DatabaseOperations.getAllDoctorDetails();
    }

    public List<Doctor> getDoctorsList() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            loadDataList(); // Reload to get most recent data.
        }
        return doctorsList;
    }
	
    public String showRecipesById() {
		return "show_doctor_recipes.xhtml";
	}
	
	public List<Recipe> getDoctorRecipesList(){
		Doctor doctor = new Doctor();
		editDoctorId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedDoctorId");
		doctor = DatabaseOperations.getDoctorById(Integer.parseInt(editDoctorId));
		return DatabaseOperations.getDoctorRecipes(doctor);
	}

	public String addNewDoctor(DoctorBean doc) {
		return DatabaseOperations.createNewObject(doc.getFirstName(),doc.getLastName(),Doctor.class);		
	}
	
	public String deleteDoctorDetailsById(int docId) {
		return DatabaseOperations.deleteObjectDetails(docId,Doctor.class);		
	}
	
	public String editDoctorDetailsById() {
		editDoctorId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedDoctorId");		
		return "edit_doctor.xhtml";
	}
	
	public String updateDoctorDetails(DoctorBean docBean) {
		return DatabaseOperations.updateObjDetails(Integer.parseInt(docBean.getEditDoctorId()),Doctor.class, docBean.getFirstName(),docBean.getLastName());		
	}
}
