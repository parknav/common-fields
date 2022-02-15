package com.parknav.common.fields;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.parknav.common.fields.demo.model.boat.Boat;
import com.parknav.common.fields.model.carrier.CarrierDemoService;
import com.parknav.common.fields.model.carrier.CarrierService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.parknav.common.fields.model.carrier.Carrier;
import com.parknav.common.fields.demo.model.person.Person;

public class CarrierTest {

	@Before
	public void init() {

		carrierService = new CarrierDemoService();
		
		id = "enterprise";
		
	}
	
	@Test
	public void testGet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.entity, FieldGraph.of(Person.Field.name, Person.Field.boat))
			.add(Carrier.Field.list, FieldGraph.of(Person.Field.name))
			.add(Carrier.Field.set, FieldGraph.of(Person.Field.name))
			.add(Carrier.Field.map, FieldGraph.of(Person.Field.name))
			.add(Carrier.Field.listlist, FieldGraph.of(Person.Field.boat))
			.add(Carrier.Field.setset, FieldGraph.of(Person.Field.boat))
			.add(Carrier.Field.maplist, FieldGraph.of(Person.Field.boat))
			.add(Carrier.Field.mapset, FieldGraph.of(Person.Field.boat))
			.add(Carrier.Field.mapmap, FieldGraph.of(Person.Field.boat))
			.add(Carrier.Field.mapentitylist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		// entity
		Assert.assertNotNull("entity should not be null",
			Optional.of(carrier)
				.filter(c -> c.hasFields(Carrier.Field.entity))
				.map(Carrier::getEntity)
				.orElse(null)
		);
		Assert.assertNotNull("entity.name should not be null",
			Optional.of(carrier)
				.filter(c -> c.hasFields(Carrier.Field.entity))
				.map(Carrier::getEntity)
				.filter(e -> e.hasFields(Person.Field.name))
				.map(Person::getName)
				.orElse(null)
		);
		Assert.assertNotNull("entity.boat should not be null",
			Optional.of(carrier)
				.filter(c -> c.hasFields(Carrier.Field.entity))
				.map(Carrier::getEntity)
				.filter(e -> e.hasFields(Person.Field.boat))
				.map(Person::getBoat)
				.orElse(null)
		);
		Assert.assertNull("entity.email should be null",
			Optional.of(carrier)
				.filter(c -> c.hasFields(Carrier.Field.entity))
				.map(Carrier::getEntity)
				.filter(e -> e.hasFields(Person.Field.email))
				.map(Person::getEmail)
				.orElse(null)
		);
		
		// list
		Assert.assertFalse("list should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getList, Carrier.Field.list)));
		for (Person person : carrier.getList()) {
			Assert.assertNotNull("list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNull("list.boat should be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("list.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
		}
		
		// set
		Assert.assertFalse("set should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSet, Carrier.Field.set)));
		for (Person person : carrier.getSet()) {
			Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNull("set.boat should be null", person.getIfPresent(person::getEmail, Person.Field.email));
		}

		// map
		Assert.assertFalse("map should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMap, Carrier.Field.map)));
		for (Person person : carrier.getMap().values()) {
			Assert.assertNotNull("map.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNull("map.boat should be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("map.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
		}
		
		// listlist
		Assert.assertFalse("listlist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getListList, Carrier.Field.listlist)));
		for (List<Person> list : carrier.getListList()) {
			Assert.assertFalse("listlist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("listlist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("listlist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("listlist.list.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		// setset
		Assert.assertFalse("setset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSetSet, Carrier.Field.setset)));
		for (Set<Person> set : carrier.getSetSet()) {
			Assert.assertFalse("setset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("setset.set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("setset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("setset.set.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		// maplist
		Assert.assertFalse("maplist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapList, Carrier.Field.maplist)));
		for (List<Person> list : carrier.getMapList().values()) {
			Assert.assertFalse("maplist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("maplist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("maplist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("maplist.list.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		// mapset
		Assert.assertFalse("mapset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapSet, Carrier.Field.mapset)));
		for (Set<Person> set : carrier.getMapSet().values()) {
			Assert.assertFalse("mapset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("mapset.set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapset.set.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		// mapmap
		Assert.assertFalse("mapmap should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapMap, Carrier.Field.mapmap)));
		for (Map<String, Person> map : carrier.getMapMap().values()) {
			Assert.assertFalse("mapmap.map should not be empty", map.isEmpty());
			for (Person person : map.values()) {
				Assert.assertNull("mapmap.map.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapmap.map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapmap.map.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		// mapentitylist
		Assert.assertFalse("mapentitylist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapEntityList, Carrier.Field.mapentitylist)));
		for (Map.Entry<Person, List<Person>> entry : carrier.getMapEntityList().entrySet()) {
			// key (Person)
			Person keyPerson = entry.getKey();
			Assert.assertNull("mapentitylist.key[person].name should be null", keyPerson.getIfPresent(keyPerson::getName, Person.Field.name));
			Assert.assertNotNull("mapentitylist.key[person].boat should not be null", keyPerson.getIfPresent(keyPerson::getBoat, Person.Field.boat));
			Assert.assertNull("mapentitylist.key[person].email should be null", keyPerson.getIfPresent(keyPerson::getEmail, Person.Field.email));
			// value
			Assert.assertFalse("mapentitylist.list should not be empty", entry.getValue().isEmpty());
			for (Person person : entry.getValue()) {
				Assert.assertNull("mapentitylist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapentitylist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapentitylist.list.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

	}

	@Test
	public void testExtendList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.list, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("list should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getList, Carrier.Field.list)));
		for (Person person : carrier.getList()) {
			Assert.assertNull("list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("list.boat.name should be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.list, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		for (Person person : carrier.getList()) {
			Assert.assertNotNull("list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("list.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testExtendSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.set, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("set should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSet, Carrier.Field.set)));
		for (Person person : carrier.getSet()) {
			Assert.assertNull("set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("set.boat.name should be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.set, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		for (Person person : carrier.getSet()) {
			Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("set.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testExtendMap() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.map, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("map should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMap, Carrier.Field.map)));
		for (Person person : carrier.getMap().values()) {
			Assert.assertNull("map.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("map.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.map, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		for (Person person : carrier.getMap().values()) {
			Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("set.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testExtendListList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.listlist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("listlist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getListList, Carrier.Field.listlist)));
		for (List<Person> list : carrier.getListList()) {
			Assert.assertFalse("listlist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("listlist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("listlist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("listlist.list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.listlist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		for (List<Person> list : carrier.getListList())
			for (Person person : list) {
				Assert.assertNotNull("listlist.list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("listlist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("listlist.list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		
	}
	
	@Test
	public void testExtendSetSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.setset, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("setset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSetSet, Carrier.Field.setset)));
		for (Set<Person> set : carrier.getSetSet()) {
			Assert.assertFalse("setset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("setset.set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("setset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("setset.set.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.setset, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		for (Set<Person> set : carrier.getSetSet())
			for (Person person : set) {
				Assert.assertNotNull("setset.set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("setset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("setset.set.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		
	}
	
	@Test
	public void testExtendMapList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.maplist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("maplist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapList, Carrier.Field.maplist)));
		for (List<Person> list : carrier.getMapList().values()) {
			Assert.assertFalse("maplist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("maplist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("maplist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("maplist.list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.maplist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		Assert.assertFalse("maplist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapList, Carrier.Field.maplist)));
		for (List<Person> list : carrier.getMapList().values()) {
			Assert.assertFalse("maplist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNotNull("maplist.list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("maplist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("maplist.list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}
	
	@Test
	public void testExtendMapSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapset, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapSet, Carrier.Field.mapset)));
		for (Set<Person> set : carrier.getMapSet().values()) {
			Assert.assertFalse("mapset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("mapset.set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapset.set.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapset, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		Assert.assertFalse("mapset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapSet, Carrier.Field.mapset)));
		for (Set<Person> set : carrier.getMapSet().values()) {
			Assert.assertFalse("mapset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNotNull("mapset.set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapset.set.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}
	
	@Test
	public void testExtendMapMap() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapmap, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapmap should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapMap, Carrier.Field.mapmap)));
		for (Map<String, Person> map : carrier.getMapMap().values()) {
			Assert.assertFalse("mapmap.map should not be empty", map.isEmpty());
			for (Person person : map.values()) {
				Assert.assertNull("mapmap.map.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapmap.map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapmap.map.email should be null", person.getIfPresent(person::getEmail, Person.Field.email));
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapmap, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		Assert.assertFalse("mapmap should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapMap, Carrier.Field.mapmap)));
		for (Map<String, Person> map : carrier.getMapMap().values()) {
			Assert.assertFalse("mapmap.map should not be empty", map.isEmpty());
			for (Person person : map.values()) {
				Assert.assertNotNull("mapmap.map.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapmap.map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapmap.map.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testExtendMapEntityList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapentitylist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapentitylist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapEntityList, Carrier.Field.mapentitylist)));
		for (Map.Entry<Person, List<Person>> entry : carrier.getMapEntityList().entrySet()) {
			// key
			Person keyPerson = entry.getKey();
			Assert.assertNull("mapentitylist.key[person].name should be null", keyPerson.getIfPresent(keyPerson::getName, Person.Field.name));
			Assert.assertNotNull("mapentitylist.key[person].boat should not be null", keyPerson.getIfPresent(keyPerson::getBoat, Person.Field.boat));
			Assert.assertNull("mapentitylist.key[person].boat.name should be null",
				Optional.of(keyPerson)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
			// value
			Assert.assertFalse("mapentitylist.list should not be empty", entry.getValue().isEmpty());
			for (Person person : entry.getValue()) {
				Assert.assertNull("mapentitylist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapentitylist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapentitylist.list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> ExtendedGraph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapentitylist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		carrier.extend(ExtendedGraph, carrierService);
		
		Assert.assertFalse("mapentitylist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapEntityList, Carrier.Field.mapentitylist)));
		for (Map.Entry<Person, List<Person>> entry : carrier.getMapEntityList().entrySet()) {
			// key
			Person keyPerson = entry.getKey();
			Assert.assertNotNull("mapentitylist.key[person].name should not be null", keyPerson.getIfPresent(keyPerson::getName, Person.Field.name));
			Assert.assertNotNull("mapentitylist.key[person].boat should not be null", keyPerson.getIfPresent(keyPerson::getBoat, Person.Field.boat));
			Assert.assertNotNull("mapentitylist.key[person].boat.name should not be null",
				Optional.of(keyPerson)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
			// value
			Assert.assertFalse("mapentitylist.list should not be empty", entry.getValue().isEmpty());
			for (Person person : entry.getValue()) {
				Assert.assertNotNull("mapentitylist.list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapentitylist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapentitylist.list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.list, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("list should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getList, Carrier.Field.list)));
		for (Person person : carrier.getList()) {
			Assert.assertNotNull("list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("list.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.list, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Person person : carrier.getList()) {
			Assert.assertNull("list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("list.boat.name should be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testIntersectSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.set, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("set should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSet, Carrier.Field.set)));
		for (Person person : carrier.getSet()) {
			Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("set.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.set, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Person person : carrier.getSet()) {
			Assert.assertNull("set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("set.boat.name should be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testIntersectMap() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.map, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("map should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMap, Carrier.Field.map)));
		for (Person person : carrier.getMap().values()) {
			Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNotNull("set.boat.name should not be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.map, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Person person : carrier.getMap().values()) {
			Assert.assertNull("set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
			Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
			Assert.assertNull("set.boat.name should be null",
				Optional.of(person)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
		}
		
	}

	@Test
	public void testIntersectListList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.listlist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("listlist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getListList, Carrier.Field.listlist)));
		for (List<Person> list : carrier.getListList()) {
			Assert.assertFalse("listlist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNotNull("list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.listlist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (List<Person> list : carrier.getListList()) {
			Assert.assertFalse("listlist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectSetSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.setset, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("setset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getSetSet, Carrier.Field.setset)));
		for (Set<Person> set : carrier.getSetSet()) {
			Assert.assertFalse("setset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNotNull("set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("set.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.setset, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Set<Person> set : carrier.getSetSet()) {
			Assert.assertFalse("setset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("set.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectMapList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.maplist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("maplist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapList, Carrier.Field.maplist)));
		for (List<Person> list : carrier.getMapList().values()) {
			Assert.assertFalse("maplist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNotNull("maplist.list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("maplist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("maplist.list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.maplist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (List<Person> list : carrier.getMapList().values()) {
			Assert.assertFalse("maplist.list should not be empty", list.isEmpty());
			for (Person person : list) {
				Assert.assertNull("maplist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("maplist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("maplist.list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectMapSet() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapset, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapset should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapSet, Carrier.Field.mapset)));
		for (Set<Person> set : carrier.getMapSet().values()) {
			Assert.assertFalse("mapset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNotNull("mapset.set.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapset.set.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapset, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Set<Person> set : carrier.getMapSet().values()) {
			Assert.assertFalse("mapset.set should not be empty", set.isEmpty());
			for (Person person : set) {
				Assert.assertNull("mapset.set.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapset.set.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapset.set.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectMapMap() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapmap, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapmap should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapMap, Carrier.Field.mapmap)));
		for (Map<String, Person> map : carrier.getMapMap().values()) {
			Assert.assertFalse("mapmap.map should not be empty", map.isEmpty());
			for (Person person : map.values()) {
				Assert.assertNotNull("mapmap.map.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapmap.map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapmap.map.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapmap, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Map<String, Person> map : carrier.getMapMap().values()) {
			Assert.assertFalse("mapmap.map should not be empty", map.isEmpty());
			for (Person person : map.values()) {
				Assert.assertNull("mapmap.map.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapmap.map.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapmap.map.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	@Test
	public void testIntersectMapEntityList() {

		FieldGraph<Carrier.Field> Graph = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapentitylist, FieldGraph.Builder.of(Person.Field.class)
				.add(Person.Field.name)
				.add(Person.Field.boat, FieldGraph.Builder.of(Boat.Field.class)
					.add(Boat.Field.name)
					.build()
				)
				.build()
			)
			.build()
		;

		Carrier carrier = carrierService.get(id, Graph);

		Assert.assertFalse("mapentitylist should not be empty", CollectionUtils.sizeIsEmpty(carrier.getIfPresent(carrier::getMapEntityList, Carrier.Field.mapentitylist)));
		for (Map.Entry<Person, List<Person>> entry : carrier.getMapEntityList().entrySet()) {
			// key
			Person keyPerson = entry.getKey();
			Assert.assertNotNull("mapentitylist.key[person].name should not be null", keyPerson.getIfPresent(keyPerson::getName, Person.Field.name));
			Assert.assertNotNull("mapentitylist.key[person].boat should not be null", keyPerson.getIfPresent(keyPerson::getBoat, Person.Field.boat));
			Assert.assertNotNull("mapentitylist.key[person].boat.name should not be null",
				Optional.of(keyPerson)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
			// value
			Assert.assertFalse("mapentitylist.list should not be empty", entry.getValue().isEmpty());
			for (Person person : entry.getValue()) {
				Assert.assertNotNull("mapentitylist.list.name should not be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapentitylist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNotNull("mapentitylist.list.boat.name should not be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}

		FieldGraph<Carrier.Field> Intersection = FieldGraph.Builder.of(Carrier.Field.class)
			.add(Carrier.Field.mapentitylist, FieldGraph.of(Person.Field.boat))
			.build()
		;

		carrier.intersect(Intersection);
		
		for (Map.Entry<Person, List<Person>> entry : carrier.getMapEntityList().entrySet()) {
			// key
			Person keyPerson = entry.getKey();
			Assert.assertNull("mapentitylist.key[person].name should be null", keyPerson.getIfPresent(keyPerson::getName, Person.Field.name));
			Assert.assertNotNull("mapentitylist.key[person].boat should not be null", keyPerson.getIfPresent(keyPerson::getBoat, Person.Field.boat));
			Assert.assertNull("mapentitylist.key[person].boat.name should be null",
				Optional.of(keyPerson)
					.filter(p -> p.hasFields(Person.Field.boat))
					.map(Person::getBoat)
					.filter(b -> b.hasFields(Boat.Field.name))
					.map(Boat::getName)
					.orElse(null)
			);
			// value
			Assert.assertFalse("mapentitylist.list should not be empty", entry.getValue().isEmpty());
			for (Person person : entry.getValue()) {
				Assert.assertNull("mapentitylist.list.name should be null", person.getIfPresent(person::getName, Person.Field.name));
				Assert.assertNotNull("mapentitylist.list.boat should not be null", person.getIfPresent(person::getBoat, Person.Field.boat));
				Assert.assertNull("mapentitylist.list.boat.name should be null",
					Optional.of(person)
						.filter(p -> p.hasFields(Person.Field.boat))
						.map(Person::getBoat)
						.filter(b -> b.hasFields(Boat.Field.name))
						.map(Boat::getName)
						.orElse(null)
				);
			}
		}
		
	}

	private CarrierService carrierService;
	
	private String id;
	
}
