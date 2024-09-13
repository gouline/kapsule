# Kapsule 

[![GitHub Actions](https://github.com/gouline/kapsule/actions/workflows/master.yml/badge.svg)](https://github.com/gouline/kapsule/actions/workflows/master.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/gouline/molot/blob/master/LICENSE)
[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://gouline.github.io/kapsule/)

Minimalist dependency injection library for Kotlin.

Why create another dependency injection library? Here are the objectives pursued by Kapsule:

* Simple features that most projects will have use for
    - Alternative for projects whose dependency injection needs are quite basic
* Keep the method count to a minimum
    - Dependency injection shouldn't take thousands of methods to implement
* No annotation processing
    - No need for `lateinit` on properties and they can be private and read-only
* No magic, keep everything as a hard reference
    - Reading code is easier when you can click through all the references in your IDE
* Utilize the power of Kotlin
    - Use language features to simplify code instead of focusing on Java compatibility 

To accomplish all of these, Kapsule is based on [delegation](http://kotlinlang.org/docs/reference/delegation.html) and [delegated properties](http://kotlinlang.org/docs/reference/delegated-properties.html). 

## Table of Contents

* [Getting Started](#getting-started)
	- [Download](#download)
	- [Create a Module](#create-a-module)
	- [Store Module Instance](#store-module-instance)
	- [Inject Properties](#inject-properties)
* [Advanced Setup](#advanced-setup)
	- [Module Implementations](#module-implementations)
	- [Multiple Modules](#multiple-modules)
	- [Optional Delegates](#optional-delegates)
	- [Variable Delegates](#variable-delegates)
	- [Transitive Dependencies](#transitive-dependencies)
    - [Complex Dependencies](#complex-dependencies)
	- [Manual Injection](#manual-injection)
* [Samples](#samples)
* [Links](#links)

## Getting Started

### Download

To use Kapsule in your project, include it as a dependency.

#### Using Gradle:

~~~gradle
dependencies {
    compile "net.gouline.kapsule:kapsule-core:1.1"
}
~~~

#### Or Maven:

~~~xml
<dependency>
    <groupId>net.gouline.kapsule</groupId>
    <artifactId>kapsule-core</artifactId>
    <version>1.1</version>
</dependency>
~~~

### Create a Module

Define a module to provide the injected values. 

This can be any Kotlin class, so feel free to initialize properties however you like (including `lazy` expressions and custom getters).

~~~kotlin
class Module {
    val name = "SomeName"
    val manager get() = Manager()
}
~~~

Our simple example provides the same instance of `name` and a new instance of `Manager` for every property that requires it.

### Store Module Instance

Store the root module in your application context. This will depend on your framework, for example on Android, you would use the `Application` instance.

### Inject Properties

Now the injection target needs to be adjusted as follows:

* Implement `Inject<Module>` with the module you're injecting
* Declare the dependencies using `required` (or `optional`) references
* Retrieve the module instance from the application context
* Call `inject()` on the module to initialise the values

Looking at the example, let's say you have a class `Demo` that needs these injected values. The function passed for each declaration retrieves the value from `Module` that the given property expects. 

~~~kotlin
class Demo : Injects<Module> {
    private val name by required { name }
    private val manager by required { manager }

    init {
        inject(context.module)
    }
}
~~~

That's it, properties `name` and `manager` can now be used!

## Advanced Setup

The steps above show the most basic setup, which can be extended for more advanced use cases.

### Module Implementations

The basic setup uses one module called `Module`, but what if you need another implementation that returns stub values for tests? 

You can define an interface and provide two different implementations:
 
~~~kotlin
interface Module {
    val name: String
    val manager: Manager
}
~~~
 
The module used before can be the main implementation:
 
~~~kotlin
class MainModule : Module {
    override val name = "SomeName"
    override val manager get() = Manager()
}
~~~

And another `TestModule` can return the stub values:
 
~~~kotlin
class TestModule : Module {
    override val name = "SomeTestName"
    override val manager get() = TestManager()
}
~~~

Your injection routine stays the same, except now `Module` is a generic interface, you just have to provide the appropriate module through your application context (i.e. what is depicted as `Application.module` in the example).

### Multiple Modules

You may also want to define multiple modules (with some logical separation) combined into one interface used for injection. Let's say you have a `CoffeeModule` as follows:

~~~kotlin
interface CoffeeModule {
    val coffeeType: String
}
~~~

And another `TeaModule`:

~~~kotlin
interface TeaModule {
    val teaType: String
}
~~~

As described in the previous section, you can have one or more implementations for each module (omitted for brevity) that you can combine them into one `Module`:

~~~kotlin
class Module(coffee: CoffeeModule, tea: TeaModule) : 
    CoffeeModule by coffee, TeaModule by tea
~~~

Now your `Module` contains all the properties and functions of `CoffeeModule` and `TeaModule` courtesy of Kotlin's delegation support. 

When you're instantiating the global module (stored in your application context), you can provide the required implementation for each submodule:

~~~kotlin
class Application {
    val module = Module(
        coffee = MainCoffeeModule() // or TestCoffeeModule()
        tea = MainTeaModule() // or TestTeaModule()
    )
}
~~~

### Optional Delegates

So far you've only seen non-null values, but what happens if you need to inject a nullable value? You can use the `optional` function on your Kapsule:

~~~kotlin
val firstName by required { firstName }
val lastName by optional { lastName }
~~~

Given both fields are strings, `firstName` is `String`, while `lastName` is `String?`.

Unlike non-null properties, nullable ones can be read even before injection (the former would throw `KotlinNullPointerException`), they will just be null.

### Variable Delegates

In most cases you would make the injected properties `val`, however there's no reason it can't be a `var`, which would allow you to reassign it before or after injection.

~~~kotlin
var firstName by required { firstName }

init {
    firstName = "before"
    kap.inject(Application.module)
    firstName = "after"
}
~~~

Note that any delegates can be injected repeatedly, regardless of whether they're `val` or `var`, because the initialized value is contained within the delegate and it's a nullable `var`.

### Transitive Dependencies

Consider `UserDao` and an authenticator `Auth` that depends on it. Except the former is provided by `DataModule`, but the latter comes from `LogicModule`.

~~~kotlin
class Module(
        data: DataModule, logic: LogicModule) : 
        DataModule by data, LogicModule by logic

interface DataModule {
    val userDao: UserDao
}

interface LogicModule {
    val auth: Auth
}
~~~

This is where the transitive injection comes in, the `UserDao` can be injected into the `LogicModule`.

~~~kotlin
class MainDataModule : DataModule {
    override val userDao get() = UserDao()
}

class MainLogicModule : LogicModule, Injects<Module> {
    private val userDao by required { userDao }
    override val auth get() = Auth(userDao)
}
~~~

Looks good, but it won't work without two modifications to `Module`:

* It has to implement `HasModules` to suggest that it contains submodules
* `HasModules` requires you to define the module instances

~~~kotlin
class Module(
        data: DataModule, logic: LogicModule) : 
        DataModule by data, LogicModule by logic,
        HasModules {
    override val modules = setOf(data, logic)
}
~~~

Finally, when instantiating the module, you need to call `transitive()` on the module to traverse the tree and inject the `Module` into any submodules depending on it.

~~~kotlin
val module = Module(MainDataModule(), MainLogicModule()).transitive()
~~~

Note that circular dependencies are not supported, because resolution is performed iteratively and in a single pass. Take care in defining your dependency structure to avoid this situation.

### Complex Dependencies

Traditionally, injected values are kept in the same structure as they are provided by the modules. However, considering that injection functions in Kapsule are essentially just future values that will become accessible after injection happened, you can do anything you would do outside of that function, inside.

Consider the example module implementations from the [transitive dependencies](#transitive-dependencies) section:

~~~kotlin
class MainDataModule : DataModule {
    override val userDao get() = UserDao()
}

class MainLogicModule : LogicModule, Injects<Module> {
    private val userDao by required { userDao }
    override val auth get() = Auth(userDao)
}
~~~

You'll notice that the `userDao` is being provided by the `MainDataModule` as a new instance each time. What if you wanted to reuse the same instance through the whole life of the module? You would just assign it as a value, rather than return it from a custom getter.

~~~kotlin
class MainDataModule : DataModule {
    override val userDao = UserDao()
}
~~~

But what about if you want to do the same with `auth` in `MainLogicModule`? You can't just assign `Auth(userDao)` to `auth`, because `Auth()` constructor is called when the `MainLogicModule` is instantiated and by that time the `userDao` hasn't been injected yet (remember, that gets done in the `transitive()` call). That means you need to instantiate `auth` in the same future, when `userDao` will become available.

~~~kotlin
class MainLogicModule : LogicModule, Injects<Module> {
    override val auth by required { Auth(userDao) }
}
~~~

Notice that you no longer need the `userDao` separately for that. Also note that `required`/`optional` choice now depends on what the return of the new function is, rather than whether or not `userDao` by itself is.

You can do other stuff in your injection functions, like convert the injected value into something else that you need for the current context.

~~~kotlin
class Demo : Injects<Module> {
    private val authHttpClient by required { auth.httpClient }
    
    ...
}
~~~

In the above example, we're not just injecting the `auth` dependency from before, but also retrieving its `httpClient` property.

While you're not technically limited by what you can do inside these injection functions, try not to overdo it. It's similar to data bindings, where you *can* perform calculations and other complex calls right in the template file, but you know you shouldn't.

### Manual Injection

While the more convenient way to inject modules is by implementing the `Injects<Module>` interface, you may want to split the injection of separate modules (e.g. for testing). This can be done by creating separate instances of `Kapsule<Module>` and calling the injection methods on it.
 
~~~kotlin
class Screen {
    private val kap = Kapsule<Module>()
    private val name by kap.required { name }
    private val manager by kap.required { manager }

    init {
        kap.inject(Application.module)
    }
}
~~~

## Samples

For a sample project using Kapsule, see the [sample](./sample) directory. 

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.
