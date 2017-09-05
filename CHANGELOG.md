# Change Log

## Version 0.3 (2017-08-10)

* Support for transitive dependencies via `HasModules` interface and `transitive()` call on the root module.
* Functions `required()`, `optional()` and `inject()` extracted from `Injects<M>` interface into separate extension functions (this will require fixes to imports in files using them).

## Version 0.2 (2017-04-03)

* Automatic injection by implementing `Injects` interface.
* Retaining targets via a custom `WeakHashMap`.
* Sample project "android" updated to include Java class.
* Migrate to Bintray for publishing.
* Update samples and documentation to use automatic injection.

## Version 0.1 (2017-03-24)

* Initial published release.
* Working injection on `val` and `var` with a manual injector.
* Required (non-null) and optional (nullable) property delegates.
* Sample project "simple" with basic command-line application.
* Sample project "android" with a fully-functioning Android application.
* Basic documentation and directions for use.
