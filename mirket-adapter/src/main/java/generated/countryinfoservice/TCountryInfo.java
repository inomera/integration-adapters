
package generated.countryinfoservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tCountryInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="tCountryInfo">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="sISOCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sCapitalCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sPhoneCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sContinentCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sCurrencyISOCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="sCountryFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Languages" type="{http://www.oorsprong.org/websamples.countryinfo}ArrayOftLanguage"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCountryInfo", propOrder = {
    "sisoCode",
    "sName",
    "sCapitalCity",
    "sPhoneCode",
    "sContinentCode",
    "sCurrencyISOCode",
    "sCountryFlag",
    "languages"
})
public class TCountryInfo {

    @XmlElement(name = "sISOCode", required = true)
    protected String sisoCode;
    @XmlElement(required = true)
    protected String sName;
    @XmlElement(required = true)
    protected String sCapitalCity;
    @XmlElement(required = true)
    protected String sPhoneCode;
    @XmlElement(required = true)
    protected String sContinentCode;
    @XmlElement(required = true)
    protected String sCurrencyISOCode;
    @XmlElement(required = true)
    protected String sCountryFlag;
    @XmlElement(name = "Languages", required = true)
    protected ArrayOftLanguage languages;

    /**
     * Gets the value of the sisoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSISOCode() {
        return sisoCode;
    }

    /**
     * Sets the value of the sisoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSISOCode(String value) {
        this.sisoCode = value;
    }

    /**
     * Gets the value of the sName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSName() {
        return sName;
    }

    /**
     * Sets the value of the sName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSName(String value) {
        this.sName = value;
    }

    /**
     * Gets the value of the sCapitalCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCapitalCity() {
        return sCapitalCity;
    }

    /**
     * Sets the value of the sCapitalCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCapitalCity(String value) {
        this.sCapitalCity = value;
    }

    /**
     * Gets the value of the sPhoneCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPhoneCode() {
        return sPhoneCode;
    }

    /**
     * Sets the value of the sPhoneCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPhoneCode(String value) {
        this.sPhoneCode = value;
    }

    /**
     * Gets the value of the sContinentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSContinentCode() {
        return sContinentCode;
    }

    /**
     * Sets the value of the sContinentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSContinentCode(String value) {
        this.sContinentCode = value;
    }

    /**
     * Gets the value of the sCurrencyISOCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCurrencyISOCode() {
        return sCurrencyISOCode;
    }

    /**
     * Sets the value of the sCurrencyISOCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCurrencyISOCode(String value) {
        this.sCurrencyISOCode = value;
    }

    /**
     * Gets the value of the sCountryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCountryFlag() {
        return sCountryFlag;
    }

    /**
     * Sets the value of the sCountryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCountryFlag(String value) {
        this.sCountryFlag = value;
    }

    /**
     * Gets the value of the languages property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOftLanguage }
     *     
     */
    public ArrayOftLanguage getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOftLanguage }
     *     
     */
    public void setLanguages(ArrayOftLanguage value) {
        this.languages = value;
    }

}
