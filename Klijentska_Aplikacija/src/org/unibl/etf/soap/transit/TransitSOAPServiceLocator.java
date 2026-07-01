/**
 * TransitSOAPServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.soap.transit;

@SuppressWarnings("serial")
public class TransitSOAPServiceLocator extends org.apache.axis.client.Service implements org.unibl.etf.soap.transit.TransitSOAPService {

    public TransitSOAPServiceLocator() {
    }


    public TransitSOAPServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TransitSOAPServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TransitSOAP
    private java.lang.String TransitSOAP_address = "http://localhost:8080/Prelazak_Granice_Servis/services/TransitSOAP";

    public java.lang.String getTransitSOAPAddress() {
        return TransitSOAP_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TransitSOAPWSDDServiceName = "TransitSOAP";

    public java.lang.String getTransitSOAPWSDDServiceName() {
        return TransitSOAPWSDDServiceName;
    }

    public void setTransitSOAPWSDDServiceName(java.lang.String name) {
        TransitSOAPWSDDServiceName = name;
    }

    public org.unibl.etf.soap.transit.TransitSOAP getTransitSOAP() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TransitSOAP_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTransitSOAP(endpoint);
    }

    public org.unibl.etf.soap.transit.TransitSOAP getTransitSOAP(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.unibl.etf.soap.transit.TransitSOAPSoapBindingStub _stub = new org.unibl.etf.soap.transit.TransitSOAPSoapBindingStub(portAddress, this);
            _stub.setPortName(getTransitSOAPWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTransitSOAPEndpointAddress(java.lang.String address) {
        TransitSOAP_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    @SuppressWarnings("rawtypes")
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.unibl.etf.soap.transit.TransitSOAP.class.isAssignableFrom(serviceEndpointInterface)) {
                org.unibl.etf.soap.transit.TransitSOAPSoapBindingStub _stub = new org.unibl.etf.soap.transit.TransitSOAPSoapBindingStub(new java.net.URL(TransitSOAP_address), this);
                _stub.setPortName(getTransitSOAPWSDDServiceName());
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
        if ("TransitSOAP".equals(inputPortName)) {
            return getTransitSOAP();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://transit.soap.etf.unibl.org", "TransitSOAPService");
    }

    @SuppressWarnings("rawtypes")
	private java.util.HashSet ports = null;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://transit.soap.etf.unibl.org", "TransitSOAP"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TransitSOAP".equals(portName)) {
            setTransitSOAPEndpointAddress(address);
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
