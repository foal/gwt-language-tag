![Maven Central](https://img.shields.io/maven-central/v/org.jresearch.locale.languageTag/org.jresearch.locale.languageTag.bom?style=plastic)

# Java + GWT RFC-5646 language tag.

Java (+GWT) implementation of RFC-5646 (language tag) based on Connect2id language tag library. Java implementation of "Tags for Identifying Languages", RFC-5646. Supports normal language tags. Special private language tags beginning with "x" and grandfathered tags beginning with "i" are not supported.

Original library location: https://bitbucket.org/connect2id/nimbus-language-tags

## Setup

### TLDR - Java

add following Maven dependency to your project
```xml
<dependency>
	<groupId>org.jresearch.locale.languageTag</groupId>
	<artifactId>org.jresearch.locale.languageTag</artifactId>
	<version>1.1.1</version>
</dependency>
```

### Java + GWT

add BOM to the project dependency management

```xml
<dependency>
	<groupId>org.jresearch.locale.languageTag</groupId>
	<artifactId>org.jresearch.locale.languageTag.bom</artifactId>
	<version>1.1.1</version>
	<type>pom</type>
	<scope>import</scope>
</dependency>
```
add the following dependencies to the GWT module

```xml
<dependency>
	<groupId>org.jresearch.locale.languageTag</groupId>
	<artifactId>org.jresearch.locale.languageTag.gwt</artifactId>
</dependency>
<dependency>
	<groupId>org.jresearch.locale.languageTag</groupId>
	<artifactId>org.jresearch.locale.languageTag</artifactId>
</dependency>
```
If you have disabled GWT module automanagement add the following to your `gwt.xml` file

```xml
<inherits name="org.jresearch.locale.langtag.module"/>
```

More information about Nimbus Language Tags can be found on the [project page](https://bitbucket.org/connect2id/nimbus-language-tags)

