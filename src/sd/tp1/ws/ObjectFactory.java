
package sd.tp1.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sd.tp1.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetPicture_QNAME = new QName("http://srv.tp1.sd/", "getPicture");
    private final static QName _NoAlbumFoundException_QNAME = new QName("http://srv.tp1.sd/", "NoAlbumFoundException");
    private final static QName _DeleteAlbum_QNAME = new QName("http://srv.tp1.sd/", "deleteAlbum");
    private final static QName _HasPicture_QNAME = new QName("http://srv.tp1.sd/", "hasPicture");
    private final static QName _ListAlbums_QNAME = new QName("http://srv.tp1.sd/", "listAlbums");
    private final static QName _UploadPictureResponse_QNAME = new QName("http://srv.tp1.sd/", "uploadPictureResponse");
    private final static QName _ListAlbumsResponse_QNAME = new QName("http://srv.tp1.sd/", "listAlbumsResponse");
    private final static QName _CreateAlbum_QNAME = new QName("http://srv.tp1.sd/", "createAlbum");
    private final static QName _HasAlbumResponse_QNAME = new QName("http://srv.tp1.sd/", "hasAlbumResponse");
    private final static QName _DeletePictureResponse_QNAME = new QName("http://srv.tp1.sd/", "deletePictureResponse");
    private final static QName _UploadPicture_QNAME = new QName("http://srv.tp1.sd/", "uploadPicture");
    private final static QName _CheckNumberOfPhotos_QNAME = new QName("http://srv.tp1.sd/", "checkNumberOfPhotos");
    private final static QName _GetPictureResponse_QNAME = new QName("http://srv.tp1.sd/", "getPictureResponse");
    private final static QName _NoPictureFoundException_QNAME = new QName("http://srv.tp1.sd/", "NoPictureFoundException");
    private final static QName _HasPictureResponse_QNAME = new QName("http://srv.tp1.sd/", "hasPictureResponse");
    private final static QName _DeleteAlbumResponse_QNAME = new QName("http://srv.tp1.sd/", "deleteAlbumResponse");
    private final static QName _CheckNumberOfPhotosResponse_QNAME = new QName("http://srv.tp1.sd/", "checkNumberOfPhotosResponse");
    private final static QName _ListPicturesFromAlbumResponse_QNAME = new QName("http://srv.tp1.sd/", "listPicturesFromAlbumResponse");
    private final static QName _HasAlbum_QNAME = new QName("http://srv.tp1.sd/", "hasAlbum");
    private final static QName _ListPicturesFromAlbum_QNAME = new QName("http://srv.tp1.sd/", "listPicturesFromAlbum");
    private final static QName _CreateAlbumResponse_QNAME = new QName("http://srv.tp1.sd/", "createAlbumResponse");
    private final static QName _DeletePicture_QNAME = new QName("http://srv.tp1.sd/", "deletePicture");
    private final static QName _GetPictureResponseReturn_QNAME = new QName("", "return");
    private final static QName _UploadPictureArg2_QNAME = new QName("", "arg2");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sd.tp1.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListPicturesFromAlbumResponse }
     * 
     */
    public ListPicturesFromAlbumResponse createListPicturesFromAlbumResponse() {
        return new ListPicturesFromAlbumResponse();
    }

    /**
     * Create an instance of {@link CheckNumberOfPhotosResponse }
     * 
     */
    public CheckNumberOfPhotosResponse createCheckNumberOfPhotosResponse() {
        return new CheckNumberOfPhotosResponse();
    }

    /**
     * Create an instance of {@link DeleteAlbumResponse }
     * 
     */
    public DeleteAlbumResponse createDeleteAlbumResponse() {
        return new DeleteAlbumResponse();
    }

    /**
     * Create an instance of {@link NoPictureFoundException }
     * 
     */
    public NoPictureFoundException createNoPictureFoundException() {
        return new NoPictureFoundException();
    }

    /**
     * Create an instance of {@link HasPictureResponse }
     * 
     */
    public HasPictureResponse createHasPictureResponse() {
        return new HasPictureResponse();
    }

    /**
     * Create an instance of {@link GetPictureResponse }
     * 
     */
    public GetPictureResponse createGetPictureResponse() {
        return new GetPictureResponse();
    }

    /**
     * Create an instance of {@link CheckNumberOfPhotos }
     * 
     */
    public CheckNumberOfPhotos createCheckNumberOfPhotos() {
        return new CheckNumberOfPhotos();
    }

    /**
     * Create an instance of {@link UploadPicture }
     * 
     */
    public UploadPicture createUploadPicture() {
        return new UploadPicture();
    }

    /**
     * Create an instance of {@link DeletePicture }
     * 
     */
    public DeletePicture createDeletePicture() {
        return new DeletePicture();
    }

    /**
     * Create an instance of {@link CreateAlbumResponse }
     * 
     */
    public CreateAlbumResponse createCreateAlbumResponse() {
        return new CreateAlbumResponse();
    }

    /**
     * Create an instance of {@link ListPicturesFromAlbum }
     * 
     */
    public ListPicturesFromAlbum createListPicturesFromAlbum() {
        return new ListPicturesFromAlbum();
    }

    /**
     * Create an instance of {@link HasAlbum }
     * 
     */
    public HasAlbum createHasAlbum() {
        return new HasAlbum();
    }

    /**
     * Create an instance of {@link HasPicture }
     * 
     */
    public HasPicture createHasPicture() {
        return new HasPicture();
    }

    /**
     * Create an instance of {@link ListAlbums }
     * 
     */
    public ListAlbums createListAlbums() {
        return new ListAlbums();
    }

    /**
     * Create an instance of {@link UploadPictureResponse }
     * 
     */
    public UploadPictureResponse createUploadPictureResponse() {
        return new UploadPictureResponse();
    }

    /**
     * Create an instance of {@link DeleteAlbum }
     * 
     */
    public DeleteAlbum createDeleteAlbum() {
        return new DeleteAlbum();
    }

    /**
     * Create an instance of {@link NoAlbumFoundException }
     * 
     */
    public NoAlbumFoundException createNoAlbumFoundException() {
        return new NoAlbumFoundException();
    }

    /**
     * Create an instance of {@link GetPicture }
     * 
     */
    public GetPicture createGetPicture() {
        return new GetPicture();
    }

    /**
     * Create an instance of {@link DeletePictureResponse }
     * 
     */
    public DeletePictureResponse createDeletePictureResponse() {
        return new DeletePictureResponse();
    }

    /**
     * Create an instance of {@link CreateAlbum }
     * 
     */
    public CreateAlbum createCreateAlbum() {
        return new CreateAlbum();
    }

    /**
     * Create an instance of {@link HasAlbumResponse }
     * 
     */
    public HasAlbumResponse createHasAlbumResponse() {
        return new HasAlbumResponse();
    }

    /**
     * Create an instance of {@link ListAlbumsResponse }
     * 
     */
    public ListAlbumsResponse createListAlbumsResponse() {
        return new ListAlbumsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "getPicture")
    public JAXBElement<GetPicture> createGetPicture(GetPicture value) {
        return new JAXBElement<GetPicture>(_GetPicture_QNAME, GetPicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoAlbumFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "NoAlbumFoundException")
    public JAXBElement<NoAlbumFoundException> createNoAlbumFoundException(NoAlbumFoundException value) {
        return new JAXBElement<NoAlbumFoundException>(_NoAlbumFoundException_QNAME, NoAlbumFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "deleteAlbum")
    public JAXBElement<DeleteAlbum> createDeleteAlbum(DeleteAlbum value) {
        return new JAXBElement<DeleteAlbum>(_DeleteAlbum_QNAME, DeleteAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HasPicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "hasPicture")
    public JAXBElement<HasPicture> createHasPicture(HasPicture value) {
        return new JAXBElement<HasPicture>(_HasPicture_QNAME, HasPicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAlbums }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "listAlbums")
    public JAXBElement<ListAlbums> createListAlbums(ListAlbums value) {
        return new JAXBElement<ListAlbums>(_ListAlbums_QNAME, ListAlbums.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadPictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "uploadPictureResponse")
    public JAXBElement<UploadPictureResponse> createUploadPictureResponse(UploadPictureResponse value) {
        return new JAXBElement<UploadPictureResponse>(_UploadPictureResponse_QNAME, UploadPictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAlbumsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "listAlbumsResponse")
    public JAXBElement<ListAlbumsResponse> createListAlbumsResponse(ListAlbumsResponse value) {
        return new JAXBElement<ListAlbumsResponse>(_ListAlbumsResponse_QNAME, ListAlbumsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "createAlbum")
    public JAXBElement<CreateAlbum> createCreateAlbum(CreateAlbum value) {
        return new JAXBElement<CreateAlbum>(_CreateAlbum_QNAME, CreateAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HasAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "hasAlbumResponse")
    public JAXBElement<HasAlbumResponse> createHasAlbumResponse(HasAlbumResponse value) {
        return new JAXBElement<HasAlbumResponse>(_HasAlbumResponse_QNAME, HasAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "deletePictureResponse")
    public JAXBElement<DeletePictureResponse> createDeletePictureResponse(DeletePictureResponse value) {
        return new JAXBElement<DeletePictureResponse>(_DeletePictureResponse_QNAME, DeletePictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadPicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "uploadPicture")
    public JAXBElement<UploadPicture> createUploadPicture(UploadPicture value) {
        return new JAXBElement<UploadPicture>(_UploadPicture_QNAME, UploadPicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckNumberOfPhotos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "checkNumberOfPhotos")
    public JAXBElement<CheckNumberOfPhotos> createCheckNumberOfPhotos(CheckNumberOfPhotos value) {
        return new JAXBElement<CheckNumberOfPhotos>(_CheckNumberOfPhotos_QNAME, CheckNumberOfPhotos.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "getPictureResponse")
    public JAXBElement<GetPictureResponse> createGetPictureResponse(GetPictureResponse value) {
        return new JAXBElement<GetPictureResponse>(_GetPictureResponse_QNAME, GetPictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoPictureFoundException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "NoPictureFoundException")
    public JAXBElement<NoPictureFoundException> createNoPictureFoundException(NoPictureFoundException value) {
        return new JAXBElement<NoPictureFoundException>(_NoPictureFoundException_QNAME, NoPictureFoundException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HasPictureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "hasPictureResponse")
    public JAXBElement<HasPictureResponse> createHasPictureResponse(HasPictureResponse value) {
        return new JAXBElement<HasPictureResponse>(_HasPictureResponse_QNAME, HasPictureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "deleteAlbumResponse")
    public JAXBElement<DeleteAlbumResponse> createDeleteAlbumResponse(DeleteAlbumResponse value) {
        return new JAXBElement<DeleteAlbumResponse>(_DeleteAlbumResponse_QNAME, DeleteAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckNumberOfPhotosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "checkNumberOfPhotosResponse")
    public JAXBElement<CheckNumberOfPhotosResponse> createCheckNumberOfPhotosResponse(CheckNumberOfPhotosResponse value) {
        return new JAXBElement<CheckNumberOfPhotosResponse>(_CheckNumberOfPhotosResponse_QNAME, CheckNumberOfPhotosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPicturesFromAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "listPicturesFromAlbumResponse")
    public JAXBElement<ListPicturesFromAlbumResponse> createListPicturesFromAlbumResponse(ListPicturesFromAlbumResponse value) {
        return new JAXBElement<ListPicturesFromAlbumResponse>(_ListPicturesFromAlbumResponse_QNAME, ListPicturesFromAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HasAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "hasAlbum")
    public JAXBElement<HasAlbum> createHasAlbum(HasAlbum value) {
        return new JAXBElement<HasAlbum>(_HasAlbum_QNAME, HasAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPicturesFromAlbum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "listPicturesFromAlbum")
    public JAXBElement<ListPicturesFromAlbum> createListPicturesFromAlbum(ListPicturesFromAlbum value) {
        return new JAXBElement<ListPicturesFromAlbum>(_ListPicturesFromAlbum_QNAME, ListPicturesFromAlbum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateAlbumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "createAlbumResponse")
    public JAXBElement<CreateAlbumResponse> createCreateAlbumResponse(CreateAlbumResponse value) {
        return new JAXBElement<CreateAlbumResponse>(_CreateAlbumResponse_QNAME, CreateAlbumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePicture }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://srv.tp1.sd/", name = "deletePicture")
    public JAXBElement<DeletePicture> createDeletePicture(DeletePicture value) {
        return new JAXBElement<DeletePicture>(_DeletePicture_QNAME, DeletePicture.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetPictureResponse.class)
    public JAXBElement<byte[]> createGetPictureResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetPictureResponseReturn_QNAME, byte[].class, GetPictureResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = UploadPicture.class)
    public JAXBElement<byte[]> createUploadPictureArg2(byte[] value) {
        return new JAXBElement<byte[]>(_UploadPictureArg2_QNAME, byte[].class, UploadPicture.class, ((byte[]) value));
    }

}
