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
import entity.Album;
import entity.Playlist;
import db.DatabaseOperations;

@ManagedBean
@SessionScoped
public class PlaylistBean {
	private EntityManager manager;
	private String title;
	private List<Playlist> playlistsList;
	private String editPlaylistId;

	public PlaylistBean() {
		ServletContext sc = (ServletContext)
				FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getContext();
		EntityManagerFactory emf = (EntityManagerFactory)
						sc.getAttribute("emf");
		manager = emf.createEntityManager();
	}
	
	public String getEditPlaylistId() {
		return editPlaylistId;
	}

	public void setEditPlaylistId(String editPlaylistId) {
		this.editPlaylistId = editPlaylistId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	private void loadDataList() {
		playlistsList = DatabaseOperations.getAllPlaylistsDetails();
    }

    public List<Playlist> getPlaylistsList() {
        if (FacesContext.getCurrentInstance().getRenderResponse()) {
            loadDataList(); // Reload to get most recent data.
        }
        return playlistsList;
    }

    public String addNewPlaylist(PlaylistBean playlist) {
		return DatabaseOperations.createNewPlaylist(playlist.getTitle());		
	}
	
	public String deletePlaylistDetailsById(int playlistId) {
		return DatabaseOperations.deleteObjectDetails(playlistId,Playlist.class);		
	}
	
	public String editPlaylistDetailsById() {
		editPlaylistId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("selectedPlaylistId");		
		return "edit_playlist.xhtml";
	}
	
	public String updatePlaylistDetails(PlaylistBean playlistBean) {
		return DatabaseOperations.updatePlaylistDetails(Integer.parseInt(playlistBean.getEditPlaylistId()), playlistBean.getTitle());		
	}
	
	public String showSongsById(String editPlaylistId)
	{
		this.editPlaylistId = editPlaylistId;
		return "view_playlist_songs.xhtml";
	}
	
	public List<Song> getPlaylistSongsList(){
		Playlist playlist = new Playlist();
		playlist = DatabaseOperations.getPlaylistById(Integer.parseInt(editPlaylistId));
		return DatabaseOperations.getPlaylistSongs(playlist);
	}
	
	public String addSongById(int songId)
	{
		Song song = new Song();
		song = DatabaseOperations.getSongById(songId);
		Playlist playlist = new Playlist();
		playlist = DatabaseOperations.getPlaylistById(Integer.parseInt(editPlaylistId));
		return DatabaseOperations.createPlaylistSong(playlist, song);
	}
	
	public String remSongById(int songId)
	{
		Song song = new Song();
		song = DatabaseOperations.getSongById(songId);
		Playlist playlist = new Playlist();
		playlist = DatabaseOperations.getPlaylistById(Integer.parseInt(editPlaylistId));
		return DatabaseOperations.deletePlaylistSong(playlist, song);
	}
}
