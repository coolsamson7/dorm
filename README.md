# dORM

_dOrm_ is a complete dynamic orm ( Object Relational Mapper ), that doesn't require to setup custom table structures per entity but is able to map
dynamic entities on a database based on a couple of technical tables only.

## What's the problem anyway...

Before we describe the solution, let's figure out what the problem is....

Typical applications use the normal ORMs that persist entities on table structures that are known at compile time and 
relie on predefined tables. 
While this is fine for most cases, there are some situations, where we need to configure entities dynamically.
Think of a custom workflow definition ( e.g. with Camunda ) that wants to persist some complex internal state, which is not known upfront.

Exactly this problem is solved by the current implementation.

## Sample 
Let's look a simple example first:

Assuming we have an injected `ObjectManager` which is responsible for transaction and object lifecycle management, we are able to
specify an entity by defining the attributes including type and type constraint information


```Kotlin
personDescriptor = manager.type("person")
   .attribute("name", string().length(100)) // string with length constraint
   .attribute("age", int())
   ...
   .register()
``` 

With this structural information - which is alos persisted in the database - we can create and access `DataObject` instances, that carry the payload information

        
```Kotlin
manager.begin()
try {
    val person = manager.create(personDescriptor)

    // set some values
    
    person["name"] = "Andi"
    person["age"] = 58
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

    val queryResult = query.execute().getResultList() // we know, just one so far!

    val name = queryResult[0]["name"]

    // let's modify values

    queryResult[0]["name"] = name + "X"
}
finally {
    manager.commit()
}
``` 

Since the objects are managed by the `ObjectManager` we can update values easily. The manager will know aboutr any changes and will
persist them at the end of the transaction.

In addition to a criteria api like query, we are of course able to specify hql like queries as well:

```Kotlin
val queryManager = manager.queryManager()

manager.begin()
try {
    val query = manager.query<DataObject>("SELECT p.name FROM person AS p WHERE p.age = :age AND p.name = :name")

    val queryResult = query.executor()
        .set("age", 58)
        .set("name", "Andi")
        .execute()
        .getResultList()

        ...
}
finally {
    manager.commit() // will update
}
```
## Solution design

The solution is pretty simple: Entities are stored as a combination of two technical tables
* `ENTITY` a table referencing the entity definition and a generated primary key
* `ATTRIBUTE` a table that will store all attributes of an entity

The attribute table defines the columns

* 'TYPE' the id of the entity structure
* 'ENTITY' the id of the corresponding entity

and a number of columns that are able to store payload data
* 'STRING_VALUE' and string value
* 'INT_VALUE' int values
* 'DOUBLE_VALUE' floating point values

As the definition of an entity is known, the engine will know which attributes are stored in what columns.

Let's look at a simple query, that will read all persons.

```Sql
  select
        ae1_0.ATTRIBUTE,
        ae1_0.ENTITY,
        ae1_0.DOUBLE_VALUE,
        ae1_0.INT_VALUE,
        ae1_0.STRING_VALUE,
        ae1_0.TYPE 
    from
        ATTRIBUTE ae1_0 
    where
        ae1_0.ENTITY=?
```

If we talk about queries, that code gets a little bit more complicated. Querying for an attribute "age" by the operator "=" will result in
```Sql
select
        ae1_0.ATTRIBUTE,
        ae1_0.ENTITY,
        ae1_0.DOUBLE_VALUE,
        ae1_0.INT_VALUE,
        ae1_0.STRING_VALUE,
        ae1_0.TYPE 
    from
        ATTRIBUTE ae1_0 
    where
        ae1_0.ENTITY in (
            (select distinct ae2_0.ENTITY 
               from ATTRIBUTE ae2_0 
              where
                    ae2_0.TYPE=? 
                    and ae2_0.ATTRIBUTE=? 
                    and ae2_0.INT_VALUE=?)) 
    order by
        ae1_0.ENTITY
```

## Performance

Of course, the performance is not as good as if we would map on static tables, since
* we have a lot a attribute rows
* indexes are much bigger
* we required s subselect for every condition

Let's look at some benchmarks:

TODO

## Possible Optimizations

## Reference

TODO