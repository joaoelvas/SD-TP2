package sd.tp1.srv;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

@Path("/fileserverrest")
public class FileServerImplREST {
	
	private static final String SERVER_ADDRESS = "233.10.10.10";
	private static final String SERVER_KIND = "albumserver";
	
	private static File basePath;
	
	/**
	 * Returns the name of all albums on this server
	 * @return
	 */
	@GET
	@Path("/listalbums")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAlbums() {
		File f = new File(basePath, ".");
		String[] s = new String[0];
		if (f.exists() && f.isDirectory()) {
			s = f.list();
		}
		return Response.ok(s).build();
	}
	
	/**
	 * Returns if album @name is on server
	 * @param name
	 * @return
	 */
	@GET
	@Path("/hasalbums/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response hasAlbum(@PathParam("name") String name) {
		File f = new File(basePath, ".");
		if (f.exists() && f.isDirectory()) {
			String[] fList = f.list();
			for(int i = 0; i < fList.length; i++) {
				if(fList[i].equalsIgnoreCase(name)) {
					return Response.status(javax.ws.rs.core.Response.Status.ACCEPTED).build();
				}
			}
		}
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
			
	}
	
	/**
	 * Returns if picture @pictureName is on server
	 * @param albumName
	 * @param pictureName
	 * @return
	 * @throws InfoNotFoundException
	 */
	@GET
	@Path("/haspicture/{albumName}/{pictureName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response hasPicture(@PathParam("albumName") String albumName, @PathParam("pictureName") String pictureName) throws NoAlbumFoundException {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory()) {
			for(int i = 0; i < f.list().length; i++) {
				if(f.list()[i].equals(pictureName)) {
					return Response.status(javax.ws.rs.core.Response.Status.ACCEPTED).build();
				}
			}
		} else Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
	}
	
	/**
	 * Returns the name of all pictures from an album @album on this server
	 * @param album
	 * @return
	 * @throws InfoNotFoundException
	 */
	@GET
	@Path("/listpicturesfromalbum/{album}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listPicturesFromAlbum(@PathParam("album") String album) {
		File f = new File(basePath, album);
		if (f.exists() && f.isDirectory())
			return Response.ok(f.list()).build();
		else
			return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
	}
	
	/**
	 * This method creates an album with the name @albumName
	 * @param albumName
	 * @throws SecurityException
	 */
	@POST
	@Path("/createalbum")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createAlbum(String albumName) {
		File f = new File(basePath,albumName);
		
		if (!f.exists()) {
		    boolean result = false;

		    try{
		        f.mkdir();
		        result = true;
		    } 
		    catch(SecurityException e){
		    	return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		    }        
		    if(result) {
		    	System.out.println("Album created");  
		    	return Response.status(javax.ws.rs.core.Response.Status.OK).build();  
		    } 
		}
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		
	}
	
	/**
	 * Deletes an album with the name @albumName
	 * @param albumName
	 * @throws NoAlbumFoundException
	 */
	@DELETE
	@Path("/deletealbum/{albumName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAlbum(@PathParam("albumName") String albumName) {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			f.renameTo(new File(f.getAbsolutePath() + ".deleted"));
			return Response.ok().build();
		} else {
			return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		}
	}
	
	/**
	 * This method deletes a picture with the name @albumName
	 * @param albumName
	 * @param pictureName
	 * @throws NoAlbumFoundException
	 * @throws NoPictureFoundException 
	 */
	@DELETE
	@Path("/deletepicture/{albumName}/{pictureName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePicture(@PathParam("albumName") String albumName, @PathParam("pictureName") String pictureName) {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			String[] a = f.list();
			for (int i = 0; i < a.length; i++) {
				if(a[i].equalsIgnoreCase(pictureName)) {
					File p = new File(f.getAbsolutePath(), pictureName);
					if(p.exists() && !isDeleted(p)) {
						p.renameTo(new File(p.getAbsolutePath() + ".deleted"));
						return Response.ok().build();
					} else return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
					
				}
			}
		} else if(!f.exists()) return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
	}
	
	/**
	 * This method sends the picture to the client as a byte[]
	 * @param albumName
	 * @param pictureName
	 * @return
	 * @throws NoAlbumFoundException 
	 * @throws NoPictureFoundException 
	 */
	@GET
	@Path("/getpicture/{albumName}/{pictureName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getPicture(@PathParam("albumName") String albumName, @PathParam("pictureName") String pictureName) {
		File f = new File(basePath, albumName);
		if (f.exists() && f.isDirectory() && !isDeleted(f)) {
			File p = new File(f.getAbsolutePath(),pictureName);
			
			if(!p.exists()) {
				return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
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
			return Response.ok(bFile).build();
		} else if(!f.exists()) return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
	}
	
	/**
	 * This method receives the picture @data from the client as a byte[]
	 * @param albumName
	 * @param pictureName
	 * @param data
	 * @throws NoAlbumFoundException
	 */
	@POST
	@Path("/getpictue/{albumName}/{pictureName}")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response uploadPicture(@PathParam("albumName")String albumName, @PathParam("pictureName")String pictureName, byte[] data) {
		File f = new File(basePath, albumName);
		if(f.isDirectory()) {
			File p = new File(f.getAbsolutePath(),pictureName);
			if(!p.exists()) {
				try {
					RandomAccessFile pi = new RandomAccessFile(p, "rw");
					
					pi.write(data.length);
					pi.close();
					Response.ok().build();
					
				} catch (Exception e) {
					System.err.println("Erro: " + e.getMessage());
				}
			}
		}
		
		return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
	}
	
	@GET
	@Path("/checknumberofphotos")
	@Produces(MediaType.APPLICATION_JSON)
	public int checkNumberOfPhotos() {
		int photos = 0;
		String[] lst = lstOfAlbums();
		for(int i = 0;i < lst.length;i++) {
			File p = new File(basePath,lst[i]);
			photos += p.list().length;
		}
		return photos;
	}
	
	private String[] lstOfAlbums() {
		File f = new File(basePath, ".");
		String[] s = new String[0];
		if (f.exists() && f.isDirectory()) {
			s = f.list();
		}
		return s;
	}
	
	@GET
	@Path("/search/{pattern}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("pattern") String pattern) {
		System.out.println("cc");
		try {
		File f = new File(basePath, ".");
		List<String> lst = new ArrayList<String>();
		if (f.exists() && f.isDirectory()) {
			String[] s = f.list();
			for(int i = 0; i < s.length; i++) {
				File a = new File(basePath, s[i]);
				if(a.exists() && a.isDirectory()) {
					String[] p = a.list();
					
					for(int j = 0; j < p.length; j++) {
						System.out.println(p[j]);
						System.out.println(pattern);
						if(p[j].toLowerCase().contains(pattern.toLowerCase())) {
							lst.add("http://0.0.0.0:8080/fileserverrest/getx/"+ s[i] + "/" + p[j]);
							System.out.println("cucu");
						}
					}
				}
				
			}
			Response r = Response.ok(lst.toArray()).build();
			r.getHeaders().add("Access-Control-Allow-Origin", "*");
			System.err.println(lst.size());
			return r;
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		Response ri = Response.ok(new String[0]).build();
		ri.getHeaders().add("Access-Control-Allow-Origin", "*");
		System.out.println("caca");
		return ri;
	}
	
	@Path("/getx/{album}/{file}")
	public Response getx(@PathParam("album")String album, @PathParam("file") String file)	{
		System.out.println("getx");
		File f = new File(basePath, album);
		if (f.exists() && f.isDirectory()) {
			String[] a = f.list();
			for (int i = 0; i < a.length; i++) {
				if(a[i].equalsIgnoreCase(file)) {
					File p = new File(f.getAbsolutePath(), file);
					if(p.exists()) {
						Response r = Response.ok(p).build();
						r.getHeaders().add("Access-Control-Allow-Origin", "*");
						return r;
					} else return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
					
				}
			}
		} else if(!f.exists()) return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).build();
		return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).build();
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
		
		String port = "";
		
		URI uri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
		
		if(args.length == 1) {
			port = args[0];
			int p = Integer.parseInt(port);
			uri = UriBuilder.fromUri("http://0.0.0.0/").port(p).build();
			basePath = new File(".");
		}
		
		if(args.length >= 2) {
			path = args[0];
			port = args[1];
			int p = Integer.parseInt(port);
			uri = UriBuilder.fromUri("http://0.0.0.0/").port(p).build();
			basePath = new File(path);
		}
		
		ResourceConfig config = new ResourceConfig();
		
		config.register(FileServerImplREST.class);
		
		HttpServer server = JdkHttpServerFactory.createHttpServer(uri, config);
		
		System.err.println("FileServerRest started");
		
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
