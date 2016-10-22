(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventAssignedService', EventAssignedService);

    // this an intermediary caching service shared across the controllers
    function EventAssignedService($log, AssignedEvent, $q, $filter) {
        var assignedEvents = undefined;

        var service = {
            getAssignedEvents: getAssignedEvents,
            addAssignedEvent: addAssignedEvent,
            unassignEvent: unassignEvent,
            assignEvent: assignEvent,
            reassignEvent: reassignEvent,
            getAssignedEventsForRace: getAssignedEventsForRace
        };

        return service;

        function getAssignedEvents() {
            if (!assignedEvents) {

                var deferred = $q.defer();
                // get assigned events and split into array of races each with an array of events
                AssignedEvent.query().$promise.then(function (data) {
                    var raceEvents = [];
                    for (var i = 0; i < data.length; i++) {
                        var assignedRaceEvent = data[i];
                        var raceElement = getRaceElement(assignedRaceEvent.race.id, raceEvents);
                        if (raceElement) {
                            raceElement.events.push({ event: assignedRaceEvent.event, raceEventOrder: assignedRaceEvent.raceEventOrder});
                        } else {
                            var events = [];
                            events.push({ event: assignedRaceEvent.event, raceEventOrder: assignedRaceEvent.raceEventOrder});
                            var raceEvent = { race: assignedRaceEvent.race, events: events };
                            raceEvents.push(raceEvent);
                        }
                    }
                    orderEvents(raceEvents);

                    assignedEvents = raceEvents;
                    deferred.resolve(assignedEvents);

                }, function(error) {
                    assignedEvents = error;
                    deferred.reject(error);
                    $log.warn("Failed to get assigned events", error);
                });
                assignedEvents = deferred.promise;
            }

            return $q.when(assignedEvents);
        }

        function addAssignedEvent(event) {
            assignedEvents.push(event);
        };

        function assignEvent(item, raceId, index) {
            var events = getAssignedEventsForRace(raceId);
            var updatedEvents = [];
            var orderAtDrop = - 1;
            if (index === events.length) {
                orderAtDrop = events[index - 1].raceEventOrder + 1;
            } else {
                orderAtDrop = events[index].raceEventOrder;
                updatedEvents = reorderRaceOrder(events, index, events.length, 1);
            }
            var raceAssigned = getRaceAssigned(raceId);
            var race = raceAssigned.race;
            // add event to race_event
            AssignedEvent.save({race: {id: race.id}, event: {id: item.id}, raceEventOrder: orderAtDrop}).$promise.then(function (data) {
            }, function(error) {
                $log.warn("Failed to add event", error);
            });

            if (updatedEvents.length > 0) {
                // update race_events that have had race_order
                AssignedEvent.update(updatedEvents).$promise.then(function (data) {
                }, function(error) {
                    $log.warn("Failed to update events", error);
                });
            }
            events.splice(index, 0, {event: item, raceEventOrder: orderAtDrop});
        };

        function reassignEvent(item, raceId, dropIndex) {
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
            console.log("indexOfItem " + indexOfItem);
            if (index < indexOfItem) {
                $log.debug("drop before");
                updatedEvents = reorderRaceOrder(events, index, indexOfItem, 1);
                events[indexOfItem].raceEventOrder = orderAtDrop;
                updatedEvents.push({ event: { id: item.event.id }, raceEventOrder: orderAtDrop });
                events = $filter('orderBy')(events, "raceEventOrder");

            } else if (index > indexOfItem) {
                $log.debug("drop after ");
                $log.debug(events[index].event);
                updatedEvents = reorderRaceOrder(events, indexOfItem, index + 1, -1);
                if (dropIndex !== index) {
                    events[indexOfItem].raceEventOrder = orderAtDrop;
                } else {
                    events[indexOfItem].raceEventOrder = orderAtDrop;
                }
                updatedEvents.push({ event: { id: item.event.id }, raceEventOrder: orderAtDrop });
                events = $filter('orderBy')(events, "raceEventOrder");

            } else {
                $log.debug("drop on self");
            }

            if (updatedEvents.length > 0) {
                // update race_events that have had race_order
                AssignedEvent.update(updatedEvents).$promise.then(function (data) {
                }, function(error) {
                    $log.warn("Failed to update events", error);
                });
            }

            raceAssigned.events = events;
        };

        function unassignEvent(item, raceId) {
            var events = getAssignedEventsForRace(raceId);
            var index = getEventIndex(events, item);

            var updatedEvents = reorderRaceOrder(events, index + 1, events.length, -1);

            // delete event from race_event
            AssignedEvent.remove({ id: events[index].event.id }).$promise.then(function (data) {
            }, function(error) {
                $log.warn("Failed to remove event", error);
            });
            // update race_events that have had race_order
            AssignedEvent.update(updatedEvents).$promise.then(function (data) {
            }, function(error) {
                $log.warn("Failed to update events", error);
            });

            $log.debug("Removing event " + item.event.name);
            events.splice(index, 1);
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
                item.raceEventOrder = item.raceEventOrder + value;
                updates.push({ event: { id: item.event.id }, raceEventOrder: item.raceEventOrder });
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

            return [];
        };

    };

})();