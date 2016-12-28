(function() {
    'use strict'; // NOSONAR

    angular.module('EntryController').service('entryService', EntryService);

    // this an intermediary caching service shared across the controllers
    function EntryService($log, Entry, $q) {
        var entries = [];
        var entryChangeListeners = [];
        var entriesPromise = undefined;
        var activeEntry = {fixedNumber: false, timeOnly: false};

        var service = {
            getEntries: getEntries,
            getActiveEntry: getActiveEntry,
            setActiveEntry: setActiveEntry,
            addEntry: addEntry,
            removeEntry: removeEntry,
            updateEntry: updateEntry,
            addEntryChangeListener: addEntryChangeListener
        };

        return service;

        function addEntryChangeListener(listener) {
            entryChangeListeners.push(listener);
        }

        function getActiveEntry() {
            return activeEntry;
        }

        function setActiveEntry(entry) {
            activeEntry = entry;
        }

        function notifyEntryChangeListener() {
            for (var i = 0; i < entryChangeListeners.length; i++) {
                entryChangeListeners[i]();
            }
        }

        function getEntries() {
            if (!entriesPromise) {
                var deferred = $q.defer();
                Entry.query().$promise.then(function (data) {
                    entries = data;
                    deferred.resolve(entries);

                }, function(error) {
                    entries = error;
                    deferred.reject(error);
                    $log.warn("Failed to get entries", error);
                });
                entriesPromise = deferred.promise;
            }

            return $q.when(entriesPromise);
        }

        function addEntry(entry) {
            var deferred = $q.defer();
            Entry.save(entry).$promise.then(function(data) {
                entry.id = data.id;
                entries.push(entry);
                deferred.resolve();

                notifyEntryChangeListener();

            }, function(error) {
                $log.warn("Failed to add entry", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        }

        function removeEntry(entry) {
            var deferred = $q.defer();
            Entry.remove(entry).$promise.then(function() {
                // remove from list
                entries.splice(entries.indexOf(entry), 1);
                deferred.resolve();

                notifyEntryChangeListener();

            }, function(error) {
                $log.warn("Failed to remove entry", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        }

        function updateEntry(entry) {
            var deferred = $q.defer();
            Entry.update(entry).$promise.then(function(data) {
                deferred.resolve();

                notifyEntryChangeListener();

            }, function(error) {
                $log.warn("Failed to update entry", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        }

    };
})();        