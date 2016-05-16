package sd.tp1.srv;

@SuppressWarnings("serial")
public class NoAlbumFoundException extends Exception {
	public NoAlbumFoundException(String message) {
		super(message);
	}
}
