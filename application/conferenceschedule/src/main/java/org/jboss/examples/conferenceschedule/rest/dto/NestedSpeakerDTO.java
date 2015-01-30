package org.jboss.examples.conferenceschedule.rest.dto;

import java.io.Serializable;
import org.jboss.examples.conferenceschedule.domain.Speaker;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class NestedSpeakerDTO implements Serializable
{

   private long id;
   private Integer version;
   private String bio;
   private String name;
   private String twitterhandle;

   public NestedSpeakerDTO()
   {
   }

   public NestedSpeakerDTO(final Speaker entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.bio = entity.getBio();
         this.name = entity.getName();
         this.twitterhandle = entity.getTwitterhandle();
      }
   }

   public Speaker fromDTO(Speaker entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Speaker();
      }
      if (((Long) this.id) != null)
      {
         TypedQuery<Speaker> findByIdQuery = em.createQuery(
               "SELECT DISTINCT s FROM Speaker s WHERE s.id = :entityId",
               Speaker.class);
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
      entity.setBio(this.bio);
      entity.setName(this.name);
      entity.setTwitterhandle(this.twitterhandle);
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

   public String getBio()
   {
      return this.bio;
   }

   public void setBio(final String bio)
   {
      this.bio = bio;
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   public String getTwitterhandle()
   {
      return this.twitterhandle;
   }

   public void setTwitterhandle(final String twitterhandle)
   {
      this.twitterhandle = twitterhandle;
   }
}