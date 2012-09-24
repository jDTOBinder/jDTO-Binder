jDTO Binder ::  Scripting Integration
=====================================

This module provides powerful and convenience DTO Mergers based mainly on the
Groovy scripting language, this is a small example:

```java
@SourceNames({"bean1", "bean2"})
public class GroovyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Source(value="myString", merger=GroovyMerger.class, 
            mergerParam="sourceValue == null ? 'is null' : 'is not null'")
    private String singleSource;
    
    @Sources(value = {@Source("myString"), @Source(value = "myString", sourceBean="bean2")},
            merger = MultiGroovyMerger.class, 
            mergerParam = "sourceValues[0] + ' and ' + sourceValues[1]")
    private String multipleSource;

    ...// getters and setters

}

```

Groovy is a powerful way of defining custom mergers.