/**
 * CentralniRegistarSOAPServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.soap.centralregistry;

@SuppressWarnings("serial")
public class CentralniRegistarSOAPServiceLocator extends org.apache.axis.client.Service implements org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPService {

    public CentralniRegistarSOAPServiceLocator() {
    }


    public CentralniRegistarSOAPServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CentralniRegistarSOAPServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CentralniRegistarSOAP
    private java.lang.String CentralniRegistarSOAP_address = "http://localhost:8080/Centralni_Registar/services/CentralniRegistarSOAP";

    public java.lang.String getCentralniRegistarSOAPAddress() {
        return CentralniRegistarSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CentralniRegistarSOAPWSDDServiceName = "CentralniRegistarSOAP";

    public java.lang.String getCentralniRegistarSOAPWSDDServiceName() {
        return CentralniRegistarSOAPWSDDServiceName;
    }

    public void setCentralniRegistarSOAPWSDDServiceName(java.lang.String name) {
        CentralniRegistarSOAPWSDDServiceName = name;
    }

    public org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP getCentralniRegistarSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CentralniRegistarSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCentralniRegistarSOAP(endpoint);
    }

    public org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP getCentralniRegistarSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPSoapBindingStub _stub = new org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPSoapBindingStub(portAddress, this);
            _stub.setPortName(getCentralniRegistarSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCentralniRegistarSOAPEndpointAddress(java.lang.String address) {
        CentralniRegistarSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.unibl.etf.soap.centralregistry.CentralniRegistarSOAP.class.isAssignableFrom(serviceEndpointInterface)) {
                org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPSoapBindingStub _stub = new org.unibl.etf.soap.centralregistry.CentralniRegistarSOAPSoapBindingStub(new java.net.URL(CentralniRegistarSOAP_address), this);
                _stub.setPortName(getCentralniRegistarSOAPWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CentralniRegistarSOAP".equals(inputPortName)) {
            return getCentralniRegistarSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://centralregistry.soap.etf.unibl.org", "CentralniRegistarSOAPService");
    }

    @SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://centralregistry.soap.etf.unibl.org", "CentralniRegistarSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CentralniRegistarSOAP".equals(portName)) {
            setCentralniRegistarSOAPEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
