[![MIT License](https://img.shields.io/github/license/traversals/kapsule.svg)](https://github.com/traversals/kapsule/blob/master/LICENSE)
[![Kotlin 1.0.7](https://img.shields.io/badge/Kotlin-1.0.7-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/traversals/kapsule.svg?branch=master)](https://travis-ci.org/traversals/kapsule)
[![Bintray](https://img.shields.io/bintray/v/traversals/maven/kapsule.svg)](https://bintray.com/traversals/maven/kapsule)
[![Maven Central](https://img.shields.io/maven-central/v/space.traversal.kapsule/kapsule-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22space.traversal.kapsule%22)

# Kapsule 

Minimalist dependency injection library for Kotlin.

**Note:** Kapsule is currently in pre-release stages of development. While every milestone build is well-tested and ready for use, breaking changes can be introduced without warning. Once version 1.0 is released, the changes will become more gradual.

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
  
## Getting started

Basic injection can be set up in just two steps.

### Step 1: Create a module

Define a module to provide the injected values. It can be a regular Kotlin class, so feel free to initialize properties however you like (including `lazy` expressions and custom getters).

```kotlin
class Module {
    val name = "SomeName"
    val manager get() = Manager()
}
```

This will provide the same instance of `name` and a new instance of `Manager` for every property that requires it. 

### Step 2: Inject properties

Let's say you have a class `Screen` that needs these values. You need to create an injection Kapsule (hence the name) and invoke it as a function to map uninitialized delegates to your properties.    

```kotlin
class Screen {
    private val kap = Kapsule<Module>()
    private val name by kap { name }
    private val manager by kap { manager }
    
    init {
        kap.inject(Application.module)
    }
}
```

The function passed for each call retrieves the value in `Module` that the given property expects. 

Once you obtain the module instance, which can be stored in the application context, use it to inject the Kapsule and hence initialize the values of the above properties (this can be done in an asynchronous callback method, e.g. `onCreate()` in Android).

That's it, properties `name` and `manager` can now be used!

## Advanced setup

The steps above show the most basic setup, which can be extended for more advanced use cases.

### Module implementations

The basic setup uses one module called `Module`, but what if you need another implementation that returns stub values for tests? You can define an interface and provide two different implementations:
 
```kotlin
interface Module {
    val name: String
    val manager: Manager
}
```
 
The module used before can be the main implementation:
 
```kotlin
class MainModule : Module {
    override val name = "SomeName"
    override val manager get() = Manager()
}
```

And another `TestModule` can return the stub values:
 
```kotlin
class TestModule : Module {
    override val name = "SomeTestName"
    override val manager get() = TestManager()
}
```

Your injection routine stays the same, except now `Module` is a generic interface, you just have to provide the appropriate module through your application context (i.e. what is depicted as `Application.module` in the example).

### Multiple modules

You may also want to define multiple modules (with some logical separation) combined into one interface used for injection. Let's say you have a `CoffeeModule` as follows:

```kotlin
interface CoffeeModule {
    val coffeeType: String
}
```

And another `TeaModule`:

```kotlin
interface TeaModule {
    val teaType: String
}
```

As described in the previous section, you can have one or more implementations for each module (omitted for brevity) that you can combine them into one `Module` :

```kotlin
class Module(coffee: CoffeeModule, tea: TeaModule) : 
    CoffeeModule by coffee, TeaModule by tea
```

Now your `Module` contains all the properties and functions of `CoffeeModule` and `TeaModule` courtesy of Kotlin's delegation support. 

When you're instantiating the global module (stored in your application context), you can provide the required implementation for each submodule:

```kotlin
object Application {
    val module = Module(
        coffee = MainCoffeeModule() // or TestCoffeeModule()
        tea = MainTeaModule() // or TestTeaModule()
    )
}
```

### Optional delegates

So far you've only seen non-null values, but what happens if you need to inject a nullable value? You can use the `opt` function on your Kapsule:

```kotlin
val kap = Kapsule<Module>()
val firstName by kap { firstName }
val lastName by kap.opt { lastName }
```

Given both fields are strings, `firstName` is `String`, while `lastName` is `String?`. The default `kap {}` is actually shorthand for `kap.req {}`, so either can be used interchangeably.

Unlike non-null properties, nullable ones can be read even before injection (the former would throw `KotlinNullPointerException`). 

### Variable delegates

In most cases you would make the injected properties `val`, however there's no reason it can't be a `var`, which would allow you to reassign it before or after injection.

```kotlin
val kap = Kapsule<Module>()
var firstName by kap { firstName }
 
init {
    firstName = "before"
    kap.inject(Application.module)
    firstName = "after"
}
```

Note that any delegates can be injected repeatedly, regardless of whether they're `val` or `var`, because the initialized value is contained within the delegate and it's a nullable `var`.

## Kotlin 1.0 vs 1.1

Kotlin 1.1 infers property types from the delegates, which allows for a simpler definitions:

```kotlin
val firstName by kap { firstName }
val lastName by kap.opt { lastName }
```

However, when using 1.0, you have to specify the types explicitly:
 
```kotlin
val firstName by kap<String> { firstName }
val lastName by kap.opt<String?> { lastName }
```

## Samples

For sample projects using Kapsule, see the [samples](samples) directory. 

## Download

To use Kapsule in your project, include it as a dependency:
  
```gradle
dependencies {
    compile "space.traversal.kapsule:kapsule-core:x.y.z"
}
```

Releases are published to `jcenter()` and `mavenCentral()` repositories.

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.
