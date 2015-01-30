package org.jboss.examples.conferenceschedule.rest.dto;

import java.io.Serializable;
import org.jboss.examples.conferenceschedule.domain.Conference;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;

public class NestedConferenceDTO implements Serializable
{

   private long id;
   private Integer version;
   private String location;
   private String name;
   private Date startDate;
   private Date endDate;

   public NestedConferenceDTO()
   {
   }

   public NestedConferenceDTO(final Conference entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.location = entity.getLocation();
         this.name = entity.getName();
         this.startDate = entity.getStartDate();
         this.endDate = entity.getEndDate();
      }
   }

   public Conference fromDTO(Conference entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Conference();
      }
      if (((Long) this.id) != null)
      {
         TypedQuery<Conference> findByIdQuery = em
               .createQuery(
                     "SELECT DISTINCT c FROM Conference c WHERE c.id = :entityId",
                     Conference.class);
         findByIdQuery.setParameter("entityId", this.id);
         try
         {
            entity = findByIdQuery.getSingleResult();
         }
         catch (javax.persistence.NoResultException nre)
         {
            entity = null;
         }
         return entity;
      }
      entity.setVersion(this.version);
      entity.setLocation(this.location);
      entity.setName(this.name);
      entity.setStartDate(this.startDate);
      entity.setEndDate(this.endDate);
      entity = em.merge(entity);
      return entity;
   }

   public long getId()
   {
      return this.id;
   }

   public void setId(final long id)
   {
      this.id = id;
   }

   public Integer getVersion()
   {
      return this.version;
   }

   public void setVersion(final Integer version)
   {
      this.version = version;
   }

   public String getLocation()
   {
      return this.location;
   }

   public void setLocation(final String location)
   {
      this.location = location;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public Date getStartDate()
   {
      return this.startDate;
   }

   public void setStartDate(final Date startDate)
   {
      this.startDate = startDate;
   }

   public Date getEndDate()
   {
      return this.endDate;
   }

   public void setEndDate(final Date endDate)
   {
      this.endDate = endDate;
   }
}