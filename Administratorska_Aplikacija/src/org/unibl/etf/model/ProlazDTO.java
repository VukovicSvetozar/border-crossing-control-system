/**
 * ProlazDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.model;

@SuppressWarnings("serial")
public class ProlazDTO  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String idTerminal;

    private org.unibl.etf.model.KorisnikDTO[] korisnici;

    private java.lang.String tipProlaza;

    public ProlazDTO() {
    }

    public ProlazDTO(
           java.lang.String id,
           java.lang.String idTerminal,
           org.unibl.etf.model.KorisnikDTO[] korisnici,
           java.lang.String tipProlaza) {
           this.id = id;
           this.idTerminal = idTerminal;
           this.korisnici = korisnici;
           this.tipProlaza = tipProlaza;
    }


    /**
     * Gets the id value for this ProlazDTO.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ProlazDTO.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the idTerminal value for this ProlazDTO.
     * 
     * @return idTerminal
     */
    public java.lang.String getIdTerminal() {
        return idTerminal;
    }


    /**
     * Sets the idTerminal value for this ProlazDTO.
     * 
     * @param idTerminal
     */
    public void setIdTerminal(java.lang.String idTerminal) {
        this.idTerminal = idTerminal;
    }


    /**
     * Gets the korisnici value for this ProlazDTO.
     * 
     * @return korisnici
     */
    public org.unibl.etf.model.KorisnikDTO[] getKorisnici() {
        return korisnici;
    }


    /**
     * Sets the korisnici value for this ProlazDTO.
     * 
     * @param korisnici
     */
    public void setKorisnici(org.unibl.etf.model.KorisnikDTO[] korisnici) {
        this.korisnici = korisnici;
    }


    /**
     * Gets the tipProlaza value for this ProlazDTO.
     * 
     * @return tipProlaza
     */
    public java.lang.String getTipProlaza() {
        return tipProlaza;
    }


    /**
     * Sets the tipProlaza value for this ProlazDTO.
     * 
     * @param tipProlaza
     */
    public void setTipProlaza(java.lang.String tipProlaza) {
        this.tipProlaza = tipProlaza;
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProlazDTO)) return false;
        ProlazDTO other = (ProlazDTO) obj;
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
            ((this.idTerminal==null && other.getIdTerminal()==null) || 
             (this.idTerminal!=null &&
              this.idTerminal.equals(other.getIdTerminal()))) &&
            ((this.korisnici==null && other.getKorisnici()==null) || 
             (this.korisnici!=null &&
              java.util.Arrays.equals(this.korisnici, other.getKorisnici()))) &&
            ((this.tipProlaza==null && other.getTipProlaza()==null) || 
             (this.tipProlaza!=null &&
              this.tipProlaza.equals(other.getTipProlaza())));
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
        if (getIdTerminal() != null) {
            _hashCode += getIdTerminal().hashCode();
        }
        if (getKorisnici() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getKorisnici());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKorisnici(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipProlaza() != null) {
            _hashCode += getTipProlaza().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProlazDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "ProlazDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTerminal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "idTerminal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("korisnici");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "korisnici"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "KorisnikDTO"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://centralregistry.soap.etf.unibl.org", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipProlaza");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "tipProlaza"));
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
