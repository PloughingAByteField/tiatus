describe("src.test.javascript.management.position.service.js", function() {

    beforeEach(module('managementApp'));

    describe('positionService', function() {
        var Position, positionService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Position_, _positionService_, $q) {
            Position = _Position_;
            positionService = _positionService_;
            deferredUpdate = $q.defer();
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(Position, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(Position, 'update').and.returnValue({$promise: deferredUpdate.promise});
            spyOn(Position, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(Position, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();

        }));

        function getPositions() {
            var positions;
            positionService.getPositions().then(function(data) {
                positions = data;
            });
            expect(Position.query).toHaveBeenCalled();
            deferredQuery.resolve([{id: 1, name: 'Position 1', order: 1}, {id: 2, name: 'Position 2' , order: 2}]);
            scope.$apply();
            return positions;
        };

        describe('position service', function() {
            it('should get positions', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);
            });

            it('should fail to fetch positions', function() {
                var positions;
                expect(positionService.positions).toBeUndefined();
                positionService.getPositions().then(function(data){
                    positions = data;
                });
                deferredQuery.reject();
                scope.$apply();
                expect(Position.query).toHaveBeenCalled();
                expect(positions).toBeUndefined();
            });

            it('should add position', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {name: 'Position 3', order: 3};
                positionService.addPosition(position);
                deferredSave.resolve({id: 3});
                scope.$apply();

                expect(Position.save).toHaveBeenCalled();

                positions = getPositions()
                expect(positions.length).toBe(3);
                expect(positions[2].id).toBe(3);
                expect(positions[2].order).toBe(3);
                expect(positions[2].name).toBe('Position 3');
            });

            it('should attempt to add position but fail', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {name: 'Position 3', order: 3};
                positionService.addPosition(position);
                deferredSave.reject();
                scope.$apply();

                expect(Position.save).toHaveBeenCalled();

                 positions = getPositions();
                 expect(positions.length).toBe(2);
            });

            it('should remove position', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {id: 2, name: 'Position 2' , order: 2};
                positionService.removePosition(position);
                deferredRemove.resolve();
                scope.$apply();

                expect(Position.remove).toHaveBeenCalled();

                positions = getPositions()
                expect(positions.length).toBe(1);
            });

            it('should attempt to remove position but fail', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {id: 2, name: 'Position 2' , order: 2};
                positionService.removePosition(position);
                deferredRemove.reject();
                scope.$apply();

                expect(Position.remove).toHaveBeenCalled();

                positions = getPositions()
                expect(positions.length).toBe(2);
            });

            it('should update position', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {id: 2, name: 'Position 2a', order: 4};
                positionService.updatePosition(position);
                deferredUpdate.resolve();
                scope.$apply();

                expect(Position.update).toHaveBeenCalled();

                positions = getPositions()
                expect(positions.length).toBe(2);
                expect(positions[1].id).toBe(2);
                expect(positions[1].order).toBe(4);
                expect(positions[1].name).toBe('Position 2a');
            });

            it('should attempt to update position but fail', function() {
                var positions = getPositions();
                expect(positions.length).toBe(2);

                var position = {id: 2, name: 'Position 2a', order: 4};
                positionService.updatePosition(position);
                deferredUpdate.reject();
                scope.$apply();

                expect(Position.update).toHaveBeenCalled();

                positions = getPositions()
                expect(positions.length).toBe(2);
                expect(positions[1].id).toBe(2);
                expect(positions[1].order).toBe(2);
                expect(positions[1].name).toBe('Position 2');
            });
        });
    });
});