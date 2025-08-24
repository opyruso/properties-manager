# properties-manager

## Build

Use `build-all.sh` to compile all Java modules in the correct order so that dependent libraries are available in the local Maven repository:

```sh
./build-all.sh
```

This script installs `propertiesmanager-api-lib` before `propertiesmanager-security-lib`, preventing the "Could not find artifact propertiesmanager-api-lib" errors seen when building modules individually.
