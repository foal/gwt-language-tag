![Maven Central](https://img.shields.io/maven-central/v/org.jresearch.gwt.languageTag/org.jresearch.gwt.languageTag?style=plastic)

# GWT RFC-5646 language tag.

GWT implementation of RFC-5646 (language tag) based on Connect2id language tag library. Java implementation of "Tags for Identifying Languages", RFC-5646. Supports normal language tags. Special private language tags beginning with "x" and grandfathered tags beginning with "i" are not supported.
Original library location: https://bitbucket.org/connect2id/nimbus-language-tags

## Setup

### Maven dependency

```xml
<dependency>
	<groupId>org.jresearch.gwt.languageTag</groupId>
	<artifactId>org.jresearch.gwt.languageTag</artifactId>
	<version>1.0.0</version>
</dependency>
```

### GWT module inheritance
```xml
<inherits name="org.jresearch.gwt.locale.langtag.module"/>
```

More information about Nimbus Language Tags can be found on the [project page](https://bitbucket.org/connect2id/nimbus-language-tags)

