(function() {
  'use strict'; // NOSONAR

  angular.module('DrawController').service('drawService', DrawService);

  function DrawService($log, $q, $filter, Entry) {
    var entriesPromise = undefined;
    var entries = [];
    var entryChangeListeners = [];

    var service = {
      getEntries: getEntries,
      setEntryPosition: setEntryPosition,
      addEntryChangeListener: addEntryChangeListener
    };

    return service;

    function addEntryChangeListener(listener) {
      entryChangeListeners.push(listener);
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

    function setEntryPosition(item, position) {
      // is number fixed
      console.log(position);
      console.log(item);
      var entriesForRace = $filter('filter')(entries, function (entry) {
        return entry.race.id == item.race.id;
      });

      var deferred = $q.defer();
      var updatedEntries = [];

      var index = position;
      var itemIndex = getIndexForItem(item, entriesForRace);
      if (index > itemIndex) {
        $log.debug("drop after");
        var itemAtDrop = entriesForRace[index - 1];
        var orderAtDrop = angular.copy(itemAtDrop.raceOrder);
        var numberAtDrop = angular.copy(itemAtDrop.number);
        updatedEntries = reorder(entriesForRace, itemIndex + 1, index, -1);
        entriesForRace[itemIndex].raceOrder = orderAtDrop;
        entriesForRace[itemIndex].number = numberAtDrop;
        updatedEntries.push(entriesForRace[itemIndex]);

      } else if (index < itemIndex) {
        $log.debug("drop before");
        var itemAtDrop = entriesForRace[index];
        var orderAtDrop = angular.copy(itemAtDrop.raceOrder);
        var numberAtDrop = angular.copy(itemAtDrop.number);
        updatedEntries = reorder(entriesForRace, index, itemIndex, 1);
        entriesForRace[itemIndex].raceOrder = orderAtDrop;
        entriesForRace[itemIndex].number = numberAtDrop;
        updatedEntries.push(entriesForRace[itemIndex]);

      } else {
        $log.debug("drop on self");
      }

      // notifyEntryChangeListener();

      if (updatedEntries.length > 0) {
        Entry.updateEntries(updatedEntries).$promise.then(function(data) {
          deferred.resolve();

          // notifyEntryChangeListener();

        }, function(error) {
          $log.warn("Failed to update entries", error);
          deferred.reject(error);

        });
        return $q.when(deferred.promise);
        deferred.resolve();

      } else {
        deferred.resolve();
      }

      return $q.when(deferred.promise);
    }

    function getIndexForItem(item, items) {
      for (var i = 0; i < items.length; i++) {
        var it = items[i];
        if (it.raceOrder === item.raceOrder) {
          return i;
        }
      }
      return items.length - 1;
    }

    function reorder(items, start, end, value) {
      var updates = [];
      for (var i = start; i < end; i++) {
        var item = items[i];
        item.raceOrder = item.raceOrder + value;
        if (!item.fixedNumber) {
          item.number = item.number + value;
        }
        updates.push(item);
      }
      return updates;
    }

  }
})();