/**
 * TransitSOAPService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.soap.transit;

public interface TransitSOAPService extends javax.xml.rpc.Service {
    public java.lang.String getTransitSOAPAddress();

    public org.unibl.etf.soap.transit.TransitSOAP getTransitSOAP() throws javax.xml.rpc.ServiceException;

    public org.unibl.etf.soap.transit.TransitSOAP getTransitSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
