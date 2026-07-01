/**
 * TerminalDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.model;

@SuppressWarnings("serial")
public class TerminalDTO  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String naziv;

    private org.unibl.etf.model.ProlazDTO[] prolazi;

    private java.lang.String tipSerijalizacije;

    public TerminalDTO() {
    }

    public TerminalDTO(
           java.lang.String id,
           java.lang.String naziv,
           org.unibl.etf.model.ProlazDTO[] prolazi,
           java.lang.String tipSerijalizacije) {
           this.id = id;
           this.naziv = naziv;
           this.prolazi = prolazi;
           this.tipSerijalizacije = tipSerijalizacije;
    }


    /**
     * Gets the id value for this TerminalDTO.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this TerminalDTO.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the naziv value for this TerminalDTO.
     * 
     * @return naziv
     */
    public java.lang.String getNaziv() {
        return naziv;
    }


    /**
     * Sets the naziv value for this TerminalDTO.
     * 
     * @param naziv
     */
    public void setNaziv(java.lang.String naziv) {
        this.naziv = naziv;
    }


    /**
     * Gets the prolazi value for this TerminalDTO.
     * 
     * @return prolazi
     */
    public org.unibl.etf.model.ProlazDTO[] getProlazi() {
        return prolazi;
    }


    /**
     * Sets the prolazi value for this TerminalDTO.
     * 
     * @param prolazi
     */
    public void setProlazi(org.unibl.etf.model.ProlazDTO[] prolazi) {
        this.prolazi = prolazi;
    }


    /**
     * Gets the tipSerijalizacije value for this TerminalDTO.
     * 
     * @return tipSerijalizacije
     */
    public java.lang.String getTipSerijalizacije() {
        return tipSerijalizacije;
    }


    /**
     * Sets the tipSerijalizacije value for this TerminalDTO.
     * 
     * @param tipSerijalizacije
     */
    public void setTipSerijalizacije(java.lang.String tipSerijalizacije) {
        this.tipSerijalizacije = tipSerijalizacije;
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TerminalDTO)) return false;
        TerminalDTO other = (TerminalDTO) obj;
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
            ((this.naziv==null && other.getNaziv()==null) || 
             (this.naziv!=null &&
              this.naziv.equals(other.getNaziv()))) &&
            ((this.prolazi==null && other.getProlazi()==null) || 
             (this.prolazi!=null &&
              java.util.Arrays.equals(this.prolazi, other.getProlazi()))) &&
            ((this.tipSerijalizacije==null && other.getTipSerijalizacije()==null) || 
             (this.tipSerijalizacije!=null &&
              this.tipSerijalizacije.equals(other.getTipSerijalizacije())));
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
        if (getNaziv() != null) {
            _hashCode += getNaziv().hashCode();
        }
        if (getProlazi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProlazi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProlazi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipSerijalizacije() != null) {
            _hashCode += getTipSerijalizacije().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TerminalDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "TerminalDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("naziv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "naziv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prolazi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "prolazi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "ProlazDTO"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://centralregistry.soap.etf.unibl.org", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipSerijalizacije");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "tipSerijalizacije"));
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
