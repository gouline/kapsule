[![Kotlin 1.0.7](https://img.shields.io/badge/Kotlin-1.0.7-blue.svg)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/space.traversal.kapsule/kapsule-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22space.traversal.kapsule%22)
[![Bintray](https://img.shields.io/bintray/v/traversals/kapsule/kapsule-core.svg)](https://bintray.com/traversals/kapsule/kapsule-core)
[![MIT License](https://img.shields.io/github/license/traversals/kapsule.svg)](https://github.com/traversals/kapsule/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/traversals/kapsule.svg?branch=master)](https://travis-ci.org/traversals/kapsule)

# Kapsule 

Minimalist dependency injection library for Kotlin.

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
  
## Injection

TODO

## Modules

TODO

## Examples

TODO

## Download

To use Kapsule in your project, include it as a dependency:
  
```gradle
dependencies {
    compile "space.traversal.kapsule:kapsule-core:x.y.z"
}
```

Release builds are made available on `jcenter()` and `mavenCentral()` repositories.

## License

This project is licensed under the terms of the MIT license. See the [LICENSE](LICENSE) file.
