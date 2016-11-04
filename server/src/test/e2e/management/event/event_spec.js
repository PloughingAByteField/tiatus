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

    it('should be able to add unassiged event and move to assigned', function() {

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

        // all this is done as selenium webdirver does not handle html5 drag and drop
        unassignedEvent.getAttribute('id').then(function(txt){
            var id = txt;
            var draggable = browser.findElement(by.id(id));
            var droppable = browser.findElement(by.id('assignedEvents'));
            browser.driver.executeScript(dragAndDrop, draggable, droppable);
        });
    });

    it('should be able to add a second unassiged event and move to assigned', function() {

        var assignedEvents = element(by.id('assignedEvents'));
        var assignedRows = assignedEvents.$$('.assigned-event');
        expect(assignedRows.count()).toBe(1);
        var unassignedEvents = element(by.id('unassignedEvents'));
        var unassignedRows = unassignedEvents.$$('.unassigned-event');

        // expect(unassignedRows.count()).toBe(0);
        element(by.model('ctrl.event.name')).sendKeys('Event 2');
        element(by.id('createEvent')).click();

        unassignedRows = unassignedEvents.$$('.unassigned-event');
        expect(unassignedRows.count()).toBe(1);
        assignedRows = assignedEvents.$$('.assigned-event');
        expect(assignedRows.count()).toBe(1);

        var unassigned = element.all(by.repeater('event in ctrlUnassigned.unassigned'));
        expect(unassigned.count()).toBe(1);
        var unassignedEvent = element.all(by.repeater('event in ctrlUnassigned.unassigned')).get(0);
        var assignedEvent = element.all(by.repeater('raceEvent in ctrl.assignedToRace')).get(0);


        // all this is done as selenium webdirver does not handle html5 drag and drop
        unassignedEvent.getAttribute('id').then(function(txt){
            var id = txt;
            assignedEvent.getAttribute('id').then(function(assignedId){
                var draggable = browser.findElement(by.id(id));
                var droppable = browser.findElement(by.id(assignedId));
                browser.driver.executeScript(dragAndDrop, draggable, droppable);
            });
        });

    });
});