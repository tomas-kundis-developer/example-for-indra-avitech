<!-- TOC -->
**TOC**

* [1. Prerequisites](#1-prerequisites)
* [2. Installation](#2-installation)
* [3. Build](#3-build)
* [4. Run](#4-run)
---
<!-- TOC -->

# 1. Prerequisites

Change your working directory to the project directory.

It is a directory where are located other subdirectories and files like `src/`, `pom.xml`, etc.

#### Tested Configuration

* Apache Maven version 3.8.4
* Java
  * Java version "17.0.3.1" 2022-04-22 LTS
    * Oracle Java(TM) SE Runtime Environment (build 17.0.3.1+2-LTS-6)
    * Oracle JDK version 17.0.3.1 

# 2. Installation

#### 1. Create and setup environment variables for concrete running environment.

For prepared production-scenario setup:

* in `/src/main/java/resources/` directory
    * copy file `.env-example` to `.env`

#### 2. Create database and database schema (tables).

For prepared production-scenario setup, run database migration with Maven build tool from cmd line.

> You should prepare this 3-step process every time before build, or running build/tests.

* clean project build:

` $ mvn clean`

* clean database:

`$ mvn flyway:clean`

* initialize (and create if not) the database:

`$ mvn flyway:migrate`

# 3. Build

`$ mvn clean install`

# 4. Run

#### Test scenario 1

> Those integration tests are excluded from Maven's build pipeline.

* in single-thread mode

`$ mvn -Dtest=UseCase100SingleThreadTest#testScenario1 test`
