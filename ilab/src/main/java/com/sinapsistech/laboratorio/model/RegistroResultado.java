package com.sinapsistech.laboratorio.model;

// Generated Mar 2, 2015 7:12:28 PM by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RegistroResultado generated by hbm2java
 */
@Entity
@Table(name = "registro_resultado"
      , schema = "public")
public class RegistroResultado implements java.io.Serializable
{

   private int idRegistroResultado;
   private SolicitudDetalle solicitudDetalle;
   private Trabajador trabajador;
   private String registrosNormales;
   private String registrosAnormales;
   private String resultado;
   private String observaciones;
   private Date fechaRegistroResultado;
   private Date fechaReg;
   private Boolean flagAprobado;
   private String flagEstado;
   private String usuarioReg;
   private Date fechaMod;
   private String usuarioMod;
   private Date fechaBorrado;
   private String usuarioBorrado;

   //adicionado por roy ayala
   private Componente componente;
   private Examen examen;
   
   public RegistroResultado()
   {
   }

   public RegistroResultado(int idRegistroResultado, SolicitudDetalle solicitudDetalle, Trabajador trabajador)
   {
      this.idRegistroResultado = idRegistroResultado;
      this.solicitudDetalle = solicitudDetalle;
      this.trabajador = trabajador;
   }

   public RegistroResultado(int idRegistroResultado, SolicitudDetalle solicitudDetalle, Trabajador trabajador, String registrosNormales, String registrosAnormales, String resultado, String observaciones, Date fechaRegistroResultado, Date fechaReg, Boolean flagAprobado, String flagEstado, String usuarioReg, Date fechaMod, String usuarioMod, Date fechaBorrado, String usuarioBorrado)
   {
      this.idRegistroResultado = idRegistroResultado;
      this.solicitudDetalle = solicitudDetalle;
      this.trabajador = trabajador;
      this.registrosNormales = registrosNormales;
      this.registrosAnormales = registrosAnormales;
      this.resultado = resultado;
      this.observaciones = observaciones;
      this.fechaRegistroResultado = fechaRegistroResultado;
      this.fechaReg = fechaReg;
      this.flagAprobado = flagAprobado;
      this.flagEstado = flagEstado;
      this.usuarioReg = usuarioReg;
      this.fechaMod = fechaMod;
      this.usuarioMod = usuarioMod;
      this.fechaBorrado = fechaBorrado;
      this.usuarioBorrado = usuarioBorrado;
   }

   @Id
   @GeneratedValue(strategy=GenerationType.IDENTITY)
   @Column(name = "id_registro_resultado", unique = true, nullable = false, insertable=true)
   public int getIdRegistroResultado()
   {
      return this.idRegistroResultado;
   }

   public void setIdRegistroResultado(int idRegistroResultado)
   {
      this.idRegistroResultado = idRegistroResultado;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_solicitud_detalle", nullable = false)
   public SolicitudDetalle getSolicitudDetalle()
   {
      return this.solicitudDetalle;
   }

   public void setSolicitudDetalle(SolicitudDetalle solicitudDetalle)
   {
      this.solicitudDetalle = solicitudDetalle;
   }

   //@Todo: adicionado por roy ayala
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_componente")
   public Componente getComponente()
   {
      return this.componente;
   }

   public void setComponente(Componente componente)
   {
      this.componente = componente;
   }

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "id_examen")
   public Examen getExamen()
   {
      return this.examen;
   }

   public void setExamen(Examen examen)
   {
      this.examen = examen;
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

   @Column(name = "registros_normales", length = 200)
   public String getRegistrosNormales()
   {
      return this.registrosNormales;
   }

   public void setRegistrosNormales(String registrosNormales)
   {
      this.registrosNormales = registrosNormales;
   }

   @Column(name = "registros_anormales", length = 200)
   public String getRegistrosAnormales()
   {
      return this.registrosAnormales;
   }

   public void setRegistrosAnormales(String registrosAnormales)
   {
      this.registrosAnormales = registrosAnormales;
   }

   @Column(name = "resultado", length = 200)
   public String getResultado()
   {
      return this.resultado;
   }

   public void setResultado(String resultado)
   {
      this.resultado = resultado;
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

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "fecha_registro_resultado", length = 29)
   public Date getFechaRegistroResultado()
   {
      return this.fechaRegistroResultado;
   }

   public void setFechaRegistroResultado(Date fechaRegistroResultado)
   {
      this.fechaRegistroResultado = fechaRegistroResultado;
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

   @Column(name = "flag_aprobado")
   public Boolean getFlagAprobado()
   {
      return this.flagAprobado;
   }

   public void setFlagAprobado(Boolean flagAprobado)
   {
      this.flagAprobado = flagAprobado;
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

}
