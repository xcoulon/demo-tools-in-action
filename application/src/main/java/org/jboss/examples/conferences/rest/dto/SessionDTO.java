package org.jboss.examples.conferences.rest.dto;

import java.io.Serializable;
import org.jboss.examples.conferences.domain.Session;
import javax.persistence.EntityManager;
import org.jboss.examples.conferences.rest.dto.NestedConferenceDTO;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import org.jboss.examples.conferences.rest.dto.NestedSpeakerDTO;
import org.jboss.examples.conferences.domain.Speaker;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionDTO implements Serializable
{

   private long id;
   private Integer version;
   private NestedConferenceDTO conference;
   private String description;
   private Date endTime;
   private String location;
   private String name;
   private Date startTime;
   private Set<NestedSpeakerDTO> speakers = new HashSet<NestedSpeakerDTO>();

   public SessionDTO()
   {
   }

   public SessionDTO(final Session entity)
   {
      if (entity != null)
      {
         this.id = entity.getId();
         this.version = entity.getVersion();
         this.conference = new NestedConferenceDTO(entity.getConference());
         this.description = entity.getDescription();
         this.endTime = entity.getEndTime();
         this.location = entity.getLocation();
         this.name = entity.getName();
         this.startTime = entity.getStartTime();
         Iterator<Speaker> iterSpeakers = entity.getSpeakers().iterator();
         while (iterSpeakers.hasNext())
         {
            Speaker element = iterSpeakers.next();
            this.speakers.add(new NestedSpeakerDTO(element));
         }
      }
   }

   public Session fromDTO(Session entity, EntityManager em)
   {
      if (entity == null)
      {
         entity = new Session();
      }
      entity.setVersion(this.version);
      if (this.conference != null)
      {
         entity.setConference(this.conference.fromDTO(
               entity.getConference(), em));
      }
      entity.setDescription(this.description);
      entity.setEndTime(this.endTime);
      entity.setLocation(this.location);
      entity.setName(this.name);
      entity.setStartTime(this.startTime);
      Iterator<Speaker> iterSpeakers = entity.getSpeakers().iterator();
      while (iterSpeakers.hasNext())
      {
         boolean found = false;
         Speaker speaker = iterSpeakers.next();
         Iterator<NestedSpeakerDTO> iterDtoSpeakers = this.getSpeakers()
               .iterator();
         while (iterDtoSpeakers.hasNext())
         {
            NestedSpeakerDTO dtoSpeaker = iterDtoSpeakers.next();
            if (((Long) dtoSpeaker.getId()).equals((Long) speaker.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            iterSpeakers.remove();
         }
      }
      Iterator<NestedSpeakerDTO> iterDtoSpeakers = this.getSpeakers()
            .iterator();
      while (iterDtoSpeakers.hasNext())
      {
         boolean found = false;
         NestedSpeakerDTO dtoSpeaker = iterDtoSpeakers.next();
         iterSpeakers = entity.getSpeakers().iterator();
         while (iterSpeakers.hasNext())
         {
            Speaker speaker = iterSpeakers.next();
            if (((Long) dtoSpeaker.getId()).equals((Long) speaker.getId()))
            {
               found = true;
               break;
            }
         }
         if (found == false)
         {
            Iterator<Speaker> resultIter = em
                  .createQuery("SELECT DISTINCT s FROM Speaker s",
                        Speaker.class).getResultList().iterator();
            while (resultIter.hasNext())
            {
               Speaker result = resultIter.next();
               if (((Long) result.getId()).equals((Long) dtoSpeaker
                     .getId()))
               {
                  entity.getSpeakers().add(result);
                  break;
               }
            }
         }
      }
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

   public NestedConferenceDTO getConference()
   {
      return this.conference;
   }

   public void setConference(final NestedConferenceDTO conference)
   {
      this.conference = conference;
   }

   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(final String description)
   {
      this.description = description;
   }

   public Date getEndTime()
   {
      return this.endTime;
   }

   public void setEndTime(final Date endTime)
   {
      this.endTime = endTime;
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

   public Date getStartTime()
   {
      return this.startTime;
   }

   public void setStartTime(final Date startTime)
   {
      this.startTime = startTime;
   }

   public Set<NestedSpeakerDTO> getSpeakers()
   {
      return this.speakers;
   }

   public void setSpeakers(final Set<NestedSpeakerDTO> speakers)
   {
      this.speakers = speakers;
   }
}