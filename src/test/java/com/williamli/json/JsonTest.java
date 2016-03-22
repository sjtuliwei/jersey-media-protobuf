package com.williamli.json;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.pakulov.jersey.protobuf.Example.Person;

public class JsonTest extends JerseyTest {
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(ProtobufResource.class);
    }

    @Path("/")
    public static class ProtobufResource {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Person getPerson() {
            return buildPerson();
        }

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public String createPerson(Person person) {
            return person.toString();
        }
    }

    @Test
    public void serializePerson() {
        Person person = target("/").request(MediaType.APPLICATION_JSON).get(Person.class);
        assertEquals(1, person.getId());
        assertEquals("John Doe", person.getName());
        assertEquals("john.doe@google.com", person.getEmail());
    }

    @Test
    public void deserilizePerson() {
        String result = target("/").request().post(Entity.entity(buildPerson(), MediaType.APPLICATION_JSON), String.class);
        assertEquals("name: \"John Doe\"\nid: 1\nemail: \"john.doe@google.com\"\n", result);
    }

//    @Test(expected = BadRequestException.class)
//    public void emptyBody() {
//        target("/").request().post(Entity.entity(null, MediaType.APPLICATION_JSON), String.class);
//    }

    public static Person buildPerson() {
        return Person.newBuilder()
                .setId(1)
                .setName("John Doe")
                .setEmail("john.doe@google.com").build();
    }
}
