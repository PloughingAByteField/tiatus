import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { TranslateService } from 'ng2-translate';

import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';
import { Club } from '../../models/club.model';
import { PositionTime, convertFromTimeStamp } from '../../models/postion-time.model';
import { EntryTime } from '../../models/entry-time.model';

import { PositionsService } from '../../services/positions.service';
import { RacesService } from '../../services/races.service';

@Component({
    selector: 'entries',
    styleUrls: [ './entries.component.css' ],
    templateUrl: './entries.component.html',
    providers: [ PositionsService ]
})
export class EntriesComponent implements OnInit {

    public page: number = 1;
    public itemsPerPage: number = 10;
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';

    public filteredEntryTimes: EntryTime[];

    private raceId: number = 0;
    private races: Race[];
    private race: Race;

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private racesService: RacesService,
        private positionsService: PositionsService
    ) {}

    public ngOnInit() {
        console.log('hello from entries');
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
        if (field === 'number') {
            this.numberFilter = value;
        }
        if (field === 'club') {
            this.clubFilter = value;
        }
        if (field === 'event') {
            this.eventFilter = value;
        }
        // this.filteredEntryTimes = this.filterEntries();
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
            // if (this.checkIfDisqualified(entryTime)) {
            //     return null;
            // }
            return null;
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            // this.getTimesForRace(this.race);
        }
    }
}
