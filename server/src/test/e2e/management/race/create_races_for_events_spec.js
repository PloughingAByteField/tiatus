describe('src.test.e2e.management.race.create_races_for_events_race_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/race');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /race/.test(url);
            });
        }, 5000);
    });

    it('should have create race 1', function() {
        var table = element(by.id('races'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.race.name')).sendKeys('Race 1');
        element(by.model('ctrl.race.order')).sendKeys('1');
        element(by.id('createRace')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
    });

    it('should have create race 2', function() {
        var table = element(by.id('races'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.model('ctrl.race.name')).sendKeys('Race 2');
        element(by.model('ctrl.race.order')).sendKeys('2');
        element(by.id('createRace')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(4);
    });

    it('should have create race 3', function() {
        var table = element(by.id('races'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(4);

        element(by.model('ctrl.race.name')).sendKeys('Race 3');
        element(by.model('ctrl.race.order')).sendKeys('3');
        element(by.id('createRace')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(5);
    });
});