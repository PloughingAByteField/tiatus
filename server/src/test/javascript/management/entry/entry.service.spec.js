describe("src.test.javascript.management.entry.service.js", function() {

    beforeEach(module('managementApp'));

    describe('entryService', function() {
        var Entry, entryService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Entry_, _entryService_, $q) {
            Entry = _Entry_;
            entryService = _entryService_;
            deferredUpdate = $q.defer();
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(Entry, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(Entry, 'update').and.returnValue({$promise: deferredUpdate.promise});
            spyOn(Entry, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(Entry, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();
        }));

        function getEntries() {
            var entries;
            entryService.getEntries().then(function(data) {
                entries = data;
            });
            expect(Entry.query).toHaveBeenCalled();
            deferredQuery.resolve([
              {id: 1, clubs: [{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}], event: {name: 'Event 1', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false},
              {id: 2, clubs: [{id: 2, clubName: 'Club 3'}], event: {name: 'Event 2', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false}
            ]);
            scope.$apply();
            return entries;
        };

        it('should get entries', function() {
           var entries = getEntries();
           expect(entries.length).toBe(2);
        });

        it('should attempt to get entries but fail', function() {
            entryService.getEntries();
            deferredQuery.reject();
            scope.$apply();
            expect(Entry.query).toHaveBeenCalled();
        });

        it('should add entry', function() {
           var entries = getEntries();
           expect(entries.length).toBe(2);
           var entry = {event: 'Event 1'};
           entryService.addEntry(entry);
           deferredSave.resolve({id: 3});
           scope.$apply();

           expect(Entry.save).toHaveBeenCalled();

           entries = getEntries();
           expect(entries.length).toBe(3);
        });

        it('should attempt to add entry but fail', function() {
           var entries = getEntries();
           expect(entries.length).toBe(2);
           var entry = {event: 'Event 1'};
           entryService.addEntry(entry);
           deferredSave.reject();
           scope.$apply();

           expect(Entry.save).toHaveBeenCalled();

           entries = getEntries();
           expect(entries.length).toBe(2);
        });

        it('should remove entry', function() {
          var entries = getEntries();
          expect(entries.length).toBe(2);
          var entry = {event: 'Event 1'};
          entryService.removeEntry(entry);
          deferredRemove.resolve();
          scope.$apply();

          expect(Entry.remove).toHaveBeenCalled();

          entries = getEntries();
          expect(entries.length).toBe(1);
        });

        it('should attempt to remove entry but fail', function() {
          var entries = getEntries();
          expect(entries.length).toBe(2);
          var entry = {event: 'Event 1'};
          entryService.removeEntry(entry);
          deferredRemove.reject();
          scope.$apply();

          expect(Entry.remove).toHaveBeenCalled();

          entries = getEntries();
          expect(entries.length).toBe(2);
        });

        it('should update entry', function() {
          var entries = getEntries();
          expect(entries.length).toBe(2);
          var entry = {event: 'Event 1'};
          entryService.updateEntry(entry);
          deferredUpdate.resolve();
          scope.$apply();

          expect(Entry.update).toHaveBeenCalled();

          entries = getEntries();
          expect(entries.length).toBe(2);
        });

        it('should attempt to remove entry but fail', function() {
          var entries = getEntries();
          expect(entries.length).toBe(2);
          var entry = {event: 'Event 1'};
          entryService.updateEntry(entry);
          deferredUpdate.reject();
          scope.$apply();

          expect(Entry.update).toHaveBeenCalled();

          entries = getEntries();
          expect(entries.length).toBe(2);
        });

        it('should trigger notifyEntryChangeListener', function() {
          var triggered = false;
          entryService.addEntryChangeListener(function () {
            triggered = true;
          });
          var entries = getEntries();
          expect(entries.length).toBe(2);
          var entry = {event: 'Event 1'};
          entryService.removeEntry(entry);
          deferredRemove.resolve();
          scope.$apply();

          expect(Entry.remove).toHaveBeenCalled();

          entries = getEntries();
          expect(entries.length).toBe(1);
          expect(triggered).toBe(true);
        });

        it('should call getActiveEntry', function() {
          var entry = entryService.getActiveEntry();
          expect(entry.fixedNumber).toBe(false);
          expect(entry.timeOnly).toBe(false);
        });

        it('should call setActiveEntry', function() {
          entryService.setActiveEntry({id: 1, fixedNumber: true, timeOnly: true});
          var entry = entryService.getActiveEntry();
          expect(entry.fixedNumber).toBe(true);
          expect(entry.timeOnly).toBe(true);
          expect(entry.id).toBe(1);
        });
    });
});