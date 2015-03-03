package com.sinapsistech.laboratorio.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.sinapsistech.laboratorio.model.RegistroResultado;
import com.sinapsistech.laboratorio.model.SolicitudDetalle;
import com.sinapsistech.laboratorio.model.Trabajador;

/**
 * Backing bean for RegistroResultado entities.
 * <p/>
 * This class provides CRUD functionality for all RegistroResultado entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RegistroResultadoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving RegistroResultado entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private RegistroResultado registroResultado;

   public RegistroResultado getRegistroResultado()
   {
      return this.registroResultado;
   }

   public void setRegistroResultado(RegistroResultado registroResultado)
   {
      this.registroResultado = registroResultado;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "ilab-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.registroResultado = this.example;
      }
      else
      {
         this.registroResultado = findById(getId());
      }
   }

   public RegistroResultado findById(Integer id)
   {

      return this.entityManager.find(RegistroResultado.class, id);
   }

   /*
    * Support updating and deleting RegistroResultado entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.registroResultado);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.registroResultado);
            return "view?faces-redirect=true&id=" + this.registroResultado.getIdRegistroResultado();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         RegistroResultado deletableEntity = findById(getId());
         SolicitudDetalle solicitudDetalle = deletableEntity.getSolicitudDetalle();
         solicitudDetalle.getRegistroResultados().remove(deletableEntity);
         deletableEntity.setSolicitudDetalle(null);
         this.entityManager.merge(solicitudDetalle);
         Trabajador trabajador = deletableEntity.getTrabajador();
         trabajador.getRegistroResultados().remove(deletableEntity);
         deletableEntity.setTrabajador(null);
         this.entityManager.merge(trabajador);
         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching RegistroResultado entities with pagination
    */

   private int page;
   private long count;
   private List<RegistroResultado> pageItems;

   private RegistroResultado example = new RegistroResultado();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public RegistroResultado getExample()
   {
      return this.example;
   }

   public void setExample(RegistroResultado example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<RegistroResultado> root = countCriteria.from(RegistroResultado.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<RegistroResultado> criteria = builder.createQuery(RegistroResultado.class);
      root = criteria.from(RegistroResultado.class);
      TypedQuery<RegistroResultado> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<RegistroResultado> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      int idRegistroResultado = this.example.getIdRegistroResultado();
      if (idRegistroResultado != 0)
      {
         predicatesList.add(builder.equal(root.get("idRegistroResultado"), idRegistroResultado));
      }
      SolicitudDetalle solicitudDetalle = this.example.getSolicitudDetalle();
      if (solicitudDetalle != null)
      {
         predicatesList.add(builder.equal(root.get("solicitudDetalle"), solicitudDetalle));
      }
      Trabajador trabajador = this.example.getTrabajador();
      if (trabajador != null)
      {
         predicatesList.add(builder.equal(root.get("trabajador"), trabajador));
      }
      String registrosNormales = this.example.getRegistrosNormales();
      if (registrosNormales != null && !"".equals(registrosNormales))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("registrosNormales")), '%' + registrosNormales.toLowerCase() + '%'));
      }
      String registrosAnormales = this.example.getRegistrosAnormales();
      if (registrosAnormales != null && !"".equals(registrosAnormales))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("registrosAnormales")), '%' + registrosAnormales.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<RegistroResultado> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back RegistroResultado entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<RegistroResultado> getAll()
   {

      CriteriaQuery<RegistroResultado> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(RegistroResultado.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(RegistroResultado.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final RegistroResultadoBean ejbProxy = this.sessionContext.getBusinessObject(RegistroResultadoBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((RegistroResultado) value).getIdRegistroResultado());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private RegistroResultado add = new RegistroResultado();

   public RegistroResultado getAdd()
   {
      return this.add;
   }

   public RegistroResultado getAdded()
   {
      RegistroResultado added = this.add;
      this.add = new RegistroResultado();
      return added;
   }
}
