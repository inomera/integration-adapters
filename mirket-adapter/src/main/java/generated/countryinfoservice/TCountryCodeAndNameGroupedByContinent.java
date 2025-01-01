
package generated.countryinfoservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tCountryCodeAndNameGroupedByContinent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="tCountryCodeAndNameGroupedByContinent">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Continent" type="{http://www.oorsprong.org/websamples.countryinfo}tContinent"/>
 *         <element name="CountryCodeAndNames" type="{http://www.oorsprong.org/websamples.countryinfo}ArrayOftCountryCodeAndName"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tCountryCodeAndNameGroupedByContinent", propOrder = {
    "continent",
    "countryCodeAndNames"
})
public class TCountryCodeAndNameGroupedByContinent {

    @XmlElement(name = "Continent", required = true)
    protected TContinent continent;
    @XmlElement(name = "CountryCodeAndNames", required = true)
    protected ArrayOftCountryCodeAndName countryCodeAndNames;

    /**
     * Gets the value of the continent property.
     * 
     * @return
     *     possible object is
     *     {@link TContinent }
     *     
     */
    public TContinent getContinent() {
        return continent;
    }

    /**
     * Sets the value of the continent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TContinent }
     *     
     */
    public void setContinent(TContinent value) {
        this.continent = value;
    }

    /**
     * Gets the value of the countryCodeAndNames property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOftCountryCodeAndName }
     *     
     */
    public ArrayOftCountryCodeAndName getCountryCodeAndNames() {
        return countryCodeAndNames;
    }

    /**
     * Sets the value of the countryCodeAndNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOftCountryCodeAndName }
     *     
     */
    public void setCountryCodeAndNames(ArrayOftCountryCodeAndName value) {
        this.countryCodeAndNames = value;
    }

}
