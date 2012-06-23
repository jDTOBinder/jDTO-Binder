jDTO Binder Version Changelog
==============================

Version 1.2:
------------

### New Features:

* Added value restore logic to reverse binding process.
* Added conversion from date to calendar and calendar to date to value compatibility logic.
* IdentityPropertyValueMerger now can restore values.
* DateFormatMerger now can restore values.
* DecimalFormatMerger now can restore values. 
* MethodCallMerger now can restore values.
* Added extra methods to the SinglePropertyValueMerger interface to unmerge when possible the values.
* Added new merger to calculate the time between two dates. (TimeBetweenDatesMerger)
* Introduced new marker interface to treat all PropertyValueMergers as if they were of only one kind.
* Added new method to obtain property value mergers to the DTOBinder interface.
    public <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass);


### Bugfixes:

* Fixed circular refference problem for non-immutable DTOs by adding local cache for the binding process.
* Now the @DTOCascade annotation can be used within a constructor argument.
* Fixed small issue with the analysis of cascaded dto arguments.
* Full refactor to improve code consistency and cohesion regarding PropertyValueMergers 
* Fixed compilation issue with the DateMerger on some environments.
* Changelog file started with version 1.2.