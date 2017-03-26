[![MIT License](https://img.shields.io/github/license/traversals/kapsule.svg)](https://github.com/traversals/kapsule/blob/master/LICENSE)
[![Kotlin 1.0.7](https://img.shields.io/badge/Kotlin-1.0.7-blue.svg)](http://kotlinlang.org)
[![Build Status](https://travis-ci.org/traversals/kapsule.svg?branch=master)](https://travis-ci.org/traversals/kapsule)
[![Bintray](https://img.shields.io/bintray/v/traversals/kapsule/kapsule-core.svg)](https://bintray.com/traversals/kapsule/kapsule-core)
[![Maven Central](https://img.shields.io/maven-central/v/space.traversal.kapsule/kapsule-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22space.traversal.kapsule%22)

# Kapsule 

Minimalist dependency injection library for Kotlin.

**Note:** Kapsule is currently in pre-release stage of development. Although every release is well-tested and ready for use, breaking changes can be introduced without advanced warning. However, the API should stabilize by the time version 1.0 comes out.

Why create another dependency injection library? Here are the objectives pursued by Kapsule:

* Simple features that most projects will use
    - Alternative for projects whose dependency injection needs are simple
* Keep the method count to a minimum
    - Dependency injection shouldn't take thousands of methods to implement
* No annotation processing
    - No need for `lateinit` on properties and they can be private and read-only
* No magic, keep everything as a hard reference
    - Reading code is easier when you can click through all the references in your IDE
* Utilize the power of Kotlin
    - Simplify code with its language features, instead of maximizing Java compatibility

To accomplish all of these, Kapsule is based on [delegation](http://kotlinlang.org/docs/reference/delegation.html) and [delegated properties](http://kotlinlang.org/docs/reference/delegated-properties.html) in Kotlin. 
  
## Getting started

Basic injection can be set up in just two steps.

### Step 1: Create a module

First you need to define a module to provide the values for injection. It's a regular Kotlin class, so feel free to initialize properties however you like, including `lazy` expressions and custom getters.

```kotlin
class Module {
    val name = "SomeName"
    val manager = Manager()
}
```

### Step 2: Inject properties

Now consider the class `Screen` that needs properties to be injected. You need to instantiate an injection Kapsule (hence the name). Invoking it as a function returns a uninitialized property delegate. 

Once you obtain the module, which can be stored in the application context, use it to inject the Kapsule and therefore initialize the values of the above properties (this can be in an asynchronous callback method, e.g. `onCreate()` in Android).    

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

## Advanced setup

Injection consists of two parts:

* Modules - where  

### Modules

TODO

### Delegates

TODO

### Example

TODO

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
