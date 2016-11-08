// this runs drag and drop simulation code to create the drag and drop events for the browser as selenium webdriver currently does not support dnd
var dragAndDrop = require('html-dnd').code;

describe('src.test.e2e.management.event.event_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/event');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /event/.test(url);
            });
        }, 5000);
    });

    it('should have a title', function() {
        expect(browser.getTitle()).toEqual('Events');
    });

    it('should have assigned be empty', function() {
        var assignedEvents = element(by.id('assignedEvents'));
        var rows = assignedEvents.$$('.assigned-event');
        expect(rows.count()).toBe(0);
    });

    it('should have unassigned be empty', function() {
        var assignedEvents = element(by.id('unassignedEvents'));
        var rows = assignedEvents.$$('.unassigned-event');
        expect(rows.count()).toBe(0);
    });

    it('should be able to add unassigned event and move to assigned', function() {

        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        expect(assignedRows.count()).toBe(0);
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 1');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);
        assignedRows = assignedEvents.$$('.assigned-event');
        expect(assignedRows.count()).toBe(0);

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt){
            var id = txt;
            var draggable = browser.findElement(by.id(id));
            var droppable = browser.findElement(by.id('assignedEvents'));
            browser.driver.executeScript(dragAndDrop, draggable, droppable);
            assignedRows = assignedEvents.$$('.assigned-event');
            expect(assignedRows.count()).toBe(1);
            unassignedRows = unassignedEvents.$$('.unassigned-event');
            expect(unassignedRows.count()).toBe(0);
            var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
            expect(assignedEvent.getText()).toContain('Event: Event 1');
            expect(assignedEvent.getText()).toContain('Race Order: 1');
        });
    });

    it('should be able to add a second unassigned event and move to assigned', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 2');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);
        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt){
            var id = txt;
            assignedEvent.getAttribute('id').then(function(assignedId){
                var draggable = browser.findElement(by.id(id));
                var droppable = browser.findElement(by.id(assignedId));
                browser.driver.executeScript(dragAndDrop, draggable, droppable);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(assignedRowsCount + 1);
                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);

                var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
                expect(assignedEvent.getText()).toContain('Event: Event 1');
                expect(assignedEvent.getText()).toContain('Race Order: 1');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
                expect(assignedEvent.getText()).toContain('Event: Event 2');
                expect(assignedEvent.getText()).toContain('Race Order: 2');
            });
        });

    });

    it('should be able to add a third unassigned event and move to assigned in the middle', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 3');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);
        assignedRows = assignedEvents.$$('.assigned-event');

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);
        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt) {
            var id = txt;
            assignedEvent.getAttribute('id').then(function(assignedId) {
                var draggable = browser.findElement(by.id(id));
                var droppable = browser.findElement(by.id(assignedId));
                browser.driver.executeScript(dragAndDrop, draggable, droppable);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(assignedRowsCount + 1);
                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);

                var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
                expect(assignedEvent.getText()).toContain('Event: Event 1');
                expect(assignedEvent.getText()).toContain('Race Order: 1');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
                expect(assignedEvent.getText()).toContain('Event: Event 3');
                expect(assignedEvent.getText()).toContain('Race Order: 2');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
                expect(assignedEvent.getText()).toContain('Event: Event 2');
                expect(assignedEvent.getText()).toContain('Race Order: 3');
            });
        });
    });

    it('should be able to add a forth unassigned event and move to assigned at the start', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 4');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);
        assignedRows = assignedEvents.$$('.assigned-event');

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);
        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt) {
            var id = txt;
            assignedEvent.getAttribute('id').then(function(assignedId) {
                var draggable = browser.findElement(by.id(id));
                var droppable = browser.findElement(by.id(assignedId));
                browser.driver.executeScript(dragAndDrop, draggable, droppable, 1, 1);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(assignedRowsCount + 1);
                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);

                var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
                expect(assignedEvent.getText()).toContain('Event: Event 4');
                expect(assignedEvent.getText()).toContain('Race Order: 1');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
                expect(assignedEvent.getText()).toContain('Event: Event 1');
                expect(assignedEvent.getText()).toContain('Race Order: 2');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
                expect(assignedEvent.getText()).toContain('Event: Event 3');
                expect(assignedEvent.getText()).toContain('Race Order: 3');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
                expect(assignedEvent.getText()).toContain('Event: Event 2');
                expect(assignedEvent.getText()).toContain('Race Order: 4');
            });
        });
    });

    it('should be able to add a fifth unassigned event and move to assigned one after the start', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 5');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);
        assignedRows = assignedEvents.$$('.assigned-event');

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);
        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt) {
            var id = txt;
            assignedEvent.getAttribute('id').then(function(assignedId) {
                var draggable = browser.findElement(by.id(id));
                var droppable = browser.findElement(by.id(assignedId));
                browser.driver.executeScript(dragAndDrop, draggable, droppable, 1000, 1000);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(assignedRowsCount + 1);
                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);

                var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
                expect(assignedEvent.getText()).toContain('Event: Event 4');
                expect(assignedEvent.getText()).toContain('Race Order: 1');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
                expect(assignedEvent.getText()).toContain('Event: Event 5');
                expect(assignedEvent.getText()).toContain('Race Order: 2');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
                expect(assignedEvent.getText()).toContain('Event: Event 1');
                expect(assignedEvent.getText()).toContain('Race Order: 3');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
                expect(assignedEvent.getText()).toContain('Event: Event 3');
                expect(assignedEvent.getText()).toContain('Race Order: 4');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(4);
                expect(assignedEvent.getText()).toContain('Event: Event 2');
                expect(assignedEvent.getText()).toContain('Race Order: 5');
            });
        });
    });

    it('should drop assigned on self and do nothing', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);

        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        assignedEvent.getAttribute('id').then(function(assignedId) {
            var droppable = browser.findElement(by.id(assignedId));
            var draggable = droppable;
            browser.driver.executeScript(dragAndDrop, draggable, droppable, 1, 1);
            assignedRows = assignedEvents.$$('.assigned-event');
            expect(assignedRows.count()).toBe(assignedRowsCount);
            unassignedRows = unassignedEvents.$$('.unassigned-event');
            expect(unassignedRows.count()).toBe(0);

            var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
            expect(assignedEvent.getText()).toContain('Event: Event 4');
            expect(assignedEvent.getText()).toContain('Race Order: 1');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
            expect(assignedEvent.getText()).toContain('Event: Event 5');
            expect(assignedEvent.getText()).toContain('Race Order: 2');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
            expect(assignedEvent.getText()).toContain('Event: Event 1');
            expect(assignedEvent.getText()).toContain('Race Order: 3');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
            expect(assignedEvent.getText()).toContain('Event: Event 3');
            expect(assignedEvent.getText()).toContain('Race Order: 4');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(4);
            expect(assignedEvent.getText()).toContain('Event: Event 2');
            expect(assignedEvent.getText()).toContain('Race Order: 5');
        });
    });

   it('should reorder assigned when top is dropped on bottom', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);

        var assignedEventTop = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
        var assignedEventBottom = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(4);

        assignedEventTop.getAttribute('id').then(function(assignedIdTop) {
            assignedEventBottom.getAttribute('id').then(function(assignedIdBottom) {
                var draggable = browser.findElement(by.id(assignedIdTop));
                var droppable = browser.findElement(by.id(assignedIdBottom));
                browser.driver.executeScript(dragAndDrop, draggable, droppable);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(assignedRowsCount);
                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);

                var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
                expect(assignedEvent.getText()).toContain('Event: Event 5');
                expect(assignedEvent.getText()).toContain('Race Order: 1');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
                expect(assignedEvent.getText()).toContain('Event: Event 1');
                expect(assignedEvent.getText()).toContain('Race Order: 2');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
                expect(assignedEvent.getText()).toContain('Event: Event 3');
                expect(assignedEvent.getText()).toContain('Race Order: 3');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
                expect(assignedEvent.getText()).toContain('Event: Event 2');
                expect(assignedEvent.getText()).toContain('Race Order: 4');

                assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(4);
                expect(assignedEvent.getText()).toContain('Event: Event 4');
                expect(assignedEvent.getText()).toContain('Race Order: 5');
            });
        });
   });

   it('should drop assigned on unassigned and reorder', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(0);

        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);

        assignedEvent.getAttribute('id').then(function(assignedId) {
            var draggable = browser.findElement(by.id(assignedId));
            var droppable = browser.findElement(by.id('unassignedEvents'));
            browser.driver.executeScript(dragAndDrop, draggable, droppable);

            assignedRows = assignedEvents.$$('.assigned-event');
            expect(assignedRows.count()).toBe(assignedRowsCount - 1);
            unassignedRows = unassignedEvents.$$('.unassigned-event');
            expect(unassignedRows.count()).toBe(1);

            var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
            expect(assignedEvent.getText()).toContain('Event: Event 1');
            expect(assignedEvent.getText()).toContain('Race Order: 1');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
            expect(assignedEvent.getText()).toContain('Event: Event 3');
            expect(assignedEvent.getText()).toContain('Race Order: 2');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
            expect(assignedEvent.getText()).toContain('Event: Event 2');
            expect(assignedEvent.getText()).toContain('Race Order: 3');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
            expect(assignedEvent.getText()).toContain('Event: Event 4');
            expect(assignedEvent.getText()).toContain('Race Order: 4');
        });
   });

   it('should delete unassigned', function() {
        var assignedRowsCount;
        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        assignedRows.count().then(function(count) {
            assignedRowsCount = count;
        });
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        expect(unassignedRows.count()).toBe(1);

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);

        unassignedEvent.getAttribute('id').then(function(txt) {
            var id = txt;
            element(by.id(id)).element(by.buttonText('Delete Event')).click();
            assignedRows = assignedEvents.$$('.assigned-event');
            expect(assignedRows.count()).toBe(assignedRowsCount);
            unassignedRows = unassignedEvents.$$('.unassigned-event');
            expect(unassignedRows.count()).toBe(0);

            var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);
            expect(assignedEvent.getText()).toContain('Event: Event 1');
            expect(assignedEvent.getText()).toContain('Race Order: 1');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(1);
            expect(assignedEvent.getText()).toContain('Event: Event 3');
            expect(assignedEvent.getText()).toContain('Race Order: 2');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(2);
            expect(assignedEvent.getText()).toContain('Event: Event 2');
            expect(assignedEvent.getText()).toContain('Race Order: 3');

            assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(3);
            expect(assignedEvent.getText()).toContain('Event: Event 4');
            expect(assignedEvent.getText()).toContain('Race Order: 4');
        });
   });

});