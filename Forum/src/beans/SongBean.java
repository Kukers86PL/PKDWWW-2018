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

import entity.Song;
import db.DatabaseOperations;

@ManagedBean
@SessionScoped
public class SongBean {
	private EntityManager manager;
	private String title;
	private String artist;
	private List<Song> songsList;
	private String editSongId;

	public SongBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getEditSongId() {
		return editSongId;
	}

	public void setEditSongId(String editSongId) {
		this.editSongId = editSongId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	private void loadDataList() {
		songsList = DatabaseOperations.getAllSongsDetails();
    }

    public List<Song> getSongsList() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            loadDataList(); // Reload to get most recent data.
        }
        return songsList;
    }

	public String addNewSong(SongBean song) {
		return DatabaseOperations.createNewSong("t1","a1");		
	}
	
	public String deleteSongDetailsById(int songId) {
		return DatabaseOperations.deleteObjectDetails(songId,Song.class);		
	}
	
	public String editSongDetailsById() {
		editSongId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedSongId");		
		return "edit_song.xhtml";
	}
	
	public String updateSongDetails(SongBean song) {
		return DatabaseOperations.updateObjDetails(Integer.parseInt(song.getEditSongId()),Song.class, song.getTitle(),song.getArtist());		
	}
}
