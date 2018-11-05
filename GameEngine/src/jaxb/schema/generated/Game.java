
package jaxb.schema.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Board"/>
 *         &lt;element ref="{}Variant"/>
 *         &lt;element ref="{}InitialPositions"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "board",
    "variant",
    "initialPositions"
})
@XmlRootElement(name = "Game")
public class Game {

    @XmlElement(name = "Board", required = true)
    protected Board board;
    @XmlElement(name = "Variant", required = true)
    protected String variant;
    @XmlElement(name = "InitialPositions", required = true)
    protected InitialPositions initialPositions;

    /**
     * Gets the value of the board property.
     * 
     * @return
     *     possible object is
     *     {@link Board }
     *     
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the value of the board property.
     * 
     * @param value
     *     allowed object is
     *     {@link Board }
     *     
     */
    public void setBoard(Board value) {
        this.board = value;
    }

    /**
     * Gets the value of the variant property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariant() {
        return variant;
    }

    /**
     * Sets the value of the variant property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariant(String value) {
        this.variant = value;
    }

    /**
     * Gets the value of the initialPositions property.
     * 
     * @return
     *     possible object is
     *     {@link InitialPositions }
     *     
     */
    public InitialPositions getInitialPositions() {
        return initialPositions;
    }

    /**
     * Sets the value of the initialPositions property.
     * 
     * @param value
     *     allowed object is
     *     {@link InitialPositions }
     *     
     */
    public void setInitialPositions(InitialPositions value) {
        this.initialPositions = value;
    }

}
