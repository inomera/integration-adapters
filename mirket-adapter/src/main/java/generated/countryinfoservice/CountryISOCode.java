
package generated.countryinfoservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="sCountryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sCountryName"
})
@XmlRootElement(name = "CountryISOCode")
public class CountryISOCode {

    @XmlElement(required = true)
    protected String sCountryName;

    /**
     * Gets the value of the sCountryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCountryName() {
        return sCountryName;
    }

    /**
     * Sets the value of the sCountryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCountryName(String value) {
        this.sCountryName = value;
    }

}
