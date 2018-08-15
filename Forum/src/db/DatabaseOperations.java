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
import entity.Song;

public class DatabaseOperations {
	private static final String PERSISTENCE_UNIT_NAME = "forum";	
	private static EntityManager entityMgrObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME).createEntityManager();
	private static EntityTransaction transactionObj = entityMgrObj.getTransaction();
	
	//Select All Objects
	
	@SuppressWarnings("unchecked")
	public static List<Song> getAllSongsDetails() {
		Query queryObj = entityMgrObj.createQuery("SELECT s FROM Song s");
		List<Song> songsList = queryObj.getResultList();
		if (songsList != null && songsList.size() > 0) {			
			return songsList;
		} else {
			return null;
		}
	}
	
	//Select Object by Id
	public static Song getSongById(int songId) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
			Query queryObj = entityMgrObj.createQuery("SELECT s FROM Song s WHERE s.id = :id");			
			queryObj.setParameter("id", songId);
			return (Song) queryObj.getSingleResult();
	}
	
	//Create Objects
	 
	public static String createNewSong(String title,String artist) {
		if(!transactionObj.isActive()) {
			transactionObj.begin();
		}
		Song song = new Song();
		song.setTitle(title);
		song.setArtist(artist);
		entityMgrObj.persist(song);
		transactionObj.commit();
		return "view_songs.xhtml?faces-redirect=true";
	}
	
	//Update Objects
	public static String updateSongDetails(int objId, String title,String artist) {
		if (!transactionObj.isActive()) {
			transactionObj.begin();
		}
		if(isObjPresent(objId,Song.class)) {
			Query queryObj = entityMgrObj.createQuery("UPDATE Song s SET s.title = :title, s.artist = :artist WHERE s.id = :id");			
			queryObj.setParameter("id", objId);
			queryObj.setParameter("title", title);
			queryObj.setParameter("artist", artist);
			int updateCount = queryObj.executeUpdate();
			if(updateCount > 0) {
				System.out.println("Record For Id: " + objId + " Is Updated");
			}
		}
		transactionObj.commit();
		FacesContext.getCurrentInstance().addMessage("editSongForm:recipeId", new FacesMessage( "Song Record #" + objId + " Is Successfully Updated In Db"));
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
				if ( deleteObj instanceof Song) {
					((Song)deleteObj).setId(objId);
					url = "view_songs.xhtml?faces-redirect=true";
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
