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

import entity.Doctor;
import entity.Medicine;
import entity.Patient;
import entity.Recipe;
import entity.User;
import db.DatabaseOperations;
@ManagedBean
@SessionScoped
public class MedicineBean {
	private EntityManager manager;

	private String name;
	private String selectedRecipeId;
	private String editMedicineId;
	private List<SelectItem> recipeItems;
	
	public MedicineBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSelectedRecipeId() {
		return selectedRecipeId;
	}

	public void setSelectedRecipeId(String selectedRecipeId) {
		this.selectedRecipeId = selectedRecipeId;
	}
	
	public String getEditMedicineId() {
		return editMedicineId;
	}

	public void setEditMedicineId(String editMedicineId) {
		this.editMedicineId = editMedicineId;
	}

	public List<Medicine> getMedicinesList(){
		return DatabaseOperations.getAllMedicineDetails();
	}
	
	public String addNewMedicine(MedicineBean medicine) {
		Recipe recipe = new Recipe();
		recipe = DatabaseOperations.getRecipeById(Integer.parseInt(selectedRecipeId));
		return DatabaseOperations.createMedicine(medicine.getName(),recipe);		
	}
	
	public String deleteMedicineById(int medId) {
		return DatabaseOperations.deleteObjectDetails(medId,Medicine.class);		
	}
	
	public String editMedicineDetailsById() {
		editMedicineId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedMedicineId");		
		return "edit_medicine.xhtml";
	}
	
	public List<SelectItem> getRecipeItems(){
        if (recipeItems == null){
        	recipeItems = new ArrayList<SelectItem>();
            List<Recipe> recipeList = DatabaseOperations.getAllRecipeDetails();
            for (Recipe recipe : recipeList){
            	recipeItems.add(new SelectItem(
            			recipe.getId(), 
            			recipe.getName()
                        )
            	); // value, label, the value to choose and label to appear fo the user
            }
        }
        return recipeItems;
	} 
	
	public String updateMedicineDetails(MedicineBean medicineBean) {
		Recipe recipe = new Recipe();
		recipe = DatabaseOperations.getRecipeById(Integer.parseInt(selectedRecipeId));
		return DatabaseOperations.updateMedicineDetails(Integer.parseInt(medicineBean.getEditMedicineId()),medicineBean.getName(),recipe);		
	}
}
