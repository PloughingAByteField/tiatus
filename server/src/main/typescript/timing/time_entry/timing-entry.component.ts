import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { EntriesService } from '../../services/entries.service';
import { ClubsService } from '../../services/clubs.service';
import { TimesService } from '../../services/times.service';
import { RacesService } from '../../services/races.service';

import { Club } from '../../models/club.model';
import { Entry } from '../../models/entry.model';
import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';
import { PositionTime } from '../../models/postion-time.model';

import { TimingPositionService } from '../timing-position.service';

@Component({
    selector: 'timing-entry',
    styleUrls: [ './timing-entry.component.css' ],
    templateUrl: './timing-entry.component.html',
    providers: [ TimesService ]
})
export class TimingEntryComponent implements OnInit {
    public raceEntries: Entry[];
    public page: number = 1;
    public itemsPerPage: number = 10;
    public filteredEntries: Entry[];
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public reverseTimeSort = false;
    public reverseSyncedSort = false;

    private position: Position;
    private entries: Entry[];
    private times: PositionTime[];
    private raceId: number = 0;
    private races: Race[];
    private race: Race;

    constructor(
        private entriesService: EntriesService,
        private clubsService: ClubsService,
        private route: ActivatedRoute,
        private timingPositionService: TimingPositionService,
        private timesService: TimesService,
        private racesService: RacesService
    ) {}

    public ngOnInit() {
        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.race = this.racesService.getRaceForId(this.raceId);
            }
            this.getTimesForRace(this.raceId);
        });

        this.timingPositionService.getPosition.subscribe((position: Position) => {
            if (position) {
                this.position = position;
                this.getTimesForRace(this.raceId);
            }
        });

        this.entriesService.getEntries().subscribe((data: Entry[]) => {
            this.entries = data;
            this.filteredEntries = this.filterRace(this.raceId);
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            if (this.races) {
                this.race = this.racesService.getRaceForId(this.raceId);
            }
            this.getTimesForRace(this.raceId);
            this.filteredEntries = this.filterRace(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
        });

    }

    public getClubNames(clubs): string {
        return clubs.map((club) => club.clubName).join(' / ');
    }

    public enterTime(value: string) {
        console.log(value);
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
        this.filteredEntries = this.filterEntries();
    }

    public sortTime(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseTimeSort = !this.reverseTimeSort;
    }

    public sortSynced(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseSyncedSort = !this.reverseSyncedSort;
    }

    private getTimesForRace(raceId): void {
        if (this.position && this.race) {
            this.timesService.getTimesForPositionInRace(this.position, this.race)
            .subscribe((data: PositionTime[]) => {
                this.times = data;
                console.log(this.times);
            });
        }
    }

    private filterEntries(): Entry[] {
        let filteredEntries: Entry[] = this.filterRace(this.raceId);
        if (this.numberFilter) {
            filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
        }
        if (this.clubFilter) {
            filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
        }
        if (this.eventFilter) {
            filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
        }

        return filteredEntries;
    }

    private filterNumbers(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry) => entry.number.toString().includes(value));
    }

    private filterClubs(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry)  => {
             if (entry.clubs.find((club: Club) => club.clubName.includes(value))) {
                return entry;
             }
        });
    }

    private filterEvents(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry)  => entry.event.name.includes(value));
    }

    private filterRace(raceId: number): Entry[] {
        if (this.entries) {
            return this.entries.filter((entry: Entry) => entry.race.id === raceId);
        }
    }
}
