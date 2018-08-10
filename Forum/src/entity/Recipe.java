package entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Recipe {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=200)
	private String name;
	@OneToMany(mappedBy="recipe")
	private List<Medicine> medicines;
	
	@ManyToOne
	@JoinColumn(name="patient_id")
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name="doctor_id")
	private Doctor doctor;
	
	
	public List<Medicine> getMedicines() {
		return medicines;
	}
	public void setMedicines(List<Medicine> medicines) {
		this.medicines = medicines;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Medicine> getMedicine() {
		return medicines;
	}
	public void setMedicine(List<Medicine> medicine) {
		this.medicines = medicine;
	}
}
