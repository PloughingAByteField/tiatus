import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';
import { EntryTime } from '../models/entry-time.model';

import { PositionsService } from '../../services/positions.service';
import { RacesService } from '../../services/races.service';
import { EntryTimesService } from '../services/entry-times.service';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ PositionsService, EntryTimesService ]
})
export class RaceResultsComponent implements OnInit {

    public positions: Position[];
    public displayablePositions: Position[];
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';

    private raceId: number = 0;
    private races: Race[];
    private race: Race;
    private entryTimes: EntryTime[];
    private filteredEntryTimes: EntryTime[];

    constructor(
        private route: ActivatedRoute,
        private racesService: RacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService
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
            this.entryTimesService.getEntriesForRace(this.race)
                .subscribe((data: EntryTime[]) => {
                    this.entryTimes = data;
                    this.filteredEntryTimes = this.entryTimes;
                    console.log(this.entryTimes);
            });
        }
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.getTimesForRace(this.race);
        }
    }
}
