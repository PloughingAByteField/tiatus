describe('src.test.e2e.management.club.club_spec.js', function() {
    it('should be to get to page', function() {
        browser.get('/management/management.html#/club');
        return browser.driver.wait(function() {
            return browser.driver.getCurrentUrl().then(function(url) {
                return /club/.test(url);
            });
        }, 5000);
    });

    it('should have a title', function() {
        expect(browser.getTitle()).toEqual('Clubs');
    });

    it('should have empty table', function() {
        var table = element(by.id('clubs'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should create club', function() {
        var table = element(by.id('clubs'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.club.clubName')).sendKeys('Club 1');
        element(by.id('createClub')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
        var club = element(by.repeater('club in ctrl.clubs').row(0));
        expect(club.element(by.model('club.clubName')).getAttribute('value')).toEqual('Club 1');
    });

    it('should remove club', function() {
        var table = element(by.id('clubs'));
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.repeater('club in ctrl.clubs').row(0)).element(by.css('.delete-club')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(2);
    });

    it('should update club', function() {
        var table = element(by.id('clubs'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(2);

        element(by.model('ctrl.club.clubName')).sendKeys('Club 1');
        element(by.id('createClub')).click();

        rows = table.$$('tr');
        expect(rows.count()).toBe(3);
        var club = element(by.repeater('club in ctrl.clubs').row(0));
        var clubTxt = club.element(by.model('club.clubName'));
        expect(clubTxt.getAttribute('value')).toEqual('Club 1');
        clubTxt.clear();
        clubTxt.sendKeys('Club 2');
        club.element(by.css('.update-club')).click();
        expect(clubTxt.getAttribute('value')).toEqual('Club 2');
    });

    it('should trigger name check directive', function() {
        var table = element(by.id('clubs'));
        expect(table.isPresent());
        var rows = table.$$('tr');
        expect(rows.count()).toBe(3);

        element(by.model('ctrl.club.clubName')).sendKeys('Club 2');

        var errors = $('.errors');
        expect(errors.isPresent()).toBe(true);

        expect(errors.$('[ng-message=required]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=clubNameExists]').isPresent()).toBe(true);
        expect(errors.$('[ng-message=minlength]').isPresent()).toBe(false);
        expect(errors.$('[ng-message=maxlength]').isPresent()).toBe(false);

        element(by.model('ctrl.club.clubName')).clear();
    });

});