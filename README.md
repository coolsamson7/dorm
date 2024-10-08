[![Java CI with Maven](https://github.com/coolsamson7/dorm/actions/workflows/maven.yml/badge.svg)](https://github.com/coolsamson7/dorm/actions/workflows/maven.yml)

# dORM

_dOrm_ is a complete dynamic orm ( Object Relational Mapper ), that doesn't require to setup custom table structures per entity but is able to map
dynamic entities on a database based on a couple of technical tables only. In addition to the server side api a GraphQL interface has been implemented with identical scope.

## What's the problem anyway...

Before we describe the solution, let's figure out what the problem is....

Typical applications use the normal ORMs that persist entities on table structures that are known at compile time predefined in the underlying dbms. 
While this is fine for most cases, there are some situations, where we need to configure entities dynamically.
Think of a custom workflow definition ( e.g. with Camunda ) that wants to persist some complex internal state, which is not known upfront.

Exactly this problem is solved by the current implementation.

## Sample 
Let's look a simple example first:

Assuming we have an injected `ObjectManager` which is responsible for transaction and object lifecycle management, we are able to
specify an entity by defining the attributes including type and type constraint information


```Kotlin
personDescriptor = manager.type("person")
   .add(attribute("name").type(string().length(100))) // string with length constraint
   .add(attribute("age").type(int().greaterThan(0)))
   .add(relation("parents").target("person").multiplicity(Multiplicity.ONE_OR_MANY).inverse("children"))
   .add(relation("children").target("person").multiplicity(Multiplicity.ZERO_OR_MANY).inverse("parents"))
   ...
   .register()
``` 

With this structural information - which is also persisted in the database - we can create and access `DataObject` instances, that carry the payload information

        
```Kotlin
manager.begin()
try {
    val parent = manager.create(personDescriptor)

    // set some values by the custom get and set operators
    
    person["name"] = "Andi"
    person["age"] = 58

   val child = manager.create(personDescriptor)

    // set some values by the custom get and set operators
    
    child["name"] = "Nika"
    child["age"] = 14

    // add relation

    parent.relation("children").add(child)

}
finally {
    manager.commit() // will create!
}
``` 

Let's query all persons persisted so far...

```Kotlin
val queryManager = manager.queryManager()
manager.begin()
try {
    val person = queryManager.from(personDescriptor)
    val query = queryManager
        .create()
        .select(person)
        .from(person)

    val queryResult = query.execute().getResultList()

    val name = queryResult[0]["name"]

    // let's modify values

    queryResult[0]["age"] = 30 // better!
}
finally {
    manager.commit() // will update all dirty objects
}
``` 

Since the objects are managed by the `ObjectManager` we can update values easily. The manager will know about any changes and will
persist them at the end of the transaction.

Projections are possible as well, as seen here

```Kotlin
val queryManager = manager.queryManager()
manager.begin()
try {
    val person = queryManager.from(personDescriptor)
    val children = person.join("children")
    val query = queryManager
        .create()
        .select(person.get("age"), person.get("name")
        .where(eq(children.get("name"), "Nika"))

    val tupleResult = query.execute().getResultList()

    val name = tupleResult[0][1]
}
finally {
    manager.commit()
}
``` 

In addition to a criteria api like query, we are of course able to specify hql like queries as well:

```Kotlin
val queryManager = manager.queryManager()

manager.begin()
try {
    val query = manager.query<DataObject>("SELECT p.name FROM person AS p JOIN p.children as children WHERE children.name = :name")

    val queryResult = query.executor()
        .set("name", "NIka")
        .execute()
        .getResultList()

        ...
}
finally {
    manager.commit() // will update
}
```
## Solution design

The solution is pretty straight forward. Entities are stored as a combination of three technical tables
* `ENTITY` a table referencing the entity definition and a generated primary key
* `PROPERTY` a table that will store single attributes of an entity
* `RELATION` a bridge table expressing object relations

The property table defines the columns

* `TYPE` the id of the entity structure
* `ENTITY` the id of the corresponding entity
* `PROPERTY` the property name

and a number of columns that are able to store payload data with respect to the supported low-level data types
* `STRING_VALUE` a string value
* `INT_VALUE` a int value ( stores boolean values well )
* `DOUBLE_VALUE` a floating point value

In order to model relations, the property table has a reflexive relation that expresses relationships stpored in a bridge table.

As the definition of an entity is known, the engine will know which attributes are stored in which columns.

Let's look at a simple query, that will read a single person.

```Sql
  select
        p.PROPERTY,
        p.ENTITY,
        p.DOUBLE_VALUE,
        p.INT_VALUE,
        p.STRING_VALUE,
        p.TYPE 
    from
        PROPERTY p 
    where
        p.ENTITY=?
```

After reading the result set, the engine will create the appropriate `DataObject` instance and store the appropriate values in the correct places.

If we talk about queries, that code gets a little bit more complicated. Querying for an integer attribute "age" with the operator "=" and value 58 will result in something like
```Sql
select
        p1_0.ATTRIBUTE,
        p1_0.ENTITY,
        p1_0.DOUBLE_VALUE,
        p1_0.INT_VALUE,
        p1_0.STRING_VALUE,
        p1_0.TYPE 
    from
        PROPERTY p1_0 
    where
        p1_0.ENTITY in (
            (select distinct p2_0.ENTITY 
               from PROPERTY p2_0 
              where
                    p2_0.TYPE="person" 
                    and p2_0.ATTRIBUTE="age" 
                    and p2_0.INT_VALUE=58)) 
    order by
        p1_0.ENTITY
```

## Performance

Of course, the performance and storage requirements are not as good as if we would map on static tables, since
* we have a lot a attribute rows
* indexes are much bigger
* we require a subselect for every condition

Let's look at a simple benchmark, that
* creates objects
* rereads all objects
* filters objects ( that will match all objects )
* filters and projects to a single attribute
* updates a single property flushing all objects

The test was repeated with 2 scenarios executing 2000 times each.
* JPA entity with 10 properties
* DORM object with 1 properties
* DORM object with 10 properties

The results ( avg time per object in ms ) are based on a H2 database ( on my old macbook :-) ). 
|Test              |   JPA  | DORM(1) | DORM(10) |
|------------------|--------|---------|----------|
| Create           | 0.6525 | 0.6735  |  2.2275  |
| Read             | 0.1885 | 0.644   |  0.7285  |
| Filter           | 0.1    | 0.66    |  0.558   |
| Filter & Project | 0.007  | 0.426   |  0.183   |
| Update           | 0.357  | 1.024   |  0.6755  |

As you can see, the biggest difference is the create test, since it has to create a row per property.
Reading is surpisingly fast, even though a lot of rows need to be read and processed.
Update is almost even, since only a single property was changed. The difference would grow again, the more properties are touched.

Still, not bad, huh?

## GraphQL

In addition to the supplied Kotlin API a GraphQL server is available giving access to all CRUD and Query possibilities. 
The internal schema is created dynamically based on the internal registry.

For the already mentioned `Person` object, it will create a schema based on the following technical types and inputs

```GraphQL
type OperationResult {
  count: Int
}

input FloatFilter {
  eq: Float
  ge: Float
  gt: Float
  le: Float
  lt: Float
  ne: Float
}

input IntFilter {
  eq: Int
  ge: Int
  gt: Int
  le: Int
  lt: Int
  ne: Int
}

input StringFilter {
  eq: String
  ne: String
}

input BooleanFilter {
  eq: Boolean
  ne: Boolean
}
```
resulting in
```GraphQL
type Person {
  id: Int
  name: String
  age: Int
  children: [Person]
  father: Person
}

input PersonInput {
  id: Int
  name: String
  age: Int
  children: [PersonInput]
  father: PersonInput
}

input PersonFilter {
  age: IntFilter
  and: [PersonFilter]
  father: PersonFilter
  id: IntFilter
  name: StringFilter
  or: [PersonFilter]
}

type Query {
  Person(where: PersonFilter): [Person]
}

type Mutation {
  createPerson(input: PersonInput): Person
  deletePersons(where: PersonFilter): OperationResult
  updatePerson(input: PersonInput): Person
  updatePersons(input: PersonInput, where: PersonFilter): [Person]
}
```

A query - already showing a join - will look like:

```GraphQL
query sampleQuery { Person (where: {
   father: {name: {eq: "Andi"}}}) {    
      id
      name
      father {
         id
         name
      }
   }
}
```

An bulk update:
```GraphQL
mutation {
    updatePersons(
      where: {
        id: {eq: 1}
      },
      input: { age: 59, name: "Andi" }) {
        id
        name
    }
}
```

## Reference

Check the [Wiki](https://github.com/coolsamson7/dorm/wiki) for detailed information.
