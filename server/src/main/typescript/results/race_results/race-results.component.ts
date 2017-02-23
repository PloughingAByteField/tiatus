import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';

import { PositionsService } from '../../services/positions.service';
import { RacesService } from '../../services/races.service';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ PositionsService ]
})
export class RaceResultsComponent implements OnInit {

    public positions: Position[];
    public displayablePositions: Position[];
    public reverseNumberSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';

    private raceId: number = 0;
    private races: Race[];
    private race: Race;

    constructor(
        private route: ActivatedRoute,
        private racesService: RacesService,
        private positionsService: PositionsService
    ) {}

    public ngOnInit() {
        console.log('hello from race results');
        this.positionsService.getPositions()
            .subscribe((positions: Position[]) => {
                this.positions = positions;
                this.displayablePositions = positions.slice();
                this.displayablePositions.shift();
        });

        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.setRaceForRaceId(this.raceId);
            }
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.setRaceForRaceId(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
        });
    }

    public onKey(value: string, field: string) {
        console.log('onkey');
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    private getTimesForRace(race: Race): void {
        if (race) {
            console.log('Get times for race ' + race.id);
        }
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.getTimesForRace(this.race);
        }
    }
}
