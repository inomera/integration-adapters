
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
 *         <element name="CountryNameResult" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "countryNameResult"
})
@XmlRootElement(name = "CountryNameResponse")
public class CountryNameResponse {

    @XmlElement(name = "CountryNameResult", required = true)
    protected String countryNameResult;

    /**
     * Gets the value of the countryNameResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryNameResult() {
        return countryNameResult;
    }

    /**
     * Sets the value of the countryNameResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryNameResult(String value) {
        this.countryNameResult = value;
    }

}
