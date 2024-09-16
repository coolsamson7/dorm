# dORM

_dOrm_ is a complete dynamic orm ( Object Relational Mapper ), that doesn't require to setup custom table structures per entity but is able to map
dynamic entities on a database based on a couple of technical tables only.

## What's the problem anyway...

Before we describe the solution, let's figure out wht the problem is....

Typical applications use the normal ORMs that persist entities on table structures that are known at compile time and 
relie on existing tables. 
While this is fine for most cases, there are some situations, where we need to configure entities dynamically.
Think of a custom workflow definition ( e.g. with Camunda ) that wants to persist some complex internal state, which is not known upfront.

Exactly this problem is solved by the current implementation.

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
