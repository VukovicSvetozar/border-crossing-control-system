/**
 * CentralniRegistarSOAP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.soap.centralregistry;

public interface CentralniRegistarSOAP extends java.rmi.Remote {
    public org.unibl.etf.model.TerminalDTO terminal(java.lang.String idTerminal) throws java.rmi.RemoteException;
    public void evidentirajProlazak(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException;
    public void evidentirajPotjernicu(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException;
    public void evidentirajDokumente(org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException;
    public boolean obrisiTerminal(java.lang.String idTerminal) throws java.rmi.RemoteException;
    public boolean izmjeniTerminal(org.unibl.etf.model.TerminalDTO terminal) throws java.rmi.RemoteException;
    public boolean provjeriTerminal(java.lang.String nazivTeminala) throws java.rmi.RemoteException;
    public org.unibl.etf.model.TerminalDTO[] terminali() throws java.rmi.RemoteException;
    public void dodajTerminal(org.unibl.etf.model.TerminalDTO terminal) throws java.rmi.RemoteException;
    public org.unibl.etf.model.ProlazDTO[] prolazi() throws java.rmi.RemoteException;
    public boolean aktivanProlaz(java.lang.String idProlaz) throws java.rmi.RemoteException;
    public org.unibl.etf.model.KorisnikDTO[] korisnici() throws java.rmi.RemoteException;
    public void postaviStatus(java.lang.String korisnickoIme, boolean aktivan) throws java.rmi.RemoteException;
}
