package sd.tp1.srv;

import com.github.scribejava.apis.ImgurApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.ws.Endpoint;

import org.glassfish.jersey.client.ClientConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Joao Elvas
 * @author Rodolfo Ferreira
 */
@WebService
public class FileServerImplWS {
	
	private static final String SERVER_ADDRESS = "224.10.10.10";
	private static final String SERVER_KIND = "albumserver";
	private static final String apiKey = "7b4fa3c2f331d79";
	private static final String apiSecret = "972fde05404c4927dbf0654198fa9916607d38d3"; 
	private static final String IMGUR_API_URL = "https://api.imgur.com/3";
	
	private OAuth20Service service;
	private OAuth2AccessToken accessToken;
	Client client;
	ClientConfig config;
	
	private File basePath;
	
	public FileServerImplWS() {
		this.config = new ClientConfig();
		this.client = ClientBuilder.newClient(config);
		
		OAuth20Service service = new ServiceBuilder().apiKey(apiKey).apiSecret(apiSecret)
				.build(ImgurApi.instance());
		final Scanner in = new Scanner(System.in);

		// Obtain the Authorization URL
		System.out.println("A obter o Authorization URL...");
		final String authorizationUrl = service.getAuthorizationUrl();
		System.out.println("Necessario dar permissao neste URL:");
		System.out.println(authorizationUrl);
		System.out.println("e copiar o codigo obtido para aqui:");
		System.out.print(">>");
		final String code = in.nextLine();
		
		// Trade the Request Token and Verifier for the Access Token
		System.out.println("A obter o Access Token!");
		accessToken = service.getAccessToken(code);
	}
	
	/**
	 * Returns the name of all albums on this server
	 * @return
	 */
	@WebMethod
	public String[] listAlbums() {
		String[] l = new String[0];
		System.out.println("Agora vamos aceder aos albuns dum utilizador...");
		OAuthRequest albumsReq = new OAuthRequest(Verb.GET,
				"https://api.imgur.com/3/account/joaoelvas/albums/ids", service);
		service.signRequest(accessToken, albumsReq);
		final Response albumsRes = albumsReq.send();
		System.out.println(albumsRes.getCode());
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject res = (JSONObject) parser.parse(albumsRes.getBody());
			
			JSONArray albums = (JSONArray) res.get("data");
			l = new String[albums.size()];
			int i = 0;
			Iterator albumsIt = albums.iterator();
			while (albumsIt.hasNext()) {
				OAuthRequest albumReq = new OAuthRequest(Verb.GET,
						"https://api.imgur.com/3/album/" + albumsIt.next(), service);
				service.signRequest(accessToken, albumReq);
				final Response albumRes = albumReq.send();
				JSONParser parse = new JSONParser();
				JSONObject resp = (JSONObject) parse.parse(albumRes.getBody());
				JSONObject album = (JSONObject) resp.get("data");
				l[i] = (String) album.get("title");
				i++;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		
		return l;
	}
	
	/**
	 * Returns if album @name is on server
	 * @param name
	 * @return
	 */
	@WebMethod
	public boolean hasAlbum(String name) {
		File f = new File(basePath, ".");
		if (f.exists() && f.isDirectory()) {
			String[] fList = f.list();
			for(int i = 0; i < fList.length; i++) {
				if(fList[i].equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		return false;
			
	}
	
	/**
	 * Returns if picture @pictureName is on server
	 * @param albumName
	 * @param pictureName
	 * @return
	 * @throws InfoNotFoundException
	 */
	@WebMethod
	public boolean hasPicture(String albumName, String pictureName) throws NoAlbumFoundException {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory()) {
			for(int i = 0; i < f.list().length; i++) {
				if(f.list()[i].equals(pictureName)) {
					return true;
				}
			}
		} else throw new NoAlbumFoundException("Album not found!");
		return false;
	}
	
	/**
	 * Returns the name of all pictures from an album @album on this server
	 * @param album
	 * @return
	 * @throws InfoNotFoundException
	 */
	@WebMethod
	public String[] listPicturesFromAlbum(String album) throws NoAlbumFoundException {
		File f = new File(basePath, album);
		if (f.exists() && f.isDirectory())
			return f.list();
		else
			throw new NoAlbumFoundException("Album not found!");
	}
	
	/**
	 * This method creates an album with the name @albumName
	 * @param albumName
	 * @throws SecurityException
	 */
	@WebMethod
	public void createAlbum(String albumName) throws SecurityException {
		File f = new File(basePath,albumName);
		
		if (!f.exists()) {
		    boolean result = false;

		    try{
		        f.mkdir();
		        result = true;
		    } 
		    catch(SecurityException e){
		    	throw new SecurityException();
		    }        
		    if(result) {    
		        System.out.println("Album created");  
		    }
		}
	}
	
	/**
	 * Deletes an album with the name @albumName
	 * @param albumName
	 * @throws NoAlbumFoundException
	 */
	@WebMethod
	public void deleteAlbum(String albumName) throws NoAlbumFoundException {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			f.renameTo(new File(f.getAbsolutePath() + ".deleted"));
		} else {
			throw new NoAlbumFoundException("Album not found!");
		}
	}
	
	/**
	 * This method deletes a picture with the name @albumName
	 * @param albumName
	 * @param pictureName
	 * @throws NoAlbumFoundException
	 * @throws NoPictureFoundException 
	 */
	@WebMethod
	public void deletePicture(String albumName, String pictureName) throws NoAlbumFoundException, NoPictureFoundException {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			String[] a = f.list();
			for (int i = 0; i < a.length; i++) {
				if(a[i].equalsIgnoreCase(pictureName)) {
					File p = new File(f.getAbsolutePath(), pictureName);
					if(p.exists() && !isDeleted(p)) {
						p.renameTo(new File(p.getAbsolutePath() + ".deleted"));
					} else throw new NoPictureFoundException("Picture not found!");
					
				}
			}
		} else if(!f.exists()) throw new NoAlbumFoundException("Album not found!");
	}
	
	/**
	 * This method sends the picture to the client as a byte[]
	 * @param albumName
	 * @param pictureName
	 * @return
	 * @throws NoAlbumFoundException 
	 * @throws NoPictureFoundException 
	 */
	@WebMethod
	public byte[] getPicture(String albumName, String pictureName) throws NoAlbumFoundException, NoPictureFoundException {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			File p = new File(f.getAbsolutePath(),pictureName);
			
			if(!p.exists()) {
				throw new NoPictureFoundException("Picture not found!");
			}
			
			byte[] bFile = new byte[(int) p.length()];
			FileInputStream fileInputStream = null;
			try	{
			     //convert file into array of bytes
			     fileInputStream = new FileInputStream(p);
			     fileInputStream.read(bFile);
			     fileInputStream.close();
			} catch (Exception e)	{
			     e.printStackTrace();
			}
			return bFile;
		} else if(!f.exists()) throw new NoAlbumFoundException("Album not found!");
		return null;
	}
	
