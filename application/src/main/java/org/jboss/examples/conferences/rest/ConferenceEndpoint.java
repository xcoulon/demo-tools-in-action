package org.jboss.examples.conferences.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.jboss.examples.conferences.rest.dto.ConferenceDTO;
import org.jboss.examples.conferences.domain.Conference;

/**
 * 
 */
@Stateless
@Path("/conferences")
@LoggingBinding
public class ConferenceEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;

   @POST
   @Consumes("application/json")
   public Response create(ConferenceDTO dto)
   {
      Conference entity = dto.fromDTO(null, em);
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(ConferenceEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") long id)
   {
      Conference entity = em.find(Conference.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") long id)
   {
      TypedQuery<Conference> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Conference c LEFT JOIN FETCH c.sessions WHERE c.id = :entityId ORDER BY c.id", Conference.class);
      findByIdQuery.setParameter("entityId", id);
      Conference entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      ConferenceDTO dto = new ConferenceDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<ConferenceDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Conference> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Conference c LEFT JOIN FETCH c.sessions ORDER BY c.id", Conference.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Conference> searchResults = findAllQuery.getResultList();
      final List<ConferenceDTO> results = new ArrayList<ConferenceDTO>();
      for (Conference searchResult : searchResults)
      {
         ConferenceDTO dto = new ConferenceDTO(searchResult);
         results.add(dto);
      }
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") long id, @Valid ConferenceDTO dto)
   {
      TypedQuery<Conference> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Conference c LEFT JOIN FETCH c.sessions WHERE c.id = :entityId ORDER BY c.id", Conference.class);
      findByIdQuery.setParameter("entityId", id);
      Conference entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      entity = dto.fromDTO(entity, em);
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
      }
      return Response.noContent().build();
   }
}
