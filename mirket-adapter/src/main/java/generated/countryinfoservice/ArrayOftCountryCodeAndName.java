
package generated.countryinfoservice;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOftCountryCodeAndName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>{@code
 * <complexType name="ArrayOftCountryCodeAndName">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="tCountryCodeAndName" type="{http://www.oorsprong.org/websamples.countryinfo}tCountryCodeAndName" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOftCountryCodeAndName", propOrder = {
    "tCountryCodeAndName"
})
public class ArrayOftCountryCodeAndName {

    @XmlElement(nillable = true)
    protected List<TCountryCodeAndName> tCountryCodeAndName;

    /**
     * Gets the value of the tCountryCodeAndName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a {@code set} method for the tCountryCodeAndName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTCountryCodeAndName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TCountryCodeAndName }
     * 
     * 
     * @return
     *     The value of the tCountryCodeAndName property.
     */
    public List<TCountryCodeAndName> getTCountryCodeAndName() {
        if (tCountryCodeAndName == null) {
            tCountryCodeAndName = new ArrayList<>();
        }
        return this.tCountryCodeAndName;
    }

}
