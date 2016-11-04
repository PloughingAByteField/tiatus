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

    it('should be add event to unassigned', function() {

        var assignedEvents = element(by.css('.draw-container'));
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
        var eventNameElement = unassignedEvent.element(by.css('.event-name'));
        unassignedEvent.getAttribute('id').then(function(txt){
            var id = txt;
            var eventName;
            eventNameElement.getText().then(function(txt){
                var s = txt.split(': ');
                eventName = s[1];
                var data = "ctrlAssigned.dropOnAssigned({}, 0, {'id': " + id + ", 'name': '" + eventName + "'})";
                assignedEvents.evaluate(data);

                browser.get('/management/management.html#/event');
                return browser.driver.wait(function() {
                    return browser.driver.getCurrentUrl().then(function(url) {
                        return /event/.test(url);
                    });
                }, 5000);

                unassignedRows = unassignedEvents.$$('.unassigned-event');
                expect(unassignedRows.count()).toBe(0);
                assignedRows = assignedEvents.$$('.assigned-event');
                expect(assignedRows.count()).toBe(1);
            });
        });

//        browser.actions().dragAndDrop(unassignedEvent, assignedEvents).perform(); -- replace above when html5 drag and drop is supported
    });
});