/**
 * KorisnikDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.unibl.etf.model;

@SuppressWarnings("serial")
public class KorisnikDTO  implements java.io.Serializable {
    private java.lang.Boolean aktivan;

    private java.lang.String idProlaz;

    private java.lang.String korisnickoIme;

    private java.lang.String lozinka;

    private java.lang.String tipKontrole;

    public KorisnikDTO() {
    }

    public KorisnikDTO(
           java.lang.Boolean aktivan,
           java.lang.String idProlaz,
           java.lang.String korisnickoIme,
           java.lang.String lozinka,
           java.lang.String tipKontrole) {
           this.aktivan = aktivan;
           this.idProlaz = idProlaz;
           this.korisnickoIme = korisnickoIme;
           this.lozinka = lozinka;
           this.tipKontrole = tipKontrole;
    }


    /**
     * Gets the aktivan value for this KorisnikDTO.
     * 
     * @return aktivan
     */
    public java.lang.Boolean getAktivan() {
        return aktivan;
    }


    /**
     * Sets the aktivan value for this KorisnikDTO.
     * 
     * @param aktivan
     */
    public void setAktivan(java.lang.Boolean aktivan) {
        this.aktivan = aktivan;
    }


    /**
     * Gets the idProlaz value for this KorisnikDTO.
     * 
     * @return idProlaz
     */
    public java.lang.String getIdProlaz() {
        return idProlaz;
    }


    /**
     * Sets the idProlaz value for this KorisnikDTO.
     * 
     * @param idProlaz
     */
    public void setIdProlaz(java.lang.String idProlaz) {
        this.idProlaz = idProlaz;
    }


    /**
     * Gets the korisnickoIme value for this KorisnikDTO.
     * 
     * @return korisnickoIme
     */
    public java.lang.String getKorisnickoIme() {
        return korisnickoIme;
    }


    /**
     * Sets the korisnickoIme value for this KorisnikDTO.
     * 
     * @param korisnickoIme
     */
    public void setKorisnickoIme(java.lang.String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }


    /**
     * Gets the lozinka value for this KorisnikDTO.
     * 
     * @return lozinka
     */
    public java.lang.String getLozinka() {
        return lozinka;
    }


    /**
     * Sets the lozinka value for this KorisnikDTO.
     * 
     * @param lozinka
     */
    public void setLozinka(java.lang.String lozinka) {
        this.lozinka = lozinka;
    }


    /**
     * Gets the tipKontrole value for this KorisnikDTO.
     * 
     * @return tipKontrole
     */
    public java.lang.String getTipKontrole() {
        return tipKontrole;
    }


    /**
     * Sets the tipKontrole value for this KorisnikDTO.
     * 
     * @param tipKontrole
     */
    public void setTipKontrole(java.lang.String tipKontrole) {
        this.tipKontrole = tipKontrole;
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KorisnikDTO)) return false;
        KorisnikDTO other = (KorisnikDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aktivan==null && other.getAktivan()==null) || 
             (this.aktivan!=null &&
              this.aktivan.equals(other.getAktivan()))) &&
            ((this.idProlaz==null && other.getIdProlaz()==null) || 
             (this.idProlaz!=null &&
              this.idProlaz.equals(other.getIdProlaz()))) &&
            ((this.korisnickoIme==null && other.getKorisnickoIme()==null) || 
             (this.korisnickoIme!=null &&
              this.korisnickoIme.equals(other.getKorisnickoIme()))) &&
            ((this.lozinka==null && other.getLozinka()==null) || 
             (this.lozinka!=null &&
              this.lozinka.equals(other.getLozinka()))) &&
            ((this.tipKontrole==null && other.getTipKontrole()==null) || 
             (this.tipKontrole!=null &&
              this.tipKontrole.equals(other.getTipKontrole())));
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
        if (getAktivan() != null) {
            _hashCode += getAktivan().hashCode();
        }
        if (getIdProlaz() != null) {
            _hashCode += getIdProlaz().hashCode();
        }
        if (getKorisnickoIme() != null) {
            _hashCode += getKorisnickoIme().hashCode();
        }
        if (getLozinka() != null) {
            _hashCode += getLozinka().hashCode();
        }
        if (getTipKontrole() != null) {
            _hashCode += getTipKontrole().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(KorisnikDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model.etf.unibl.org", "KorisnikDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aktivan");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "aktivan"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idProlaz");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "idProlaz"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("korisnickoIme");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "korisnickoIme"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lozinka");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "lozinka"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipKontrole");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model.etf.unibl.org", "tipKontrole"));
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
