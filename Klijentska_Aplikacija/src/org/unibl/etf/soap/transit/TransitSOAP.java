/**
 * TransitSOAP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.soap.transit;

public interface TransitSOAP extends java.rmi.Remote {
    public void dodajProlazak(java.lang.String idTerminal, java.lang.String idKontrola, org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException;
    public void azurirajProlazak(java.lang.String idKontrola, org.unibl.etf.model.ProlazakDTO prolazak) throws java.rmi.RemoteException;
    public void odjavaKontrole(java.lang.String idKontrola, java.lang.String idTerminal) throws java.rmi.RemoteException;
    public boolean provjeriDostupnost(java.lang.String idTerminal) throws java.rmi.RemoteException;
    public void promjeniDostupnost(java.lang.String idTerminal, boolean status) throws java.rmi.RemoteException;
    public org.unibl.etf.model.ProlazakDTO provjeriOsobu(java.lang.String idKontrola, java.lang.String statusProlaska) throws java.rmi.RemoteException;
    public java.lang.String provjeraStatusaKontrole(java.lang.String idKontrola) throws java.rmi.RemoteException;
    public boolean registracijaKontrole(java.lang.String idKontrola, java.lang.String idTerminal) throws java.rmi.RemoteException;
    public void azurirajStatusKontrole(java.lang.String idTerminal, java.lang.String idKontrola, java.lang.String status) throws java.rmi.RemoteException;
    public org.unibl.etf.model.ProlazakDTO azurirajInformacije(java.lang.String idKontrola, java.lang.String idProlazak) throws java.rmi.RemoteException;
}
