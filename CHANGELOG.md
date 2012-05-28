jDTO Binder Version Changelog
==============================

Version 1.2:
------------

### New Features:

* Introduced new marker interface to treat all PropertyValueMergers as if they were of only one kind.
* Added new method to obtain property value mergers to the DTOBinder interface.
    public <T extends PropertyValueMerger> T getPropertyValueMerger(Class<T> mergerClass);


### Bugfixes:

* Full refactor to improve code consistency and cohesion regarding PropertyValueMergers 
* Fixed compilation issue with the DateMerger on some environments.
* Changelog file started with version 1.2.