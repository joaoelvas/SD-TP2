
package sd.tp1.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "NoAlbumFoundException", targetNamespace = "http://srv.tp1.sd/")
public class NoAlbumFoundException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NoAlbumFoundException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public NoAlbumFoundException_Exception(String message, NoAlbumFoundException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public NoAlbumFoundException_Exception(String message, NoAlbumFoundException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: sd.tp1.ws.NoAlbumFoundException
     */
    public NoAlbumFoundException getFaultInfo() {
        return faultInfo;
    }

}
