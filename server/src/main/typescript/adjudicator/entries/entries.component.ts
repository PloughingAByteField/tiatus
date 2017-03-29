import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Penalty } from '../../penalties/penalty.model';
import { Disqualification } from '../../disqualification/disqualification.model';
import { PositionTime, convertFromTimeStamp, convertToTimeStamp }
    from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';

import { PositionsService } from '../../positions/positions.service';
import { AdjudicatorRacesService } from '../races/races.service';
import { EntryTimesFullService } from '../times/entry-times.service';

import { AdjudicatorDisqualificationService } from '../disqualification/disqualification.service';
import { AdjudicatorPenaltiesService } from '../penalties/penalties.service';
import { EventsService } from '../../events/events.service';
import { ClubsService } from '../../clubs/clubs.service';

@Component({
    selector: 'entries',
    styleUrls: [ './entries.component.css' ],
    templateUrl: './entries.component.html',
    providers: [
        PositionsService,
        EntryTimesFullService,
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
    private clubs: Club[] = new Array<Club>();
    private events: Event[] = new Array<Event>();
    private entryTimes: EntryTime[];

    constructor(
        private route: ActivatedRoute,
        private racesService: AdjudicatorRacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesFullService,
        private disqualificationService: AdjudicatorDisqualificationService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
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

        this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
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

    public getEventNameForEntry(entry: Entry): string {
        let eventName: string;
        let event: Event = this.getEventForId(entry.event);
        if (event) {
            eventName = event.name;
        }
        return eventName;
    }

    public getClubNamesForEntry(entry: Entry): string {
        let clubs: string;
        for (let clubId of entry.clubs) {
            let club: Club = this.getClubForId(clubId);
            if (club) {
                if (!clubs) {
                    clubs = club.clubName;
                } else {
                    clubs = clubs.concat(' / ');
                    clubs = clubs.concat(club.clubName);
                }
            }
        }
        return clubs;
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
                    === entryTime.entry.event)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                        === entryTime.entry.event)
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
        return entryTimes.filter((entryTime: EntryTime) => {
            for (let clubId of entryTime.entry.clubs) {
                let club: Club = this.getClubForId(clubId);
                if (club) {
                    if (club.clubName.includes(value)) {
                        return entryTime;
                    }
                }
            }
        });
    }

    private filterEvents(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            let event: Event = this.getEventForId(entryTime.entry.event);
            if (event) {
                if (event.name.includes(value)) {
                    return entryTime;
                }
            }
        });
    }

    private getEventForId(eventId: number): Event {
        for (let event of this.events) {
            if (event.id === eventId) {
                return event;
            }
        }
        return null;
    }

    private getClubForId(clubId: number): Club {
        for (let club of this.clubs) {
            if (club.id === clubId) {
                return club;
            }
        }
        return null;
    }
}
