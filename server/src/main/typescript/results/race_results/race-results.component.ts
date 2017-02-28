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
import { EntryTimesService } from '../../services/entry-times.service';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ PositionsService, EntryTimesService ]
})
export class RaceResultsComponent implements OnInit {
    public page: number = 1;
    public itemsPerPage: number = 10;
    public positions: Position[];
    public displayablePositions: Position[];
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public filteredEntryTimes: EntryTime[];

    private raceId: number = 0;
    private races: Race[];
    private race: Race;
    private entryTimes: EntryTime[];
    private disqualifiedText: string;
    private penaltyText: string;

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private racesService: RacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService
    ) {}

    public ngOnInit() {
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
        this.translate.get('DISQUALIFIED').subscribe((res: string) => {
            this.disqualifiedText = res;
        });
        this.translate.get('PENALTY').subscribe((res: string) => {
            this.penaltyText = res;
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
        this.filteredEntryTimes = this.filterEntries();
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public getTimeForEntry(entryTime: EntryTime, position: Position): string {
        if (this.checkIfDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event.startingPosition)
                .shift();
            let time: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position === position.id)
                .shift();
            if (time && actualStartPoint) {
                return convertFromTimeStamp(time.time - actualStartPoint.time);
            }
        }
        return null;
    }

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
        if (this.checkIfDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event.startingPosition)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                     === entryTime.entry.event.finishingPosition)
                .shift();
            if (eventFinishPoint && actualStartPoint) {
                // apply penalties
                let penalties: number = this.getPenaltiesForEntry(entryTime);
                return convertFromTimeStamp(
                    eventFinishPoint.time - actualStartPoint.time + penalties);
            }
        }
        return null;
    }

    public getCommentsForEntry(entryTime: EntryTime): string {
        if (this.checkIfDisqualified(entryTime)) {
           return this.disqualifiedText;
        }
        if (this.getPenaltiesForEntry(entryTime) > 0) {
           return this.penaltyText;
        }
        return null;
    }

    private getPenaltiesForEntry(entryTime: EntryTime): number {
        if (entryTime.entry.number === 2) {
            return 20000;
        }
        return 0;
    }

    private checkIfDisqualified(entryTime: EntryTime): boolean {
        if (entryTime.entry.number === 3) {
            return true;
        }
        return false;
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
