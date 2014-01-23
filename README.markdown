jDTO Binder - Java DTO/OOM Framework
====================================

Status of the builds: [![Build Status](https://travis-ci.org/jDTOBinder/jDTO-Binder.png?branch=master)](https://travis-ci.org/jDTOBinder/jDTO-Binder)

To get started, please visit http://www.jdto.org and get more information and links to documentation.

To learn how to use the framework please take a look at the book [https://github.com/jDTOBinder/jDTO-Binder/raw/master/book/jdto.pdf], further documentation and examples will be developed and published on the website news feed and Google+ page.

## Building the artifacts from source.

Even though jDTO Binder is publicly available on the central maven repository, if you want 
to try out new features you can clone the repository and compile the artifacts yourself,
in order to do that simply run on a terminal:

    $ mvn clean install

And after that, the jar files will be available in the target directory.

## Quick Start

To get started with jDTO Binder, you first need to know of the two main components:

* The DTO Binder: This object is in charge of the conversion between so called business objects and DTOs
* The DTO Configuration: The configuration is made for each DTO you wish to use (no configuration is needed for default behavior), this configuration can be made either by java annotations or XML file.

To get the DTOBinder instance:

```java
//init the binder as a singleton.
DTOBinder binder = DTOBinderFactory.getBinder();
```

With the DTOBinder instance you can do shallow copies of any object:

```java    
//shallow copy directly an object.
MyObject obj = binder.bindFromBusinessObject(MyObject.class, original);
```

This is because all the fields are bound by convention matching with the field name.

You can bind a collection of instances using the DTOBinder:

```java
Set objects;//get them from somewhere
Set dtos = binder.bindFromBusinessObjectCollection(MyDTOObject.class, objects);
```

You can configure your DTO Objects using java annotations:

```java
public class FormatDTO {

    @Source(value="aDouble", merger=StringFormatMerger.class, mergerParam="$ %.2f")
    private String price;

    @Sources(value={@Source("aDouble"), @Source("anInt")}, 
            merger=StringFormatMerger.class, mergerParam="%.2f %08d")
    private String compound;

...//getters && setters.
}
```

Many things can be achieved by configuration:

* Set the field from which the value will be read.
* Configure custom value conversion.
* Compose one value from multiple source fields.
* Tell the framework to ignore a field (using @DTOTransient).
* Make a field also be a DTO which you can use to build a whole new DTO or shallow copy an existing one.

For more information, please refer to the manual.