describe('src.test.e2e.management.race.race_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/race');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /race/.test(url);
            });
        }, 5000);
    });

    it('should have a title', function() {
        expect(browser.getTitle()).toEqual('Races');
    });

    it('should have empty table', function() {
        var table = element(by.id('races'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should have create race', function() {
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

    it('should have remove race', function() {
        var table = element(by.id('races'));
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.repeater('race in ctrl.races').row(0)).element(by.css('button.btn')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });
});