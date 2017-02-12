import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { TimesService } from '../../services/times.service';
import { RacesService } from '../../services/races.service';

import { Club } from '../../models/club.model';
import { Entry } from '../../models/entry.model';
import { Position } from '../../models/position.model';
import { Race } from '../../models/race.model';
import { PositionTime } from '../../models/postion-time.model';
import { EntryTime } from '../model/entry-time.model';

import { TimingPositionService } from '../services/timing-position.service';
import { EntryTimesService } from '../services/entry-times.service';

@Component({
    selector: 'timing-entry',
    styleUrls: [ './timing-entry.component.css' ],
    templateUrl: './timing-entry.component.html',
    providers: [ TimesService, EntryTimesService ]
})
export class TimingEntryComponent implements OnInit {
    public raceEntries: Entry[];
    public page: number = 1;
    public itemsPerPage: number = 10;
    public filteredEntryTimes: EntryTime[];
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public reverseTimeSort = false;
    public reverseSyncedSort = false;

    private position: Position;
    private entryTimes: EntryTime[];
    private times: PositionTime[];
    private raceId: number = 0;
    private races: Race[];
    private race: Race;

    constructor(
        private route: ActivatedRoute,
        private timingPositionService: TimingPositionService,
        private timesService: TimesService,
        private racesService: RacesService,
        private entryTimesService: EntryTimesService
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

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            if (this.races) {
                this.race = this.racesService.getRaceForId(this.raceId);
            }
            this.getTimesForRace(this.raceId);
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
        if (value) {
            let timeStamp: number = this.convertToTimeStamp(value);
            console.log(timeStamp);
        }
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
        this.filteredEntryTimes = this.filterEntries();
    }

    public sortTime(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseTimeSort = !this.reverseTimeSort;
    }

    public sortSynced(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseSyncedSort = !this.reverseSyncedSort;
    }

    public convertFromTimeStamp(timeStamp: string): string {
        if ( !timeStamp) {
            return null;
        }

        let date = new Date(timeStamp);
        let hours = date.getUTCHours();
        // Minutes part from the timestamp
        let minutes = '0' + date.getUTCMinutes();
        // Seconds part from the timestamp
        let seconds = '0' + date.getUTCSeconds();
        let milliSeconds = '000' + date.getUTCMilliseconds();
        let time = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
        time = time + '.' + milliSeconds.substr(-3);
        return time;
    };

    public convertToTimeStamp(timeString: string): number {
        if ( !timeString) {
            return null;
        }

        let date = new Date(0);
        date.setUTCMilliseconds(0);
        let millisecondFields = timeString.split('.');
        if (millisecondFields.length === 2) {
            date.setUTCMilliseconds(+millisecondFields[1]);
        }
        let timeFields = timeString.split(':');
        date.setUTCHours(+timeFields[0]);
        date.setUTCMinutes(+timeFields[1]);
        date.setUTCSeconds(+timeFields[2]);
        return date.getTime();
    };

    private getTimesForRace(raceId): void {
        if (this.position && this.race) {
            this.timesService.getTimesForPositionInRace(this.position, this.race)
                .subscribe((data: PositionTime[]) => {
                this.times = data;
            });

            this.entryTimesService.getEntriesForRace(this.race, this.position)
                .subscribe((data: EntryTime[]) => {
                this.entryTimes = data;
                this.filteredEntryTimes = this.entryTimes;
            });
        }
    }

    private filterEntries(): EntryTime[] {
        let filteredEntries: EntryTime[] = this.entryTimes;
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

    private filterNumbers(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) =>
            entryTime.entry.number.toString().includes(value));
    }

    private filterClubs(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime)  => {
             if (entryTime.entry.clubs.find((club: Club) => club.clubName.includes(value))) {
                return entryTime;
             }
        });
    }

    private filterEvents(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime)  =>
            entryTime.entry.event.name.includes(value));
    }

}
