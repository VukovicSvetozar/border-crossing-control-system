package org.unibl.etf.soap.centralregistry;

public class CentralniRegistarSOAPProxy implements org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP {
  private String _endpoint = null;
  private org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP centralniRegistarSOAP = null;
  
  public CentralniRegistarSOAPProxy() {
    _initCentralniRegistarSOAPProxy();
  }
  
  public CentralniRegistarSOAPProxy(String endpoint) {
    _endpoint = endpoint;
    _initCentralniRegistarSOAPProxy();
  }
  
  private void _initCentralniRegistarSOAPProxy() {
    try {
      centralniRegistarSOAP = (new org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPServiceLocator()).getCentralniRegistarSOAP();
      if (centralniRegistarSOAP != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)centralniRegistarSOAP)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)centralniRegistarSOAP)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (centralniRegistarSOAP != null)
      ((javax.xml.rpc.Stub)centralniRegistarSOAP)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP getCentralniRegistarSOAP() {
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP;
  }
  
  public org.unibl.etf.model.TerminalDTO terminal(java.lang.String idTerminal) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.terminal(idTerminal);
  }
  
  public void evidentirajProlazak(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    centralniRegistarSOAP.evidentirajProlazak(prolazak);
  }
  
  public void evidentirajPotjernicu(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    centralniRegistarSOAP.evidentirajPotjernicu(prolazak);
  }
  
  public void evidentirajDokumente(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    centralniRegistarSOAP.evidentirajDokumente(prolazak);
  }
  
  public boolean obrisiTerminal(java.lang.String idTerminal) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.obrisiTerminal(idTerminal);
  }
  
  public boolean izmjeniTerminal(org.unibl.etf.model.TerminalDTO terminal) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.izmjeniTerminal(terminal);
  }
  
  public boolean provjeriTerminal(java.lang.String nazivTeminala) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.provjeriTerminal(nazivTeminala);
  }
  
  public org.unibl.etf.model.TerminalDTO[] terminali() throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.terminali();
  }
  
  public void dodajTerminal(org.unibl.etf.model.TerminalDTO terminal) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    centralniRegistarSOAP.dodajTerminal(terminal);
  }
  
  public org.unibl.etf.model.ProlazDTO[] prolazi() throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.prolazi();
  }
  
  public boolean aktivanProlaz(java.lang.String idProlaz) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.aktivanProlaz(idProlaz);
  }
  
  public org.unibl.etf.model.KorisnikDTO[] korisnici() throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    return centralniRegistarSOAP.korisnici();
  }
  
  public void postaviStatus(java.lang.String korisnickoIme, boolean aktivan) throws java.rmi.RemoteException{
    if (centralniRegistarSOAP == null)
      _initCentralniRegistarSOAPProxy();
    centralniRegistarSOAP.postaviStatus(korisnickoIme, aktivan);
  }
  
  
}