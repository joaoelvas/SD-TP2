package sd.tp1;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import sd.tp1.gui.GalleryContentProvider;
import sd.tp1.gui.Gui;
import sd.tp1.ws.FileServerImplWS;
import sd.tp1.ws.FileServerImplWSService;
import sd.tp1.ws.NoAlbumFoundException_Exception;
import sd.tp1.ws.NoPictureFoundException_Exception;

/*
 * This class provides the album/picture content to the gui/main application.
 * 
 * Project 1 implementation should complete this class. 
 */
public class SharedGalleryContentProvider implements GalleryContentProvider{

	private static final int SERVER_PORT = 9000;
	private static final String SERVER_ADDRESS = "224.10.10.10";
	private static final String SERVER_KIND = "albumserver";
	
	Gui gui;	
	
	String serviceURL;
	
	MulticastSocket socket;
	
	Map<String, Long> serverStartTime;
	
	SharedGalleryContentProvider() {
		// TODO: code to do when shared gallery starts
		serverStartTime = new ConcurrentHashMap<>();
		
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
				URL url = new URL(entry.getKey());
				FileServerImplWSService service = new FileServerImplWSService(url);
				FileServerImplWS server = service.getFileServerImplWSPort();
				
				List<String> serverAlbums = server.listAlbums();
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
				
				URL url = new URL(entry.getKey());
				FileServerImplWSService service = new FileServerImplWSService(url);
				FileServerImplWS server = service.getFileServerImplWSPort();
				
				List<String> albumPictures = server.listPicturesFromAlbum(album.getName());
				for (String p : albumPictures) {
					if(!listHasPicture(lst, p)) {
						if(!p.endsWith(".deleted")) {
							lst.add( new SharedPicture(p));
						}
					}
				}
			} catch(NoAlbumFoundException_Exception e) {
				continue;
			}
				catch(Exception e) {
				e.printStackTrace();
				//return null;
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
			
				URL url = new URL(entry.getKey());
				FileServerImplWSService service = new FileServerImplWSService(url);
				FileServerImplWS s = service.getFileServerImplWSPort();
				if(s.hasAlbum(album.getName())) {
					if(s.hasPicture(album.getName(), picture.getName())) {
						return s.getPicture(album.getName(), picture.getName());
					}
				}
			} catch(NoAlbumFoundException_Exception e) {
				continue;
			} catch(NoPictureFoundException_Exception e) {
				continue;
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
		FileServerImplWS server = null;
		for(Entry<String, Long> entry : serverStartTime.entrySet()) {
			try {
				URL url = new URL(entry.getKey());
				FileServerImplWSService service = new FileServerImplWSService(url);
				FileServerImplWS s = service.getFileServerImplWSPort();
				if(server == null) {
					server = s;
				} else if(server.checkNumberOfPhotos() > s.checkNumberOfPhotos()) {
					server = s;
				}
			} catch(Exception e) {
				return null;
			}
		}
		try {
			server.createAlbum(name);
		} catch(SecurityException e) {
			System.err.println("Security Exception raised!");
			return null;
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
				URL url = new URL(entry.getKey());
				FileServerImplWSService service = new FileServerImplWSService(url);
				FileServerImplWS s = service.getFileServerImplWSPort();
				s.deleteAlbum(album.getName());
				gui.updateAlbum(album);
			} catch(NoAlbumFoundException_Exception e) {
				continue;
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
			FileServerImplWS server = null;
			for(Entry<String, Long> entry : serverStartTime.entrySet()) {
				try {
					URL url = new URL(entry.getKey());
					FileServerImplWSService service = new FileServerImplWSService(url);
					FileServerImplWS s = service.getFileServerImplWSPort();
					if(server == null) {
						server = s;
					} else if(server.checkNumberOfPhotos() > s.checkNumberOfPhotos()) {
						server = s;
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			try {
				server.uploadPicture(album.getName(), name, data);
			} catch(NoAlbumFoundException_Exception e) {
				this.createAlbum(album.getName());
				try {
					server.uploadPicture(album.getName(), name, data);
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
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
				try {
					URL url = new URL(entry.getKey());
					FileServerImplWSService service = new FileServerImplWSService(url);
					FileServerImplWS s = service.getFileServerImplWSPort();
					if(s.hasPicture(album.getName(), picture.getName())) {
						s.deletePicture(album.getName(), picture.getName());
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		} catch(Exception e) {
			return false;
		}
		return true;
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
