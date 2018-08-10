package db;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import beans.DoctorBean;
import entity.Doctor;
import entity.Medicine;
import entity.Patient;
import entity.Recipe;

public class DatabaseOperations {
	private static final String PERSISTENCE_UNIT_NAME = "forum";	
	private static EntityManager entityMgrObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transactionObj = entityMgrObj.getTransaction();
	
	//Select All Objects
	@SuppressWarnings("unchecked")
	public static List<Doctor> getAllDoctorDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Doctor s");
		List<Doctor> doctorsList = queryObj.getResultList();
		if (doctorsList != null && doctorsList.size() > 0) {			
			return doctorsList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Patient> getAllPatientDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Patient s");
		List<Patient> patientsList = queryObj.getResultList();
		if (patientsList != null && patientsList.size() > 0) {			
			return patientsList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Recipe> getAllRecipeDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Recipe s");
		List<Recipe> recipesList = queryObj.getResultList();
		if (recipesList != null && recipesList.size() > 0) {			
			return recipesList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Medicine> getAllMedicineDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Medicine s");
		List<Medicine> medicinesList = queryObj.getResultList();
		if (medicinesList != null && medicinesList.size() > 0) {			
			return medicinesList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Recipe> getPatientRecipes(Patient patient) {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Recipe s WHERE s.patient = :patient_id");
		queryObj.setParameter("patient_id", patient);
		List<Recipe> recipesList = queryObj.getResultList();
		if (recipesList != null && recipesList.size() > 0) {			
			return recipesList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Recipe> getDoctorRecipes(Doctor doctor) {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Recipe s WHERE s.doctor = :doctor_id");
		queryObj.setParameter("doctor_id", doctor);
		List<Recipe> recipesList = queryObj.getResultList();
		if (recipesList != null && recipesList.size() > 0) {			
			return recipesList;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Medicine> getRecipeMedicines(Recipe recipe) {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Medicine s WHERE s.recipe = :recipe_id");
		queryObj.setParameter("recipe_id", recipe);
		List<Medicine> medicinesList = queryObj.getResultList();
		if (medicinesList != null && medicinesList.size() > 0) {			
			return medicinesList;
		} else {
			return null;
		}
	}
	
	//Select Object by Id
	public static Patient getPatientById(int patientId) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
			Query queryObj = entityMgrObj.createQuery("Select s from Patient s WHERE s.id = :id");			
			queryObj.setParameter("id", patientId);
			return (Patient) queryObj.getSingleResult();
	}
	
	public static Doctor getDoctorById(int doctorId) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
			Query queryObj = entityMgrObj.createQuery("Select s from Doctor s WHERE s.id = :id");			
			queryObj.setParameter("id", doctorId);
			return (Doctor) queryObj.getSingleResult();
	}
	
	public static Recipe getRecipeById(int recipeId) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
			Query queryObj = entityMgrObj.createQuery("Select s from Recipe s WHERE s.id = :id");			
			queryObj.setParameter("id", recipeId);
			return (Recipe) queryObj.getSingleResult();
	}
	
	//Create Objects
	//Doctor and Patient have the same two fields 
	public static String createNewObject(String firstName,String lastName,Class className) {
		String url = null;
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}
		try {
			Object obj = className.newInstance();
			if (obj instanceof Doctor) {
				((Doctor) obj).setFirstName(firstName);
				((Doctor) obj).setLastName(lastName);
				url = "view_doctors.xhtml?faces-redirect=true";
			}
			if (obj instanceof Patient) {
				((Patient) obj).setFirstName(firstName);
				((Patient) obj).setLastName(lastName);
				url = "view_patients.xhtml?faces-redirect=true";
			}
			entityMgrObj.persist(obj);
			transactionObj.commit();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
		
	public static String createRecipe(String name,Patient patient,Doctor doctor) {
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}
		Recipe recipe = new Recipe();
		recipe.setName(name);
		recipe.setPatient(patient);
		recipe.setDoctor(doctor);
		entityMgrObj.persist(recipe);
		transactionObj.commit();
		return "view_recipes.xhtml?faces-redirect=true";
	}
	
	public static String createMedicine(String name,Recipe recipe) {
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}
		Medicine medicine = new Medicine();
		medicine.setName(name);
		medicine.setRecipe(recipe);
		entityMgrObj.persist(medicine);
		transactionObj.commit();
		return "view_medicines.xhtml?faces-redirect=true";
	}
	
	//Update Objects
	public static String updateObjDetails(int objId,Class className, String firstName, String lastName) {
		String url = null;
		String formName = null;
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
		if (className.getSimpleName().equals("Doctor")){
			url = "edit_doctor.xhtml";
			formName = "editDoctorForm:doctorId";
		}
		if (className.getSimpleName().equals("Patient")){
			url = "edit_patient.xhtml";
			formName = "editPatientForm:patientId";
		}
		if(isObjPresent(objId,className)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE " + className.getName() +" s SET s.firstName = :firstName,s.lastName = :lastName WHERE s.id = :id");			
			queryObj.setParameter("id", objId);
			queryObj.setParameter("firstName", firstName);
			queryObj.setParameter("lastName", lastName);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + objId + " Is Updated");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage(formName, new FacesMessage( className.getSimpleName() +" Record #" + objId + " Is Successfully Updated In Db"));
		return url;
	}
	
	public static String updateMedicineDetails(int objId, String name,Recipe recipe){
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
		if(isObjPresent(objId,Medicine.class)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE Medicine s SET s.name = :name, s.recipe = :recipe_id WHERE s.id = :id");			
			queryObj.setParameter("id", objId);
			queryObj.setParameter("name", name);
			queryObj.setParameter("recipe_id", recipe);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + objId + " Is Updated");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage("editMedicineForm:medicineId", new FacesMessage( "Recipe Record #" + objId + " Is Successfully Updated In Db"));
		return "edit_medicine.xhtml";
	}
	
	public static String updateRecipeDetails(int objId, String name,Doctor doctor,Patient patient) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
		if(isObjPresent(objId,Recipe.class)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE Recipe s SET s.name = :name, s.doctor = :doctor_id, s.patient = :patient_id WHERE s.id = :id");			
			queryObj.setParameter("id", objId);
			queryObj.setParameter("name", name);
			queryObj.setParameter("doctor_id", doctor);
			queryObj.setParameter("patient_id", patient);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + objId + " Is Updated");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage("editRecipeForm:recipeId", new FacesMessage( "Recipe Record #" + objId + " Is Successfully Updated In Db"));
		return "edit_recipe.xhtml";
	}
	
	//Delete
	public static String deleteObjectDetails(int objId,Class className) {
		String url = null;
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}

		Object deleteObj;
		try {
			deleteObj = className.newInstance();
			if(isObjPresent(objId,className)) {
				if ( deleteObj instanceof Doctor) {
					((Doctor)deleteObj).setId(objId);
					url = "view_doctors.xhtml?faces-redirect=true";
				}
				if ( deleteObj instanceof Patient) {
					((Patient)deleteObj).setId(objId);
					url = "view_patients.xhtml?faces-redirect=true";
				}
				if ( deleteObj instanceof Recipe) {
					((Recipe)deleteObj).setId(objId);
					url = "view_recipes.xhtml?faces-redirect=true";
				}
				if ( deleteObj instanceof Medicine) {
					((Medicine)deleteObj).setId(objId);
					url = "view_medicines.xhtml?faces-redirect=true";
				}
				entityMgrObj.remove(entityMgrObj.merge(deleteObj));
			}		
			transactionObj.commit();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return url;
	}
	
	private static boolean isObjPresent(int objId,Class className) {
		boolean idResult = false;
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM " + className.getName() + " s WHERE s.id = :id");
		queryObj.setParameter("id", objId);
		Object selectedObjId = queryObj.getSingleResult();
		if(selectedObjId != null) {
			idResult = true;
		}
		return idResult;
	}
}
