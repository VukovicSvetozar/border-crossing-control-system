package org.unibl.etf.soap.transit;

public class TransitSOAPProxy implements org.unibl.etf.soap.transit.TransitSOAP {
  private String _endpoint = null;
  private org.unibl.etf.soap.transit.TransitSOAP transitSOAP = null;
  
  public TransitSOAPProxy() {
    _initTransitSOAPProxy();
  }
  
  public TransitSOAPProxy(String endpoint) {
    _endpoint = endpoint;
    _initTransitSOAPProxy();
  }
  
  private void _initTransitSOAPProxy() {
    try {
      transitSOAP = (new org.unibl.etf.soap.transit.TransitSOAPServiceLocator()).getTransitSOAP();
      if (transitSOAP != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)transitSOAP)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)transitSOAP)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (transitSOAP != null)
      ((javax.xml.rpc.Stub)transitSOAP)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.unibl.etf.soap.transit.TransitSOAP getTransitSOAP() {
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP;
  }
  
  public void dodajProlazak(java.lang.String idTerminal, java.lang.String idKontrola, org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    transitSOAP.dodajProlazak(idTerminal, idKontrola, prolazak);
  }
  
  public void azurirajProlazak(java.lang.String idKontrola, org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    transitSOAP.azurirajProlazak(idKontrola, prolazak);
  }
  
  public void odjavaKontrole(java.lang.String idKontrola, java.lang.String idTerminal) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    transitSOAP.odjavaKontrole(idKontrola, idTerminal);
  }
  
  public boolean provjeriDostupnost(java.lang.String idTerminal) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP.provjeriDostupnost(idTerminal);
  }
  
  public void promjeniDostupnost(java.lang.String idTerminal, boolean status) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    transitSOAP.promjeniDostupnost(idTerminal, status);
  }
  
  public org.unibl.etf.model.ProlazakDTO provjeriOsobu(java.lang.String idKontrola, java.lang.String statusProlaska) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP.provjeriOsobu(idKontrola, statusProlaska);
  }
  
  public java.lang.String provjeraStatusaKontrole(java.lang.String idKontrola) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP.provjeraStatusaKontrole(idKontrola);
  }
  
  public boolean registracijaKontrole(java.lang.String idKontrola, java.lang.String idTerminal) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP.registracijaKontrole(idKontrola, idTerminal);
  }
  
  public void azurirajStatusKontrole(java.lang.String idTerminal, java.lang.String idKontrola, java.lang.String status) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    transitSOAP.azurirajStatusKontrole(idTerminal, idKontrola, status);
  }
  
  public org.unibl.etf.model.ProlazakDTO azurirajInformacije(java.lang.String idKontrola, java.lang.String idProlazak) throws java.rmi.RemoteException{
    if (transitSOAP == null)
      _initTransitSOAPProxy();
    return transitSOAP.azurirajInformacije(idKontrola, idProlazak);
  }
  
  
}