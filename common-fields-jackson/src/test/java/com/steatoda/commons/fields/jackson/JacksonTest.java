package com.steatoda.commons.fields.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steatoda.commons.fields.jackson.model.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

public class JacksonTest {

	@Before
	public void init() {
		mapper = new TestObjectMapper();
	}

	@Test
	public void testDeserialization() throws JsonProcessingException {

		String json =
			"{" +
			"	\"id\": \"1\"," +
			"	\"name\": \"dummy\"," +
			"	\"email\": null" +
			"}";

		Person person = mapper.readValue(json, Person.class);

		Assert.assertNotNull("person should not be null", person);
		Assert.assertNotNull("person should have ID", person.getId());
		Assert.assertFalse("person should have some fields set", person.getFields().isEmpty());
		Assert.assertTrue("person should have name field set", person.hasFields(Person.Field.name));
		Assert.assertNotNull("person's name shouldn't be null", person.getName());
		Assert.assertTrue("person should have email field set", person.hasFields(Person.Field.email));
		Assert.assertNull("person's email should be null", person.getEmail());
		Assert.assertFalse("person shouldn't have permissions field set", person.hasFields(Person.Field.permissions));

	}

	@Test
	public void testSerialization() throws JsonProcessingException {

		Person person = Person.ref("1")
			.setName("dummy")
			.setEmail(null)
		;

		String json = mapper.writeValueAsString(person);

		Assert.assertNotNull("JSON should not be null", json);
		Assert.assertTrue("JSON should contain ID field", Pattern.compile("\"id\"\\s*:\\s*\"1\"").matcher(json).find());
		Assert.assertTrue("JSON should contain name field", Pattern.compile("\"name\"\\s*:\\s*\"dummy\"").matcher(json).find());
		Assert.assertTrue("JSON should contain email field", Pattern.compile("\"email\"\\s*:\\s*null").matcher(json).find());
		Assert.assertFalse("JSON shouldn't contain permissions field", Pattern.compile("\"permissions\"\\s*:").matcher(json).find());

	}

	private ObjectMapper mapper;

}
