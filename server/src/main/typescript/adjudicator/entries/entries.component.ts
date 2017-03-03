import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { TranslateService } from 'ng2-translate';

import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Penalty } from '../../penalties/penalty.model';
import { Disqualification } from '../../disqualification/disqualification.model';
import { PositionTime, convertFromTimeStamp, convertToTimeStamp }
    from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';

import { PositionsService } from '../../positions/positions.service';
import { AdjudicatorRacesService } from '../races/races.service';
import { EntryTimesService } from '../../times/entry-times.service';

import { AdjudicatorDisqualificationService } from '../disqualification/disqualification.service';
import { AdjudicatorPenaltiesService } from '../penalties/penalties.service';

@Component({
    selector: 'entries',
    styleUrls: [ './entries.component.css' ],
    templateUrl: './entries.component.html',
    providers: [
        PositionsService,
        EntryTimesService,
        AdjudicatorDisqualificationService,
        AdjudicatorPenaltiesService
    ]
})
export class EntriesComponent implements OnInit {

    public page: number = 1;
    public itemsPerPage: number = 10;
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public race: Race;
    public filteredEntryTimes: EntryTime[];

    private penalties: Penalty[];
    private disqualifications: Disqualification[];
    private raceId: number = 0;
    private races: Race[];

    private entryTimes: EntryTime[];

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private racesService: AdjudicatorRacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService,
        private disqualificationService: AdjudicatorDisqualificationService,
        private penaltiesService: AdjudicatorPenaltiesService
    ) {}

    public ngOnInit() {
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

        this.penaltiesService.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
        });

        this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
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

    public getRace(): Race {
        return this.race;
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        let time: number = this.getTime(entryTime);
        if (time !== 0) {
            let penalites: number = this.getTimeForPenaltes(entryTime);
            return convertFromTimeStamp(time + penalites);
        }
        return null;
    }

    public getRecordedTimeForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        let time: number = this.getTime(entryTime);
        if (time !== 0) {
            return convertFromTimeStamp(time);
        }
        return null;
    }

    public getTotalPenaltyTimesForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }

        let penalites: number = this.getTimeForPenaltes(entryTime);
        if (penalites !== 0) {
            return convertFromTimeStamp(penalites);
        }

        return null;
    }

    public isDisqualified(entry: EntryTime): boolean {
        if (this.disqualifications) {
            let disqualifed: Disqualification = this.disqualifications
                .filter((disqualification: Disqualification) =>
                    disqualification.entry === entry.entry.id).shift();
            if (disqualifed) {
                return true;
            }
        }
        return false;
    }

    public raceIsOpen(): boolean {
        if (this.race) {
            return !this.race.closed;
        }
        return true;
    }

    public closeRace(): void {
        if (this.race) {
            this.race.closed = true;
            this.racesService.updateRace(this.race);
        }
    }

    public reopenRace(): void {
        if (this.race) {
            this.race.closed = false;
            this.racesService.updateRace(this.race);
        }
    }

    private getTimeForPenaltes(entryTime: EntryTime): number {
        let penalites: Penalty[] = this.penaltiesService.getPenaltiesForEntry(entryTime.entry);
        if (penalites.length > 0) {
            let total: number = 0;
            penalites.map((penalty: Penalty) => total += penalty.time);
            return total;
        }
        return 0;
    }

    private getTime(entryTime: EntryTime): number {
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
                return eventFinishPoint.time - actualStartPoint.time;
            }
        }

        return 0;
    }
    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.getTimesForRace(this.race);
        }
    }

    private getTimesForRace(race: Race): void {
        if (race) {
            this.entryTimesService.getEntriesForRace(this.race)
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
