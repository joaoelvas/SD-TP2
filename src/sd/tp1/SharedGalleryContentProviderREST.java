package sd.tp1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import sd.tp1.gui.GalleryContentProvider;
import sd.tp1.gui.Gui;

public class SharedGalleryContentProviderREST implements GalleryContentProvider {
	private static final int SERVER_PORT = 9000;
	private static final String SERVER_ADDRESS = "233.10.10.10";
	private static final String SERVER_KIND = "albumserver";
	
	Gui gui;	
	
	String serviceURL;
	
	MulticastSocket socket;
	
	Map<String, Long> serverStartTime;
	Client client;
	ClientConfig config;
	
	SharedGalleryContentProviderREST() {
		// TODO: code to do when shared gallery starts
		serverStartTime = new ConcurrentHashMap<>();
		this.config = new ClientConfig();
		this.client = ClientBuilder.newClient(config);
		
		try {
			socket = new MulticastSocket();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		new Thread(() -> {
			try{
				for(;;){
					checkForServers();
				}
			} catch (Exception e) {
				System.err.println("Erro: " + e.getMessage());
				
				e.printStackTrace();
			}
		}).start();
		
	}

	private void checkForServers() {
		try {
			DatagramPacket packetReceived;
			
			byte[] byteQuestion = SERVER_KIND.getBytes();
			DatagramPacket packet = new DatagramPacket(byteQuestion, byteQuestion.length);
			
			packet.setAddress(InetAddress.getByName(SERVER_ADDRESS));
			packet.setPort(SERVER_PORT);
			
			
			while(true) {
				Set<String> urls = serverStartTime.keySet();
				for(String s : urls) {
					if(System.currentTimeMillis() - serverStartTime.get(s) > 20000) {
						serverStartTime.remove(s);
						System.out.println(s + " removed");
					}
				}
				try {
					socket.send(packet);
					byte[] buffer = new byte[65536];
					packetReceived = new DatagramPacket(buffer, buffer.length);
					socket.setSoTimeout(5000);
					socket.receive(packetReceived);
					
					String newUrl = new String(packetReceived.getData(),packetReceived.getOffset(),packetReceived.getLength());
					boolean urlToAdd = false;
					if(!serverStartTime.containsKey(newUrl)) {
						urlToAdd = true;
						System.out.println(newUrl);
					}
					
					serverStartTime.put(newUrl, System.currentTimeMillis());
					if(urlToAdd) {
						if(gui != null) {
							gui.updateAlbums();
						}
					}
					
				} catch (SocketTimeoutException e) {
					System.err.println("Erro: " + e.getMessage());
				}
			}
		} catch(Exception e) {
			System.err.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 *  Downcall from the GUI to register itself, so that it can be updated via upcalls.
	 */
	@Override
	public void register(Gui gui) {
		if( this.gui == null ) {
			this.gui = gui;
		}
	}

	/**
	 * Returns the list of albums in the system.
	 * On error this method should return null.
	 * @throws MalformedURLException
	 */
	@Override
	public List<Album> getListOfAlbums() {
		List<Album> lst = new ArrayList<Album>();
		
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				
				String[] albums = target.path("/fileserverrest/listalbums").request().accept(MediaType.APPLICATION_JSON).get(String[].class);
				
				List<String> serverAlbums = new ArrayList<String>();
				
				for(String s : albums) {
					serverAlbums.add(s);
				}
				
				for(int i = 0; i < serverAlbums.size(); i++) {
					if(!listHasAlbum(lst, serverAlbums.get(i))) {
						lst.add(new SharedAlbum(serverAlbums.get(i)));
					}
				}
				
			} catch (Exception e) {
				System.err.println("Erro: " + e.getMessage());
				return null;
			}
			
		}
		return lst;
	}
	
	private boolean listHasAlbum(List<Album> listOfAlbums, String albumName) {
		for(Album a : listOfAlbums) {
			if(a.getName().equalsIgnoreCase(albumName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the list of pictures for the given album. 
	 * On error this method should return null.
	 */
	@Override
	public List<Picture> getListOfPictures(Album album) { 
		List<Picture> lst = new ArrayList<Picture>();
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				
				String[] pictures = target.path("/fileserverrest/listpictures/" + album.getName()).request().accept(MediaType.APPLICATION_JSON).get(String[].class);
				
				List<String> albumPictures = new ArrayList<String>();
				for(String s : pictures) {
					albumPictures.add(s);
				}
				
				for (String p : albumPictures) {
					if(!listHasPicture(lst, p)) {
						if(!p.endsWith(".deleted")) {
							lst.add( new SharedPicture(p));
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return lst;
	}
	
	private boolean listHasPicture(List<Picture> listOfPictures, String pictureName) {
		for(Picture p : listOfPictures) {
			if(p.getName().equalsIgnoreCase(pictureName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the contents of picture in album.
	 * On error this method should return null.
	 */
	@Override
	public byte[] getPictureData(Album album, Picture picture) {
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				
				Response res = target.path("/fileserverrest/haspicture/" + album.getName() + "/" + picture.getName()).request().get();
				
				if(res.getStatusInfo() == Status.ACCEPTED) {
					 return target.path("/fileserverrest/getpicture/" + album.getName() + "/" + picture.getName())
							 .request().accept(MediaType.APPLICATION_OCTET_STREAM)
							 .get(byte[].class);
				}
			} catch(Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Create a new album.
	 * On error this method should return null.
	 */
	@Override
	public Album createAlbum(String name) {
		WebTarget server = null;
		int photosOnServer = 0;
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				
				int res = target.path("/fileserverrest/checknumberofphotos").request().accept(MediaType.APPLICATION_JSON).get(Integer.class);
				
				
				if(server == null) {
					server = target;
					photosOnServer = res;
				} else if(photosOnServer > res) {
					server = target;
					photosOnServer = res;
				}
			} catch(Exception e) {
				return null;
			}
		}
		try {
			server.path("/fileserverrest/createalbum").request().post(Entity.entity(name, MediaType.APPLICATION_JSON));
		} catch(Exception e) {
			return null;
		}
		return new SharedAlbum(name);
	}

	/**
	 * Delete an existing album.
	 */
	@Override
	public void deleteAlbum(Album album) {
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				WebTarget target = client.target(getBaseURI(entry.getKey()));
				
				target.path("/fileserverrest/deletealbum/" + album.getName()).request().delete();
				
				gui.updateAlbum(album);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Add a new picture to an album.
	 * On error this method should return null.
	 */
	@Override
	public Picture uploadPicture(Album album, String name, byte[] data) {
		WebTarget server = null;
		int photosOnServer = 0;
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				
				int res = target.path("/fileserverrest/checknumberofphotos").request().accept(MediaType.APPLICATION_JSON).get(Integer.class);
				
				
				if(server == null) {
					server = target;
					photosOnServer = res;
				} else if(photosOnServer > res) {
					server = target;
					photosOnServer = res;
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		try {
			server.path("/fileserverrest/uploadpicture").request().post(Entity.entity(data, MediaType.APPLICATION_OCTET_STREAM));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
			
		
		return new SharedPicture(name);
	}

	/**
	 * Delete a picture from an album.
	 * On error this method should return false.
	 */
	@Override
	public boolean deletePicture(Album album, Picture picture) {
		try {
			for(Entry<String, Long> entry : serverStartTime.entrySet()) {
				WebTarget target = this.client.target(getBaseURI(entry.getKey()));
				Response res = target.path("/fileserverrest/haspicture/" + album.getName() + "/"+picture.getName()).request().get();
				if(res.getStatusInfo() == Status.ACCEPTED) {
					target.path("/fileserverrest/deletepicture/" + album.getName() + "/" + picture.getName()).request().delete();
				}
			}
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	private static URI getBaseURI(String url) {
	    return UriBuilder.fromUri(url).build();
	  }

	
	/**
	 * Represents a shared album.
	 */
	static class SharedAlbum implements GalleryContentProvider.Album {
		final String name;

		SharedAlbum(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	/**
	 * Represents a shared picture.
	 */
	static class SharedPicture implements GalleryContentProvider.Picture {
		final String name;

		SharedPicture(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
