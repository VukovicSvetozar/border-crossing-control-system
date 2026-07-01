/**
 * ProlazakDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.model;

@SuppressWarnings("serial")
public class ProlazakDTO  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String idOsoba;

    private java.lang.String informacije;

    private java.lang.String[] odabraniFajlovi;

    private java.lang.String status;

    public ProlazakDTO() {
    }

    public ProlazakDTO(
           java.lang.String id,
           java.lang.String idOsoba,
           java.lang.String informacije,
           java.lang.String[] odabraniFajlovi,
           java.lang.String status) {
           this.id = id;
           this.idOsoba = idOsoba;
           this.informacije = informacije;
           this.odabraniFajlovi = odabraniFajlovi;
           this.status = status;
    }


    /**
     * Gets the id value for this ProlazakDTO.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ProlazakDTO.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the idOsoba value for this ProlazakDTO.
     * 
     * @return idOsoba
     */
    public java.lang.String getIdOsoba() {
        return idOsoba;
    }


    /**
     * Sets the idOsoba value for this ProlazakDTO.
     * 
     * @param idOsoba
     */
    public void setIdOsoba(java.lang.String idOsoba) {
        this.idOsoba = idOsoba;
    }


    /**
     * Gets the informacije value for this ProlazakDTO.
     * 
     * @return informacije
     */
    public java.lang.String getInformacije() {
        return informacije;
    }


    /**
     * Sets the informacije value for this ProlazakDTO.
     * 
     * @param informacije
     */
    public void setInformacije(java.lang.String informacije) {
        this.informacije = informacije;
    }


    /**
     * Gets the odabraniFajlovi value for this ProlazakDTO.
     * 
     * @return odabraniFajlovi
     */
    public java.lang.String[] getOdabraniFajlovi() {
        return odabraniFajlovi;
    }


    /**
     * Sets the odabraniFajlovi value for this ProlazakDTO.
     * 
     * @param odabraniFajlovi
     */
    public void setOdabraniFajlovi(java.lang.String[] odabraniFajlovi) {
        this.odabraniFajlovi = odabraniFajlovi;
    }


    /**
     * Gets the status value for this ProlazakDTO.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ProlazakDTO.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProlazakDTO)) return false;
        ProlazakDTO other = (ProlazakDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idOsoba==null && other.getIdOsoba()==null) || 
             (this.idOsoba!=null &&
              this.idOsoba.equals(other.getIdOsoba()))) &&
            ((this.informacije==null && other.getInformacije()==null) || 
             (this.informacije!=null &&
              this.informacije.equals(other.getInformacije()))) &&
            ((this.odabraniFajlovi==null && other.getOdabraniFajlovi()==null) || 
             (this.odabraniFajlovi!=null &&
              java.util.Arrays.equals(this.odabraniFajlovi, other.getOdabraniFajlovi()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdOsoba() != null) {
            _hashCode += getIdOsoba().hashCode();
        }
        if (getInformacije() != null) {
            _hashCode += getInformacije().hashCode();
        }
        if (getOdabraniFajlovi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOdabraniFajlovi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOdabraniFajlovi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProlazakDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "ProlazakDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idOsoba");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "idOsoba"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("informacije");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "informacije"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("odabraniFajlovi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "odabraniFajlovi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://centralregistry.soap.etf.unibl.org", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    @SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    @SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
