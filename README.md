# CooperateModelRepository

[![Build Status](https://img.shields.io/travis/Cooperate-Project/CooperateModelRepository.svg)](https://travis-ci.org/Cooperate-Project/CooperateModelRepository)

This repository stores the code of the Cooperate Model Repository server.

## Installation
* Snapshots: (https://mammutbaum36.fzi.de/repository/snapshots/modelrepository/)

## Usage
* Choose the product from one of the upper URLs that matches your server's architecture
* Unzip it anywhere you like
* Create a folder called config and add a CDO config file (we will provide sample files and more detailed descriptions later)

## Pitfalls

### OCL
The OCL package cannot be registered dynamically but have to be registered manually. Add the following lines to your repository configuration:
```xml
<initialPackage nsURI="http://www.eclipse.org/ocl/1.1.0/OCL/Expressions"/>
<initialPackage nsURI="http://www.eclipse.org/ocl/1.1.0/OCL/Types"/>
<initialPackage nsURI="http://www.eclipse.org/ocl/1.1.0/OCL/Utilities"/>
```

### QVT-O Trace
The meta model for the QVT-O trace should be registered statically. Add the following lines to your repository configuration:
```xml
<initialPackage nsURI="http:///www.eclipse.org/m2m/qvt/operational/trace.ecore"/>
```

### JDBC Exception on Startup
After adding the initialPackage statements, the CDO server might crash at the second start. As a workaround, you can simply remove the configuration statements again. Nevertheless, we issues a [bug report](https://bugs.eclipse.org/bugs/show_bug.cgi?id=497148) to address this issue in the CDO implementation itself.
