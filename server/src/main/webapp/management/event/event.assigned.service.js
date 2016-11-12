(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventAssignedService', EventAssignedService);

    // this an intermediary caching service shared across the controllers
    function EventAssignedService($log, AssignedEvent, $q, $filter) {
        var assignedEvents = [];
        var eventChangeListeners = [];
        var assignedEventsPromise = undefined;

        var service = {
            getAssignedEvents: getAssignedEvents,
            unassignEvent: unassignEvent,
            assignEvent: assignEvent,
            reassignEvent: reassignEvent,
            getAssignedEventsForRace: getAssignedEventsForRace,
            addEventChangeListener: addEventChangeListener
        };

        return service;

        function getAssignedEvents() {
            if (!assignedEventsPromise) {

                var deferred = $q.defer();
                // get assigned events and split into array of races each with an array of events
                AssignedEvent.query().$promise.then(function (data) {
                    for (var i = 0; i < data.length; i++) {
                        var assignedRaceEvent = data[i];
                        var raceElement = getRaceElement(assignedRaceEvent.race.id, assignedEvents);
                        if (raceElement) {
                            raceElement.events.push({id: assignedRaceEvent.id, event: assignedRaceEvent.event, raceEventOrder: assignedRaceEvent.raceEventOrder});
                        } else {
                            var events = [];
                            events.push({id: assignedRaceEvent.id, event: assignedRaceEvent.event, raceEventOrder: assignedRaceEvent.raceEventOrder});
                            var raceEvent = {race: assignedRaceEvent.race, events: events };
                            assignedEvents.push(raceEvent);
                        }
                    }
                    orderEvents(assignedEvents);

                    deferred.resolve(assignedEvents);

                }, function(error) {
                    deferred.reject(error);
                    $log.warn("Failed to get assigned events", error);
                });
                assignedEventsPromise = deferred.promise;
            }

            return $q.when(assignedEventsPromise);
        }

        function addEventChangeListener(listener) {
            eventChangeListeners.push(listener);
        }

        function notifyEventChangeListener() {
            for (var i = 0; i < eventChangeListeners.length; i++) {
                eventChangeListeners[i]();
            }
        }

        function assignEvent(item, raceId, index) {
            var deferred = $q.defer();
            var events = getAssignedEventsForRace(raceId);
            var updatedEvents = [];
            var orderAtDrop = -1;
            if (index === events.length) {
                if (events.length === 0) {
                    orderAtDrop = 1;
                } else {
                    orderAtDrop = events[index - 1].raceEventOrder + 1;
                }
            } else {
                orderAtDrop = events[index].raceEventOrder;
                updatedEvents = reorderRaceOrder(events, index, events.length, 1);
            }
            var raceAssigned = getRaceAssigned(raceId);
            var race = raceAssigned.race;
            // add event to race_event
            // need to do updates first or get db constraint
            if (updatedEvents.length > 0) {
                // update race_events that have had race_order
                AssignedEvent.update(updatedEvents).$promise.then(function () {
                    updateEvents(updatedEvents, events);
                    saveAssignedEvent(deferred, events, item, index, orderAtDrop, race);
                    events = $filter('orderBy')(events, "raceEventOrder");

                }, function(error) {
                    deferred.reject(error);
                    $log.warn("Failed to update events", error);
                });
            } else {
                saveAssignedEvent(deferred, events, item, index, orderAtDrop, race);
                events = $filter('orderBy')(events, "raceEventOrder");
            }
            notifyEventChangeListener();

            return $q.when(deferred.promise);
        };

        function saveAssignedEvent(deferred, events, item, index, orderAtDrop, race) {
            AssignedEvent.save({race: {id: race.id}, event: {id: item.id}, raceEventOrder: orderAtDrop}).$promise.then(function (data) {
                events.splice(index, 0, {id: data.id, event: item, raceEventOrder: orderAtDrop});
                deferred.resolve(data);
            }, function(error) {
                $log.warn("Failed to add event", error);
                deferred.reject(error);
            });
        };

        function reassignEvent(item, raceId, dropIndex) {
            var deferred = $q.defer();
            var raceAssigned = getRaceAssigned(raceId);
            var events = raceAssigned.events;
            var orderAtDrop = - 1;
            var updatedEvents = [];
            var index = dropIndex;
            if (index === events.length) {
                index = index - 1;
            }
            orderAtDrop = events[index].raceEventOrder;
            // is drop point ahead or behind the item
            var indexOfItem = getEventIndex(events, item);
            if (index < indexOfItem) {
                $log.debug("drop before");
                updatedEvents = reorderRaceOrder(events, index, indexOfItem, 1);
                updatedEvents.push({ event: { id: item.event.id }, raceEventOrder: orderAtDrop });

            } else if (index > indexOfItem) {
                $log.debug("drop after ");
                updatedEvents = reorderRaceOrder(events, indexOfItem + 1, index + 1, -1);
                updatedEvents.push({ event: { id: item.event.id }, raceEventOrder: orderAtDrop });

            } else {
                $log.debug("drop on self");
            }

            if (updatedEvents.length > 0) {
                // update race_events that have had race_order
                AssignedEvent.update(updatedEvents).$promise.then(function () {
                    updateEvents(updatedEvents, events);
                    events[indexOfItem].raceEventOrder = orderAtDrop;
                    events = $filter('orderBy')(events, "raceEventOrder");
                    raceAssigned.events = events;
                    deferred.resolve();

                }, function(error) {
                    $log.warn("Failed to update events", error);
                    deferred.reject(error);
                });

                notifyEventChangeListener();
            } else {
                deferred.resolve();
            }

            return $q.when(deferred.promise);
        };

        function updateEvents(updatedEvents, events) {
            for (var i = 0; i < updatedEvents.length; i++) {
                var updated = updatedEvents[i];
                var eventIndex = getEventIndex(events, updated);
                var event = events[eventIndex];
                event.raceEventOrder = updated.raceEventOrder;
            }
        };

        function unassignEvent(item, raceId) {
            var deferred = $q.defer();
            var events = getAssignedEventsForRace(raceId);
            var index = getEventIndex(events, item);

            var updatedEvents = reorderRaceOrder(events, index + 1, events.length, -1);

            // delete event from race_event
            AssignedEvent.remove({ id: item.id }).$promise.then(function (data) {

                if (updatedEvents.length > 0) {
                    // update race_events that have had race_order
                    AssignedEvent.update(updatedEvents).$promise.then(function () {
                         updateEvents(updatedEvents, events);
                         $log.debug("Removing event " + item.event.name);
                         events.splice(index, 1);
                         deferred.resolve(data);

                    }, function(error) {
                        $log.warn("Failed to update events", error);
                        deferred.reject(error);
                    });
                } else {
                    events.splice(index, 1);
                    deferred.resolve(data);
                }

                notifyEventChangeListener();

            }, function(error) {
                $log.warn("Failed to remove event", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };

        function getEventIndex(items, e) {
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.event.id === e.event.id) {
                    return i;
                }
            }
        };


        function reorderRaceOrder(items, start, end, value) {
            var updates = [];
            for (var i = start; i < end; i++) {
                var item = items[i];
                updates.push({ event: { id: item.event.id }, raceEventOrder: item.raceEventOrder + value });
            }
            return updates;
        };

        function orderEvents(raceEvents) {
            for (var i = 0; i < raceEvents.length; i++) {
                var raceAssigned = raceEvents[i];
                raceAssigned.events = $filter('orderBy')(raceAssigned.events, "raceEventOrder");
            }
        };

        function getRaceElement(raceId, raceEvents) {
            for (var i = 0; i < raceEvents.length; i++) {
                var raceAssigned = raceEvents[i];
                if (raceAssigned.race.id === raceId) {
                    return raceAssigned;
                }
            }

            return undefined;
        };

        function getRaceAssigned(raceId) {
            for (var i = 0; i < assignedEvents.length; i++) {
                var raceAssigned = assignedEvents[i];
                if (raceAssigned.race.id === raceId) {
                    return raceAssigned;
                }
            }
        };

        function getAssignedEventsForRace(raceId) {
            var raceAssigned = getRaceAssigned(raceId);
            if (raceAssigned) {
                return raceAssigned.events;
            }

            raceAssigned = {race: {id: raceId}, events: []};
            assignedEvents.push(raceAssigned);
            return raceAssigned.events;
        };

    };

})();