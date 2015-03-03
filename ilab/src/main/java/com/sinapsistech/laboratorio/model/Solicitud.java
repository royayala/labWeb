package com.sinapsistech.laboratorio.model;

// Generated Mar 2, 2015 7:12:28 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Solicitud generated by hbm2java
 */
@Entity
@Table(name = "solicitud"
      , schema = "public")
public class Solicitud implements java.io.Serializable
{

   private int idSolicitud;
   private FormaPago formaPago;
   private Persona persona;
   private TipoTarifa tipoTarifa;
   private Trabajador trabajador;
   private Date fechaSolicitud;
   private Date fechaReg;
   private String observaciones;
   private String flagEstado;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;
   private Set<SolicitudDetalle> solicitudDetalles = new HashSet<SolicitudDetalle>(0);

   public Solicitud()
   {
   }

   public Solicitud(int idSolicitud, FormaPago formaPago, Persona persona, TipoTarifa tipoTarifa, Trabajador trabajador)
   {
      this.idSolicitud = idSolicitud;
      this.formaPago = formaPago;
      this.persona = persona;
      this.tipoTarifa = tipoTarifa;
      this.trabajador = trabajador;
   }

   public Solicitud(int idSolicitud, FormaPago formaPago, Persona persona, TipoTarifa tipoTarifa, Trabajador trabajador, Date fechaSolicitud, Date fechaReg, String observaciones, String flagEstado, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado, Set<SolicitudDetalle> solicitudDetalles)
   {
      this.idSolicitud = idSolicitud;
      this.formaPago = formaPago;
      this.persona = persona;
      this.tipoTarifa = tipoTarifa;
      this.trabajador = trabajador;
      this.fechaSolicitud = fechaSolicitud;
      this.fechaReg = fechaReg;
      this.observaciones = observaciones;
      this.flagEstado = flagEstado;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
      this.solicitudDetalles = solicitudDetalles;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_solicitud", unique = true, nullable = false, insertable=false)
   public int getIdSolicitud()
   {
      return this.idSolicitud;
   }

   public void setIdSolicitud(int idSolicitud)
   {
      this.idSolicitud = idSolicitud;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_forma_pago", nullable = false)
   public FormaPago getFormaPago()
   {
      return this.formaPago;
   }

   public void setFormaPago(FormaPago formaPago)
   {
      this.formaPago = formaPago;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_persona", nullable = false)
   public Persona getPersona()
   {
      return this.persona;
   }

   public void setPersona(Persona persona)
   {
      this.persona = persona;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_tipo_tarifa", nullable = false)
   public TipoTarifa getTipoTarifa()
   {
      return this.tipoTarifa;
   }

   public void setTipoTarifa(TipoTarifa tipoTarifa)
   {
      this.tipoTarifa = tipoTarifa;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_trabajador", nullable = false)
   public Trabajador getTrabajador()
   {
      return this.trabajador;
   }

   public void setTrabajador(Trabajador trabajador)
   {
      this.trabajador = trabajador;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_solicitud", length = 29)
   public Date getFechaSolicitud()
   {
      return this.fechaSolicitud;
   }

   public void setFechaSolicitud(Date fechaSolicitud)
   {
      this.fechaSolicitud = fechaSolicitud;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_reg", length = 29)
   public Date getFechaReg()
   {
      return this.fechaReg;
   }

   public void setFechaReg(Date fechaReg)
   {
      this.fechaReg = fechaReg;
   }

   @Column(name = "observaciones", length = 200)
   public String getObservaciones()
   {
      return this.observaciones;
   }

   public void setObservaciones(String observaciones)
   {
      this.observaciones = observaciones;
   }

   @Column(name = "flag_estado", length = 2)
   public String getFlagEstado()
   {
      return this.flagEstado;
   }

   public void setFlagEstado(String flagEstado)
   {
      this.flagEstado = flagEstado;
   }

   @Column(name = "usuario_reg", length = 30)
   public String getUsuarioReg()
   {
      return this.usuarioReg;
   }

   public void setUsuarioReg(String usuarioReg)
   {
      this.usuarioReg = usuarioReg;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_mod", length = 29)
   public Date getFechaMod()
   {
      return this.fechaMod;
   }

   public void setFechaMod(Date fechaMod)
   {
      this.fechaMod = fechaMod;
   }

   @Column(name = "usuario_mod", length = 30)
   public String getUsuarioMod()
   {
      return this.usuarioMod;
   }

   public void setUsuarioMod(String usuarioMod)
   {
      this.usuarioMod = usuarioMod;
   }

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_borrado", length = 29)
   public Date getFechaBorrado()
   {
      return this.fechaBorrado;
   }

   public void setFechaBorrado(Date fechaBorrado)
   {
      this.fechaBorrado = fechaBorrado;
   }

   @Column(name = "usuario_borrado", length = 30)
   public String getUsuarioBorrado()
   {
      return this.usuarioBorrado;
   }

   public void setUsuarioBorrado(String usuarioBorrado)
   {
      this.usuarioBorrado = usuarioBorrado;
   }

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "solicitud")
   public Set<SolicitudDetalle> getSolicitudDetalles()
   {
      return this.solicitudDetalles;
   }

   public void setSolicitudDetalles(Set<SolicitudDetalle> solicitudDetalles)
   {
      this.solicitudDetalles = solicitudDetalles;
   }

}
