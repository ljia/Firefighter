# Firefighter Take Home Assignment

## Notes

* The project is using Gradle to build. I changed the directory structure to use Gradle convention. For example:
    * source code moved to src/main/java
    * unit test class moved to src/test/java
* The project is tested with AdoptOpenJDK8

```bash
  > java -version
  openjdk version "1.8.0_292"
  OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_292-b10)
  OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.292-b10, mixed mode)
```

* Setup
  * The project is written and tested in IntelliJ
  * The project can be directly imported into IDE with Gradle support
  * Alternativaly, you can run the following commands to generate IntelliJ/Eclipse project files

```bash
  ./gradlew idea
  ./gradlew eclipse
```

* To run test:

```bash
  ./gradlew clean test
```

Generated results in:

```bash
  app/build/reports/tests/test/index.html
```

* Two extra methods are added in Firefighter

```java
  void travelTo(CityNode destination);
  void resetDistanceTraveled();
```

* The algorithm is using recursive function. If there is performance requirement, then an iterative way may be explored and implemented