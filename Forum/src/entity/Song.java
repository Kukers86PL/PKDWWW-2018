package entity;

import java.util.List;
import java.util.Set;

import javax.faces.bean.SessionScoped;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Song {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(length=50)
	private String title;
	@Column(length=50)
	private String artist;
	@ManyToOne
	@JoinColumn(name="album_id")
	private Album album;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "playlist_song", joinColumns = { @JoinColumn(name = "playlist_id") }, inverseJoinColumns = { @JoinColumn(name = "song_id") })
	private List<Playlist> playlists;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	public List<Playlist> getPlaylist() {
		return playlists;
	}
	public void setPlaylist(List<Playlist> playlists) {
		this.playlists = playlists;
	}
}