	/**
	 * This method receives the picture @data from the client as a byte[]
	 * @param albumName
	 * @param pictureName
	 * @param data
	 * @throws NoAlbumFoundException
	 */
	@WebMethod
	public void uploadPicture(String albumName, String pictureName, byte[] data) throws NoAlbumFoundException {
		File f = new File(basePath, albumName);
		if(f.isDirectory()) {
			File p = new File(f.getAbsolutePath(),pictureName);
			if(!p.exists()) {
				try {
					RandomAccessFile pi = new RandomAccessFile(p, "rw");
					
					pi.write(data.length);
					pi.close();
					
				} catch (Exception e) {
					System.err.println("Erro: " + e.getMessage());
				}
			}
		} else throw new NoAlbumFoundException("Album not found!");
		
		
	}
	
	@WebMethod
	public int checkNumberOfPhotos() {
		int photos = 0;
		String[] lst = listAlbums();
		for(int i = 0;i < lst.length;i++) {
			File p = new File(basePath,lst[i]);
			photos += p.list().length;
		}
		return photos;
	}
	
	/**
	 * This method checks id the given file @f as an extension of .deleted
	 * @param f
	 * @return
	 */
	private boolean isDeleted(File f) {
		if(f.getAbsolutePath().endsWith(".deleted")) return true;
		return false;
	}
 	
	public static void main(String args[]) throws Exception {
		String path = args.length > 0 ? args[0] : ".";
		
		String port = "8080";

		
		Endpoint.publish("http://0.0.0.0:"+ port +"/FileServer", new FileServerImplWS());
		System.err.println("FileServer started");
		
		// Set multicast address
		final InetAddress address = InetAddress.getByName(SERVER_ADDRESS);
		
		
		@SuppressWarnings("resource")
		MulticastSocket socket = new MulticastSocket(9000);
		socket.joinGroup(address);
		while(true) {
			byte[] buffer = new byte[65536];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length); 
			socket.receive(packet);
			
			String s = new String(packet.getData(), 0, packet.getLength());
			//System.err.println("1");
			if(s.equalsIgnoreCase(SERVER_KIND)) {
				String url = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port +"/FileServer";
				byte[] b = url.getBytes();
				DatagramPacket packetAnswer = new DatagramPacket(b, b.length);
				packetAnswer.setAddress(packet.getAddress());
				packetAnswer.setPort(packet.getPort());
				socket.send(packetAnswer);
			}
		}
		
	}
}
