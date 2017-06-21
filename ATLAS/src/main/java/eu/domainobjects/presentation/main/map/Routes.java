//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.14 at 01:35:12 PM CEST 
//

package eu.domainobjects.presentation.main.map;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="route" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="leg" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="transportType">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="type">
 *                                       &lt;simpleType>
 *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                           &lt;enumeration value="walk"/>
 *                                           &lt;enumeration value="car"/>
 *                                           &lt;enumeration value="carSharing"/>
 *                                           &lt;enumeration value="train"/>
 *                                           &lt;enumeration value="bus"/>
 *                                           &lt;enumeration value="bicycle"/>
 *                                         &lt;/restriction>
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="safety_parameters">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="safety_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                                       &lt;element name="sparseness_level" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *                                       &lt;element name="pollution_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="privacy_parameters">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="data_request_mask" minOccurs="0">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;all>
 *                                                 &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="date_of_birth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                                 &lt;element name="payment_details" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                               &lt;/all>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="geometry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="walkingDistance" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                           &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="noOfChanges" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="utility" type="{http://www.w3.org/2001/XMLSchema}double" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "route" })
@XmlRootElement(name = "routes")
public class Routes {

	protected List<Routes.Route> route;

	/**
	 * Gets the value of the route property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the route property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRoute().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Routes.Route }
	 * 
	 * 
	 */
	public List<Routes.Route> getRoute() {
		if (route == null) {
			route = new ArrayList<Routes.Route>();
		}
		return this.route;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="leg" maxOccurs="unbounded" minOccurs="0">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="transportType">
	 *                     &lt;complexType>
	 *                       &lt;complexContent>
	 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                           &lt;attribute name="type">
	 *                             &lt;simpleType>
	 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
	 *                                 &lt;enumeration value="walk"/>
	 *                                 &lt;enumeration value="car"/>
	 *                                 &lt;enumeration value="carSharing"/>
	 *                                 &lt;enumeration value="train"/>
	 *                                 &lt;enumeration value="bus"/>
	 *                                 &lt;enumeration value="bicycle"/>
	 *                               &lt;/restriction>
	 *                             &lt;/simpleType>
	 *                           &lt;/attribute>
	 *                         &lt;/restriction>
	 *                       &lt;/complexContent>
	 *                     &lt;/complexType>
	 *                   &lt;/element>
	 *                   &lt;element name="safety_parameters">
	 *                     &lt;complexType>
	 *                       &lt;complexContent>
	 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                           &lt;sequence>
	 *                             &lt;element name="safety_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
	 *                             &lt;element name="sparseness_level" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
	 *                             &lt;element name="pollution_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
	 *                           &lt;/sequence>
	 *                         &lt;/restriction>
	 *                       &lt;/complexContent>
	 *                     &lt;/complexType>
	 *                   &lt;/element>
	 *                   &lt;element name="privacy_parameters">
	 *                     &lt;complexType>
	 *                       &lt;complexContent>
	 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                           &lt;sequence>
	 *                             &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                             &lt;element name="data_request_mask" minOccurs="0">
	 *                               &lt;complexType>
	 *                                 &lt;complexContent>
	 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                                     &lt;all>
	 *                                       &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="date_of_birth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                       &lt;element name="payment_details" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
	 *                                     &lt;/all>
	 *                                   &lt;/restriction>
	 *                                 &lt;/complexContent>
	 *                               &lt;/complexType>
	 *                             &lt;/element>
	 *                           &lt;/sequence>
	 *                         &lt;/restriction>
	 *                       &lt;/complexContent>
	 *                     &lt;/complexType>
	 *                   &lt;/element>
	 *                   &lt;element name="geometry" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *                 &lt;/sequence>
	 *                 &lt;attribute name="walkingDistance" type="{http://www.w3.org/2001/XMLSchema}double" />
	 *                 &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}int" />
	 *                 &lt;attribute name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" />
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *       &lt;/sequence>
	 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" />
	 *       &lt;attribute name="noOfChanges" type="{http://www.w3.org/2001/XMLSchema}int" />
	 *       &lt;attribute name="utility" type="{http://www.w3.org/2001/XMLSchema}double" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "leg" })
	public static class Route {

		protected List<Routes.Route.Leg> leg;
		@XmlAttribute(name = "id")
		protected String id;
		@XmlAttribute(name = "color")
		protected String color;
		@XmlAttribute(name = "noOfChanges")
		protected Integer noOfChanges;
		@XmlAttribute(name = "utility")
		protected Double utility;
		@XmlAttribute(name = "flexibusRoute", required = false)
		protected Boolean flexibusRoute;

		/**
		 * Gets the value of the leg property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the leg property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getLeg().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Routes.Route.Leg }
		 * 
		 * 
		 */
		public List<Routes.Route.Leg> getLeg() {
			if (leg == null) {
				leg = new ArrayList<Routes.Route.Leg>();
			}
			return this.leg;
		}

		/**
		 * Gets the value of the id property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getId() {
			return id;
		}

		/**
		 * Sets the value of the id property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setId(String value) {
			this.id = value;
		}

		/**
		 * Gets the value of the color property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getColor() {
			return color;
		}

		/**
		 * Sets the value of the color property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setColor(String value) {
			this.color = value;
		}

		/**
		 * Gets the value of the noOfChanges property.
		 * 
		 * @return possible object is {@link Integer }
		 * 
		 */
		public Integer getNoOfChanges() {
			return noOfChanges;
		}

		/**
		 * Sets the value of the noOfChanges property.
		 * 
		 * @param value
		 *            allowed object is {@link Integer }
		 * 
		 */
		public void setNoOfChanges(Integer value) {
			this.noOfChanges = value;
		}

		/**
		 * Gets the value of the utility property.
		 * 
		 * @return possible object is {@link Double }
		 * 
		 */
		public Double getUtility() {
			return utility;
		}

		/**
		 * Sets the value of the utility property.
		 * 
		 * @param value
		 *            allowed object is {@link Double }
		 * 
		 */
		public void setUtility(Double value) {
			this.utility = value;
		}

		/**
		 * <p>
		 * Java class for anonymous complex type.
		 * 
		 * <p>
		 * The following schema fragment specifies the expected content
		 * contained within this class.
		 * 
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="transportType">
		 *           &lt;complexType>
		 *             &lt;complexContent>
		 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *                 &lt;attribute name="type">
		 *                   &lt;simpleType>
		 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
		 *                       &lt;enumeration value="walk"/>
		 *                       &lt;enumeration value="car"/>
		 *                       &lt;enumeration value="carSharing"/>
		 *                       &lt;enumeration value="train"/>
		 *                       &lt;enumeration value="bus"/>
		 *                       &lt;enumeration value="bicycle"/>
		 *                     &lt;/restriction>
		 *                   &lt;/simpleType>
		 *                 &lt;/attribute>
		 *               &lt;/restriction>
		 *             &lt;/complexContent>
		 *           &lt;/complexType>
		 *         &lt;/element>
		 *         &lt;element name="safety_parameters">
		 *           &lt;complexType>
		 *             &lt;complexContent>
		 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *                 &lt;sequence>
		 *                   &lt;element name="safety_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
		 *                   &lt;element name="sparseness_level" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
		 *                   &lt;element name="pollution_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
		 *                 &lt;/sequence>
		 *               &lt;/restriction>
		 *             &lt;/complexContent>
		 *           &lt;/complexType>
		 *         &lt;/element>
		 *         &lt;element name="privacy_parameters">
		 *           &lt;complexType>
		 *             &lt;complexContent>
		 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *                 &lt;sequence>
		 *                   &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *                   &lt;element name="data_request_mask" minOccurs="0">
		 *                     &lt;complexType>
		 *                       &lt;complexContent>
		 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *                           &lt;all>
		 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="date_of_birth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                             &lt;element name="payment_details" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
		 *                           &lt;/all>
		 *                         &lt;/restriction>
		 *                       &lt;/complexContent>
		 *                     &lt;/complexType>
		 *                   &lt;/element>
		 *                 &lt;/sequence>
		 *               &lt;/restriction>
		 *             &lt;/complexContent>
		 *           &lt;/complexType>
		 *         &lt;/element>
		 *         &lt;element name="geometry" type="{http://www.w3.org/2001/XMLSchema}string"/>
		 *       &lt;/sequence>
		 *       &lt;attribute name="walkingDistance" type="{http://www.w3.org/2001/XMLSchema}double" />
		 *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}int" />
		 *       &lt;attribute name="Cost" type="{http://www.w3.org/2001/XMLSchema}double" />
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 * 
		 * 
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "transportType", "safetyParameters",
				"privacyParameters", "geometry" })
		public static class Leg {

			@XmlElement(required = true)
			protected Routes.Route.Leg.TransportType transportType;
			@XmlElement(name = "safety_parameters", required = true)
			protected Routes.Route.Leg.SafetyParameters safetyParameters;
			@XmlElement(name = "privacy_parameters", required = true)
			protected Routes.Route.Leg.PrivacyParameters privacyParameters;
			@XmlElement(required = true)
			protected String geometry;
			@XmlAttribute(name = "walkingDistance")
			protected Double walkingDistance;
			@XmlAttribute(name = "duration")
			protected Integer duration;
			@XmlAttribute(name = "Cost")
			protected Double cost;
			@XmlAttribute(name = "id")
			protected Integer id;

			/**
			 * Gets the value of the transportType property.
			 * 
			 * @return possible object is {@link Routes.Route.Leg.TransportType }
			 * 
			 */
			public Routes.Route.Leg.TransportType getTransportType() {
				return transportType;
			}

			/**
			 * Sets the value of the transportType property.
			 * 
			 * @param value
			 *            allowed object is
			 *            {@link Routes.Route.Leg.TransportType }
			 * 
			 */
			public void setTransportType(Routes.Route.Leg.TransportType value) {
				this.transportType = value;
			}

			/**
			 * Gets the value of the safetyParameters property.
			 * 
			 * @return possible object is
			 *         {@link Routes.Route.Leg.SafetyParameters }
			 * 
			 */
			public Routes.Route.Leg.SafetyParameters getSafetyParameters() {
				return safetyParameters;
			}

			/**
			 * Sets the value of the safetyParameters property.
			 * 
			 * @param value
			 *            allowed object is
			 *            {@link Routes.Route.Leg.SafetyParameters }
			 * 
			 */
			public void setSafetyParameters(
					Routes.Route.Leg.SafetyParameters value) {
				this.safetyParameters = value;
			}

			/**
			 * Gets the value of the privacyParameters property.
			 * 
			 * @return possible object is
			 *         {@link Routes.Route.Leg.PrivacyParameters }
			 * 
			 */
			public Routes.Route.Leg.PrivacyParameters getPrivacyParameters() {
				return privacyParameters;
			}

			/**
			 * Sets the value of the privacyParameters property.
			 * 
			 * @param value
			 *            allowed object is
			 *            {@link Routes.Route.Leg.PrivacyParameters }
			 * 
			 */
			public void setPrivacyParameters(
					Routes.Route.Leg.PrivacyParameters value) {
				this.privacyParameters = value;
			}

			/**
			 * Gets the value of the geometry property.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getGeometry() {
				return geometry;
			}

			/**
			 * Sets the value of the geometry property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setGeometry(String value) {
				this.geometry = value;
			}

			/**
			 * Gets the value of the walkingDistance property.
			 * 
			 * @return possible object is {@link Double }
			 * 
			 */
			public Double getWalkingDistance() {
				return walkingDistance;
			}

			/**
			 * Sets the value of the walkingDistance property.
			 * 
			 * @param value
			 *            allowed object is {@link Double }
			 * 
			 */
			public void setWalkingDistance(Double value) {
				this.walkingDistance = value;
			}

			/**
			 * Gets the value of the duration property.
			 * 
			 * @return possible object is {@link Integer }
			 * 
			 */
			public Integer getDuration() {
				return duration;
			}

			/**
			 * Sets the value of the duration property.
			 * 
			 * @param value
			 *            allowed object is {@link Integer }
			 * 
			 */
			public void setDuration(Integer value) {
				this.duration = value;
			}

			/**
			 * Gets the value of the cost property.
			 * 
			 * @return possible object is {@link Double }
			 * 
			 */
			public Double getCost() {
				return cost;
			}

			/**
			 * Sets the value of the cost property.
			 * 
			 * @param value
			 *            allowed object is {@link Double }
			 * 
			 */
			public void setCost(Double value) {
				this.cost = value;
			}

			/**
			 * <p>
			 * Java class for anonymous complex type.
			 * 
			 * <p>
			 * The following schema fragment specifies the expected content
			 * contained within this class.
			 * 
			 * <pre>
			 * &lt;complexType>
			 *   &lt;complexContent>
			 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
			 *       &lt;sequence>
			 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
			 *         &lt;element name="data_request_mask" minOccurs="0">
			 *           &lt;complexType>
			 *             &lt;complexContent>
			 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
			 *                 &lt;all>
			 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="date_of_birth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                   &lt;element name="payment_details" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
			 *                 &lt;/all>
			 *               &lt;/restriction>
			 *             &lt;/complexContent>
			 *           &lt;/complexType>
			 *         &lt;/element>
			 *       &lt;/sequence>
			 *     &lt;/restriction>
			 *   &lt;/complexContent>
			 * &lt;/complexType>
			 * </pre>
			 * 
			 * 
			 */
			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "provider", "dataRequestMask" })
			public static class PrivacyParameters {

				@XmlElement(required = true)
				protected String provider;
				@XmlElement(name = "data_request_mask")
				protected Routes.Route.Leg.PrivacyParameters.DataRequestMask dataRequestMask;

				/**
				 * Gets the value of the provider property.
				 * 
				 * @return possible object is {@link String }
				 * 
				 */
				public String getProvider() {
					return provider;
				}

				/**
				 * Sets the value of the provider property.
				 * 
				 * @param value
				 *            allowed object is {@link String }
				 * 
				 */
				public void setProvider(String value) {
					this.provider = value;
				}

				/**
				 * Gets the value of the dataRequestMask property.
				 * 
				 * @return possible object is
				 *         {@link Routes.Route.Leg.PrivacyParameters.DataRequestMask }
				 * 
				 */
				public Routes.Route.Leg.PrivacyParameters.DataRequestMask getDataRequestMask() {
					return dataRequestMask;
				}

				/**
				 * Sets the value of the dataRequestMask property.
				 * 
				 * @param value
				 *            allowed object is
				 *            {@link Routes.Route.Leg.PrivacyParameters.DataRequestMask }
				 * 
				 */
				public void setDataRequestMask(
						Routes.Route.Leg.PrivacyParameters.DataRequestMask value) {
					this.dataRequestMask = value;
				}

				/**
				 * <p>
				 * Java class for anonymous complex type.
				 * 
				 * <p>
				 * The following schema fragment specifies the expected content
				 * contained within this class.
				 * 
				 * <pre>
				 * &lt;complexType>
				 *   &lt;complexContent>
				 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
				 *       &lt;all>
				 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="date_of_birth" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="gps" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *         &lt;element name="payment_details" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
				 *       &lt;/all>
				 *     &lt;/restriction>
				 *   &lt;/complexContent>
				 * &lt;/complexType>
				 * </pre>
				 * 
				 * 
				 */
				@XmlAccessorType(XmlAccessType.FIELD)
				@XmlType(name = "", propOrder = {

				})
				public static class DataRequestMask {

					protected boolean name;
					@XmlElement(name = "date_of_birth")
					protected boolean dateOfBirth;
					protected boolean address;
					protected boolean email;
					protected boolean phone;
					protected boolean gps;
					@XmlElement(name = "payment_details")
					protected boolean paymentDetails;

					/**
					 * Gets the value of the name property.
					 * 
					 */
					public boolean isName() {
						return name;
					}

					/**
					 * Sets the value of the name property.
					 * 
					 */
					public void setName(boolean value) {
						this.name = value;
					}

					/**
					 * Gets the value of the dateOfBirth property.
					 * 
					 */
					public boolean isDateOfBirth() {
						return dateOfBirth;
					}

					/**
					 * Sets the value of the dateOfBirth property.
					 * 
					 */
					public void setDateOfBirth(boolean value) {
						this.dateOfBirth = value;
					}

					/**
					 * Gets the value of the address property.
					 * 
					 */
					public boolean isAddress() {
						return address;
					}

					/**
					 * Sets the value of the address property.
					 * 
					 */
					public void setAddress(boolean value) {
						this.address = value;
					}

					/**
					 * Gets the value of the email property.
					 * 
					 */
					public boolean isEmail() {
						return email;
					}

					/**
					 * Sets the value of the email property.
					 * 
					 */
					public void setEmail(boolean value) {
						this.email = value;
					}

					/**
					 * Gets the value of the phone property.
					 * 
					 */
					public boolean isPhone() {
						return phone;
					}

					/**
					 * Sets the value of the phone property.
					 * 
					 */
					public void setPhone(boolean value) {
						this.phone = value;
					}

					/**
					 * Gets the value of the gps property.
					 * 
					 */
					public boolean isGps() {
						return gps;
					}

					/**
					 * Sets the value of the gps property.
					 * 
					 */
					public void setGps(boolean value) {
						this.gps = value;
					}

					/**
					 * Gets the value of the paymentDetails property.
					 * 
					 */
					public boolean isPaymentDetails() {
						return paymentDetails;
					}

					/**
					 * Sets the value of the paymentDetails property.
					 * 
					 */
					public void setPaymentDetails(boolean value) {
						this.paymentDetails = value;
					}

				}

			}

			/**
			 * <p>
			 * Java class for anonymous complex type.
			 * 
			 * <p>
			 * The following schema fragment specifies the expected content
			 * contained within this class.
			 * 
			 * <pre>
			 * &lt;complexType>
			 *   &lt;complexContent>
			 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
			 *       &lt;sequence>
			 *         &lt;element name="safety_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
			 *         &lt;element name="sparseness_level" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
			 *         &lt;element name="pollution_level" type="{http://www.w3.org/2001/XMLSchema}integer"/>
			 *       &lt;/sequence>
			 *     &lt;/restriction>
			 *   &lt;/complexContent>
			 * &lt;/complexType>
			 * </pre>
			 * 
			 * 
			 */
			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "", propOrder = { "safetyLevel", "sparsenessLevel",
					"pollutionLevel" })
			public static class SafetyParameters {

				@XmlElement(name = "safety_level", required = true)
				protected BigInteger safetyLevel;
				@XmlElement(name = "sparseness_level", required = true)
				protected BigDecimal sparsenessLevel;
				@XmlElement(name = "pollution_level", required = true)
				protected BigInteger pollutionLevel;

				/**
				 * Gets the value of the safetyLevel property.
				 * 
				 * @return possible object is {@link BigInteger }
				 * 
				 */
				public BigInteger getSafetyLevel() {
					return safetyLevel;
				}

				/**
				 * Sets the value of the safetyLevel property.
				 * 
				 * @param value
				 *            allowed object is {@link BigInteger }
				 * 
				 */
				public void setSafetyLevel(BigInteger value) {
					this.safetyLevel = value;
				}

				/**
				 * Gets the value of the sparsenessLevel property.
				 * 
				 * @return possible object is {@link BigDecimal }
				 * 
				 */
				public BigDecimal getSparsenessLevel() {
					return sparsenessLevel;
				}

				/**
				 * Sets the value of the sparsenessLevel property.
				 * 
				 * @param value
				 *            allowed object is {@link BigDecimal }
				 * 
				 */
				public void setSparsenessLevel(BigDecimal value) {
					this.sparsenessLevel = value;
				}

				/**
				 * Gets the value of the pollutionLevel property.
				 * 
				 * @return possible object is {@link BigInteger }
				 * 
				 */
				public BigInteger getPollutionLevel() {
					return pollutionLevel;
				}

				/**
				 * Sets the value of the pollutionLevel property.
				 * 
				 * @param value
				 *            allowed object is {@link BigInteger }
				 * 
				 */
				public void setPollutionLevel(BigInteger value) {
					this.pollutionLevel = value;
				}

			}

			/**
			 * <p>
			 * Java class for anonymous complex type.
			 * 
			 * <p>
			 * The following schema fragment specifies the expected content
			 * contained within this class.
			 * 
			 * <pre>
			 * &lt;complexType>
			 *   &lt;complexContent>
			 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
			 *       &lt;attribute name="type">
			 *         &lt;simpleType>
			 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
			 *             &lt;enumeration value="walk"/>
			 *             &lt;enumeration value="car"/>
			 *             &lt;enumeration value="carSharing"/>
			 *             &lt;enumeration value="train"/>
			 *             &lt;enumeration value="bus"/>
			 *             &lt;enumeration value="bicycle"/>
			 *           &lt;/restriction>
			 *         &lt;/simpleType>
			 *       &lt;/attribute>
			 *     &lt;/restriction>
			 *   &lt;/complexContent>
			 * &lt;/complexType>
			 * </pre>
			 * 
			 * 
			 */
			@XmlAccessorType(XmlAccessType.FIELD)
			@XmlType(name = "")
			public static class TransportType {

				@XmlAttribute(name = "type")
				protected String type;

				/**
				 * Gets the value of the type property.
				 * 
				 * @return possible object is {@link String }
				 * 
				 */
				public String getType() {
					return type;
				}

				/**
				 * Sets the value of the type property.
				 * 
				 * @param value
				 *            allowed object is {@link String }
				 * 
				 */
				public void setType(String value) {
					this.type = value;
				}

			}

			public Integer getId() {
				return id;
			}

			public void setId(Integer id) {
				this.id = id;
			}

		}

		public Boolean isFlexibusRoute() {
			return flexibusRoute;
		}

		public void setFlexibusRoute(Boolean flexibusRoute) {
			this.flexibusRoute = flexibusRoute;
		}

	}

}
